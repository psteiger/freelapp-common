package com.freelapp.common.domain.premiumuser.impl

import com.freelapp.common.domain.premiumuser.CheckPremiumStatusUseCase
import com.freelapp.components.biller.entity.purchase.PurchaseState
import com.freelapp.components.biller.entity.sku.AcknowledgeableSku
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class CheckPremiumStatusUseCaseImpl @Inject constructor(
    scope: CoroutineScope,
    purchaseStateOwner: PurchaseState.Owner,
    private val premiumSkus: Set<@JvmSuppressWildcards AcknowledgeableSku>
) : CheckPremiumStatusUseCase {

    private val isSubscribed =
        purchaseStateOwner
            .purchaseState
            .acknowledged
            .map { skuSet ->
                val skus = skuSet.map { it.sku }
                premiumSkus.any { it.sku in skus }
            }
            .stateIn(scope, SharingStarted.Eagerly, false)

    override fun invoke(): StateFlow<Boolean> = isSubscribed
}