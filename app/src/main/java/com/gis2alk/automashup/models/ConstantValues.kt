package com.gis2alk.automashup.models

import com.gis2alk.automashup.pages.getDeviceName

object ConstantValues {

    val simSlotName = arrayOf(
        "extra_asus_dial_use_dualsim",
        "com.android.phone.extra.slot",
        "slot",
        "simslot",
        "sim_slot",
        "subscription",
        "Subscription",
        "phone",
        "com.android.phone.DialingMode",
        "simSlot",
        "slot_id",
        "simId",
        "simnum",
        "phone_type",
        "slotId",
        "slotIdx"
    )
    val redmi = DevicesCustomVals(
        deviceName = "Redmi", DevicesViewIds(
            ussdText = "com.android.phone:id/ussd_message",
            ussdInputField = "com.android.phone:id/input_field",
            sendButton = "android:id/button1",
            cancelButton = "android:id/button2",
            dialogClassName = "dialog_dim_bg"
        )
    )

    private val devicesToMap = mapOf(
        "xiaomi" to DevicesViewIds(
            ussdText = "com.android.phone:id/ussd_message",
            ussdInputField = "com.android.phone:id/input_field",
            cancelButton = "android:id/button2",
            sendButton = "android:id/button1",
            dialogClassName = "miuix.appcompat.app.AlertDialog"
        ),
        "tcl" to DevicesViewIds(
            ussdText = "com.android.phone:id/msg_text",
            ussdInputField = "com.android.phone:id/msg_text",
            sendButton = "android:id/button1",
            cancelButton = "android:id/button2",
            dialogClassName = "android.app.AlertDialog"
        ),
        "huawei" to DevicesViewIds(
            ussdText = "android:id/message",
            ussdInputField = "com.android.phone:id/input_field",
            sendButton = "android:id/button1",
            cancelButton = "android:id/button2",
            dialogClassName = "android.app.AlertDialog"
        ),
        "tecno" to DevicesViewIds(
            ussdInputField = "com.android.phone:id/input_field",
            ussdText = "android:id/message",
            dialogClassName = "android.app.AlertDialog",
            sendButton = "android:id/button1",
            cancelButton = "android:id/button2"
        ),
        "samsung sm-g" to DevicesViewIds(
            ussdText = "com.android.phone:id/dialog_message",
            ussdInputField = "com.android.phone:id/input_field",
            cancelButton = "android:id/button2",
            sendButton = "android:id/button1",
            dialogClassName = "android.app.AlertDialog"
        ),
        "samsung sm-n" to DevicesViewIds(
            ussdText = "com.android.phone:id/dialog_message",
            ussdInputField = "com.android.phone:id/input_field",
            cancelButton = "android:id/button2",
            sendButton = "android:id/button1",
            dialogClassName = "android.app.AlertDialog"
        ),
        "samsung sm-a" to DevicesViewIds(
            ussdText = "com.android.phone:id/message",
            ussdInputField = "com.android.phone:id/input_field",
            cancelButton = "android:id/button2",
            sendButton = "android:id/button1",
            dialogClassName = "android.app.AlertDialog"
        ),
        "default" to DevicesViewIds(
            ussdInputField = "com.android.phone:id/input_field",
            ussdText = "android:id/message",
            dialogClassName = "android.app.AlertDialog",
            sendButton = "android:id/button1",
            cancelButton = "android:id/button2"
        )
    )
    val mashUpUSSDProcedureForSelf =

        USSDProcedure(
            ussdSteps = listOf(
//                USSDStep(option = "1", lookout = "welcome to mtn pulse."),
//                USSDStep(option = "1", lookout = "welcome to mtn pulse. please select an option"),
//                USSDStep(
//                    option = "1", lookout = "mashup offers"
//                ),
//                USSDStep(
//                    option = "2", lookout = "Select one of the following:"
//                ),
                USSDStep(
                    option = "0.07",
                    lookout = "enter amount to buy your preferred bundle (ghc 0.07 - 4.99)"
                ),
                USSDStep(
                    option = "5", lookout = "select your preferred option"
                ),
                USSDStep(
                    option = "1", lookout = "choose payment mode"
                ),
            )
        )

    fun currentDeviceViewIds(): DevicesViewIds {
        val deviceName = getDeviceName()
        val currentDeviceMapKey = devicesToMap.keys.filter { currentMapKey ->
            deviceName.contains(currentMapKey, true)
        }
        if (currentDeviceMapKey.size == 1) {
            return devicesToMap[currentDeviceMapKey.first()]!!
        } else {
            return devicesToMap["default"] as DevicesViewIds;
        }
//        throw Exception("Device not found")
    }
}