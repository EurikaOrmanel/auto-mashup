package com.gis2alk.automashup.services

import android.accessibilityservice.AccessibilityService
import android.os.Bundle
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.gis2alk.automashup.models.ConstantValues
import com.gis2alk.automashup.models.USSDProcedure

class UssdAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val source = event?.source

        if (source != null) {
            if (event.className?.equals("android.app.AlertDialog") == true) {
                processAProcedure(ConstantValues.mashUpUSSDProcedureForSelf, source)

            }
        }

    }

    private fun processAProcedure(procedure: USSDProcedure, source: AccessibilityNodeInfo) {
        val contentBody = source.findAccessibilityNodeInfosByViewId("com.android.phone:id/msg_text")
        val contentBodyTexts = contentBody.map { each -> each.text.toString().lowercase() }
        println(contentBodyTexts)
        val currentStep = procedure.ussdSteps.filter { ussdStep ->
            contentBodyTexts.first().contains(ussdStep.lookout)
        }
        print("Current Step: $currentStep")
        inputInFieldAndSend(source, currentStep.first().option)
        clickSendView(source)

    }

    override fun onInterrupt() {
        TODO("Not yet implemented")
    }

    private fun inputInFieldAndSend(source: AccessibilityNodeInfo, value: String) {
        val inputNode = source.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)
        if (inputNode != null) {
            val arguments = Bundle().apply {
                putCharSequence(
                    AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                    value
                );
            }
            inputNode.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)

        }

    }

    private fun clickSendView(source: AccessibilityNodeInfo) {

        val nodesById = source.findAccessibilityNodeInfosByViewId("android:id/button1")
        nodesById.forEach {
            it.performAction(AccessibilityNodeInfo.ACTION_CLICK)

        }

    }
}
