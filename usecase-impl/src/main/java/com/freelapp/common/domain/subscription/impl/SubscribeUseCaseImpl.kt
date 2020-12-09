package com.freelapp.common.domain.subscription.impl

import com.freelapp.common.application.askforinput.AskForSingleChoiceInput
import com.freelapp.common.domain.subscription.CheckSubscriptionUseCase
import com.freelapp.common.domain.subscription.SubscribeUseCase
import com.freelapp.components.biller.entity.purchase.BillingFlow
import com.freelapp.components.biller.entity.sku.AcknowledgeableSku
import com.freelapp.components.biller.entity.sku.SkuContract
import com.freelapp.components.snacker.domain.Snacker

class SubscribeUseCaseImpl(
    private val snacker: Snacker,
    private val checkSubscriptionUseCase: CheckSubscriptionUseCase,
    private val billingFlow: BillingFlow,
    private val subscriptionSkus: Map<AcknowledgeableSku, String>,
    private val askForSingleChoiceInput: AskForSingleChoiceInput<AcknowledgeableSku>,
    private val isAlreadySubscribedMsg: String,
    private val chooseSubscriptionPlanTitle: String,
    private val chooseSubscriptionPlanMsg: String
) : SubscribeUseCase {

    override suspend fun invoke() {
        if (checkSubscriptionUseCase().value) {
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