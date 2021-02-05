package com.freelapp.common.datasource.impl

import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQuery
import com.freelapp.common.datasource.NearbyUsersDataSource
import com.freelapp.common.annotation.ApplicationCoroutineScope
import com.freelapp.common.datasource.impl.ktx.DataFlow
import com.freelapp.common.datasource.impl.ktx.getSnapshot
import com.freelapp.common.entity.*
import com.freelapp.common.entity.item.Item
import com.freelapp.geofire.asTypedFlow
import com.freelapp.geofire.model.LocationData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

open class NearbyUsersDataSourceImpl<UserDaoType, UserType, DataType> @Inject constructor(
    @ApplicationCoroutineScope private val scope: CoroutineScope,
    private val db: FirebaseDatabase,
    private val geoFire: GeoFire,
    private val clazz: Class<UserDaoType>,
    private val userDaoToUserMapper: UserDaoType.(Pair<Latitude, Longitude>) -> UserType?
) : NearbyUsersDataSource<UserType, DataType> where UserType : User<DataType>,
                                                    DataType : Item,
                                                    UserDaoType : Any {

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
            .shareIn(scope, SharingStarted.WhileSubscribed(replayExpirationMillis = 0), 1)

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
            .map { snap -> snap.children.map { it.key!! to it.child("l").toLatLng() }.toMap() }
            .flowOn(Dispatchers.Default)
            .shareIn(scope, SharingStarted.WhileSubscribed(replayExpirationMillis = 0), 1)

    private fun DataSnapshot.toLatLng() =
        getValue(object : GenericTypeIndicator<List<Double>>() {})!!
            .run { get(0) to get(1) }

    @ExperimentalCoroutinesApi
    override val globalUsers: SharedFlow<Map<Key, UserType>> =
        globalUsersPositions
            .mapLatest {
                coroutineScope {
                    it.mapValues { (key, location) ->
                        async {
                            db
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
            .shareIn(scope, SharingStarted.WhileSubscribed(replayExpirationMillis = 0), 1)

    override fun queryAtLocation(location: Pair<Latitude, Longitude>, radius: Int) {
        val query = geoQuery.value
        val geoLocation = GeoLocation(location.first, location.second)
        if (query != null) {
            query.setLocation(geoLocation, radius.toDouble())
        } else {
            geoQuery.value = geoFire.queryAtLocation(geoLocation, radius.toDouble())
        }
    }
}