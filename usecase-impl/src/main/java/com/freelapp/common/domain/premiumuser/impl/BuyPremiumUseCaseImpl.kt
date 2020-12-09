package com.freelapp.common.domain.premiumuser.impl

import com.freelapp.common.application.askforinput.AskForSingleChoiceInput
import com.freelapp.common.domain.annotation.ChooseSubscriptionPlanMessageString
import com.freelapp.common.domain.annotation.ChooseSubscriptionPlanTitleString
import com.freelapp.common.domain.annotation.IsAlreadySubscribedString
import com.freelapp.common.domain.premiumuser.CheckPremiumStatusUseCase
import com.freelapp.common.domain.premiumuser.BuyPremiumUseCase
import com.freelapp.components.biller.entity.purchase.BillingFlow
import com.freelapp.components.biller.entity.sku.AcknowledgeableSku
import com.freelapp.components.biller.entity.sku.SkuContract
import com.freelapp.components.snacker.domain.Snacker
import javax.inject.Inject
import javax.inject.Provider

class BuyPremiumUseCaseImpl @Inject constructor(
    private val snacker: Snacker,
    private val checkPremiumStatusUseCase: CheckPremiumStatusUseCase,
    private val billingFlow: BillingFlow,
    private val subscriptionSkus: Map<AcknowledgeableSku, Provider<String>>,
    private val askForSingleChoiceInput: AskForSingleChoiceInput<AcknowledgeableSku>,
    @IsAlreadySubscribedString
    private val isAlreadySubscribedMsg: String,
    @ChooseSubscriptionPlanTitleString
    private val chooseSubscriptionPlanTitle: String,
    @ChooseSubscriptionPlanMessageString
    private val chooseSubscriptionPlanMsg: String
) : BuyPremiumUseCase {

    override suspend fun invoke() {
        if (checkPremiumStatusUseCase().value) {
            snacker(isAlreadySubscribedMsg)
            return
        }
        val sku = getSubscriptionOptionInput() ?: return
        billingFlow(sku)
    }

    private suspend fun getSubscriptionOptionInput(): SkuContract? =
        askForSingleChoiceInput(
            chooseSubscriptionPlanTitle,
            chooseSubscriptionPlanMsg,
            subscriptionSkus
        )
}