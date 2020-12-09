package com.freelapp.common.domain.usersearchmode.impl

import com.freelapp.common.application.getlocationname.GetLocationName
import com.freelapp.common.domain.subscription.CheckSubscriptionUseCase
import com.freelapp.common.domain.subscription.SubscribeUseCase
import com.freelapp.common.domain.usersearchmode.SetUserSearchModeUseCase
import com.freelapp.common.domain.usersearchradius.GetUserSearchRadiusUseCase
import com.freelapp.common.entity.Item
import com.freelapp.common.entity.SearchMode
import com.freelapp.common.entity.User
import com.freelapp.common.repository.user.UserRepository
import com.freelapp.components.snacker.domain.Snacker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SetUserSearchModeUseCaseImpl<UserType, DataType>(
    private val scope: CoroutineScope,
    private val getUserSearchRadiusUseCase: GetUserSearchRadiusUseCase,
    private val checkSubscriptionUseCase: CheckSubscriptionUseCase,
    private val subscribeUseCase: SubscribeUseCase,
    private val snacker: Snacker,
    private val worldwideString: String,
    private val getLocationName: GetLocationName,
    private val userRepository: UserRepository<UserType, DataType>
) : SetUserSearchModeUseCase where UserType : User<UserType, DataType>,
                                   DataType : Item<DataType> {

    override fun invoke(searchMode: SearchMode) {
        scope.launch {
            if (checkSubscriptionUseCase().value) {
                userRepository.setSearchMode(searchMode)
                if (searchMode is SearchMode.Nearby.Custom) {
                    getLocationName(
                        searchMode.location,
                        getUserSearchRadiusUseCase().value
                    )?.let {
                        snacker(it)
                    }
                } else if (searchMode is SearchMode.Worldwide) {
                    snacker(worldwideString)
                }
            } else subscribeUseCase()
        }
    }


}