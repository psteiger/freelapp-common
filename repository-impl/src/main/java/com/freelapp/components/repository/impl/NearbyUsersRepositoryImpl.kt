package com.freelapp.components.repository.impl

import android.content.Context
import android.location.Location
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQuery
import com.freelapp.common.entity.*
import com.freelapp.common.entity.item.Item
import com.freelapp.common.repository.user.UserRepository
import com.freelapp.components.repository.impl.ktx.DataFlow
import com.freelapp.components.repository.impl.ktx.getSnapshot
import com.freelapp.components.repository.impl.ktx.persist.*
import com.freelapp.flowlifecycleobserver.collectWhileStartedIn
import com.freelapp.geofire.asTypedFlow
import com.freelapp.geofire.model.LocationData
import com.freelapp.libs.locationfetcher.LocationSource
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

open class NearbyUsersRepositoryImpl<UserDaoType, UserType, DataType>(
    private val locationSource: LocationSource,
    geoFire: GeoFire,
    context: Context,
    private val clazz: Class<UserDaoType>,
    private val userDaoToUserMapper: UserDaoType.(Pair<Latitude, Longitude>) -> UserType?
) : UserRepository<UserType, DataType> where UserType : User<DataType>,
                                             DataType : Item,
                                             UserDaoType : Any {

    private val _showMyItemsFlow = MutableStateFlow(true)
    private var _showMyItems: Boolean by context.applicationContext.persist(_showMyItemsFlow)
    override val hideShowOwnData = _showMyItemsFlow.asStateFlow()
    override fun setHideShowOwnData(show: Boolean) {
        _showMyItems = show
    }

    private val _searchRadiusFlow = MutableStateFlow(300)
    private var _searchRadius: Int by context.applicationContext.persist(_searchRadiusFlow)
    final override val searchRadius = _searchRadiusFlow.asStateFlow()
    override fun setSearchRadius(radius: Int) {
        _searchRadius = radius
    }

    private val _searchText = MutableStateFlow("")
    override val searchText = _searchText.asStateFlow()
    override fun setSearchFilter(query: String) {
        _searchText.value = query
    }

    private val _searchMode: MutableStateFlow<SearchMode> = MutableStateFlow(SearchMode.Nearby.Real)
    override val searchMode = _searchMode.asStateFlow()
    override fun setSearchMode(searchMode: SearchMode) {
        _searchMode.value = searchMode
        if (searchMode is SearchMode.Nearby.Custom) {
            val location = Location("CustomLocation").apply {
                latitude = searchMode.location.first
                longitude = searchMode.location.second
            }
            locationSource.setCustomLocation(location)
            locationSource.setPreferredSource(LocationSource.Source.CUSTOM)
        } // TODO way to change back to real location
    }

    private val geoQuery = MutableStateFlow<GeoQuery?>(null)

    @ExperimentalCoroutinesApi
    override val nearbyUsers: SharedFlow<Map<Key, UserType>> =
        geoQuery
            .filterNotNull()
            .flatMapLatest {
                it
                    .asTypedFlow(clazz, "users")
                    .toUserMapFlow()
            }
            .onEmpty { emit(emptyMap()) }
            .conflate()
            .flowOn(Dispatchers.Default)
            .shareIn(
                ProcessLifecycleOwner.get().lifecycleScope,
                SharingStarted.WhileSubscribed(replayExpirationMillis = 0),
                replay = 1
            )

    @ExperimentalCoroutinesApi
    private fun Flow<Map<Key, LocationData<UserDaoType>>>.toUserMapFlow(): Flow<Map<Key, UserType>> =
        mapLatest {
            it
                .mapValues { (_, locationData) ->
                    val (location, userDao) = locationData
                    userDao?.userDaoToUserMapper(location.latitude to location.longitude)
                }
                .filterNot { (_, user) -> user == null }
                .mapValues { (_, user) -> user as UserType }
        }.flowOn(Dispatchers.Default)

    final override val globalUsersPositions: SharedFlow<Map<Key, Pair<Latitude, Longitude>>> =
        DataFlow("geofire")
            .singleValue
            .filterNotNull()
            .map {
                it.children.map { snap ->
                    snap.key!! to snap.child("l")
                        .getValue(object : GenericTypeIndicator<List<Double>>() {})!!
                        .run { get(0) to get(1) }
                }.toMap()
            }
            .flowOn(Dispatchers.Default)
            .shareIn(
                ProcessLifecycleOwner.get().lifecycleScope,
                SharingStarted.WhileSubscribed(replayExpirationMillis = 0),
                replay = 1
            )

    @ExperimentalCoroutinesApi
    override val globalUsers: SharedFlow<Map<Key, UserType>> =
        globalUsersPositions
            .mapLatest {
                coroutineScope {
                    it.mapValues { (key, location) ->
                        async {
                            FirebaseDatabase.getInstance()
                                .getReference("users/$key")
                                .getSnapshot()
                                ?.getValue(object : GenericTypeIndicator<UserDaoType>() {})
                                ?.userDaoToUserMapper(location)
                        }
                    }.values.awaitAll().filterNotNull().associateBy { it.id }
                }
            }
            .conflate()
            .flowOn(Dispatchers.IO)
            .shareIn(
                ProcessLifecycleOwner.get().lifecycleScope,
                SharingStarted.WhileSubscribed(replayExpirationMillis = 0),
                replay = 1
            )

    init {
        searchRadius // force read TODO
        combine(
            geoQuery,
            locationSource.location.filterNotNull(),
            searchRadius
        ) { query, location, radius ->
            val geoLocation = GeoLocation(location.latitude, location.longitude)
            if (query != null) {
                query.setLocation(geoLocation, radius.toDouble())
            } else {
                geoQuery.value = geoFire.queryAtLocation(geoLocation, radius.toDouble())
            }
        }.collectWhileStartedIn(ProcessLifecycleOwner.get())
    }
}