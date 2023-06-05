package com.gis2alk.automashup.services

import android.accessibilityservice.AccessibilityService
import android.os.Bundle
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.room.Room
import com.gis2alk.automashup.models.ConstantValues
import com.gis2alk.automashup.models.USSDProcedure
import com.gis2alk.automashup.pages.sendRequest
import com.gis2alk.automashup.repo.MashUpRepo
import com.gis2alk.automashup.viewmodel.MashUpHIstoryViewModel
import kotlinx.coroutines.*

class UssdAccessibilityService : AccessibilityService() {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val dbHelper =
            Room.databaseBuilder(applicationContext, RoomDBHelper::class.java, "history").build()

        val source = event?.source
        if (source != null) {
            if (event.className?.equals("android.app.AlertDialog") == true) {
                processAProcedure(ConstantValues.mashUpUSSDProcedureForSelf, source, dbHelper)

            }
        }

    }

    private val completedMessage =
        "congrats! you have purchased a mashup of ghc0.07 to be used for all networks plus free whatsapp. dial *550# to check"

    private fun processAProcedure(
        procedure: USSDProcedure,
        source: AccessibilityNodeInfo,
        dbHelper: RoomDBHelper
    ) {
        val contentBody = source.findAccessibilityNodeInfosByViewId("com.android.phone:id/msg_text")
        val contentBodyText = contentBody.first().toString().lowercase()
        val currentStep = procedure.ussdSteps.filter { ussdStep ->
            contentBodyText.contains(ussdStep.lookout)
        }
        if (contentBodyText.contains(completedMessage)) {
            clickViewById(source, "android:id/button2")
            val mashUpHIstoryViewModel = MashUpRepo(dbHelper.mashupHistoryDAO())
            runBlocking {
                val currentlyWorked = mashUpHIstoryViewModel.getLastOne()
                print(currentlyWorked)
                mashUpHIstoryViewModel.increaseCompleted(currentlyWorked.id!!)
                if (currentlyWorked.completed < currentlyWorked.total) {
                    applicationContext.sendRequest(false)
                }
            }

            return
        }
        if (currentStep.isNotEmpty()) {
            inputInFieldAndSend(source, currentStep.first().option)
            clickViewById(source, "android:id/button1")
        }

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

    private fun clickViewById(source: AccessibilityNodeInfo, buttonId: String) {

        val nodesById = source.findAccessibilityNodeInfosByViewId(buttonId)
        nodesById.forEach {
            it.performAction(AccessibilityNodeInfo.ACTION_CLICK)

        }

    }
}
