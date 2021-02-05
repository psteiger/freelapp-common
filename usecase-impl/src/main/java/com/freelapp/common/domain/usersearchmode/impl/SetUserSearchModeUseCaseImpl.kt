package com.freelapp.common.domain.usersearchmode.impl

import com.freelapp.common.annotation.ApplicationCoroutineScope
import com.freelapp.common.annotation.WorldwideString
import com.freelapp.common.application.getlocationname.GetLocationName
import com.freelapp.common.domain.premiumuser.CheckPremiumStatusUseCase
import com.freelapp.common.domain.premiumuser.BuyPremiumUseCase
import com.freelapp.common.domain.usersearchmode.SetUserSearchModeUseCase
import com.freelapp.common.domain.usersearchradius.GetUserSearchRadiusUseCase
import com.freelapp.common.entity.item.Item
import com.freelapp.common.entity.SearchMode
import com.freelapp.common.entity.User
import com.freelapp.common.repository.user.UserRepository
import com.freelapp.components.snacker.domain.Snacker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class SetUserSearchModeUseCaseImpl<UserType, DataType> @Inject constructor(
    @ApplicationCoroutineScope private val scope: CoroutineScope,
    private val getUserSearchRadiusUseCase: GetUserSearchRadiusUseCase,
    private val checkPremiumStatusUseCase: CheckPremiumStatusUseCase,
    private val buyPremiumUseCase: BuyPremiumUseCase,
    private val snacker: Snacker,
    @WorldwideString
    private val worldwideString: String,
    private val getLocationName: GetLocationName,
    private val userRepository: UserRepository<UserType, DataType>
) : SetUserSearchModeUseCase where UserType : User<DataType>,
                                   DataType : Item {

    override fun invoke(searchMode: SearchMode) {
        scope.launch {
            if (checkPremiumStatusUseCase().value) {
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
            } else buyPremiumUseCase()
        }
    }


}