package com.gis2alk.automashup.pages

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.telephony.SubscriptionManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gis2alk.automashup.R
import com.gis2alk.automashup.models.ConstantValues
import com.gis2alk.automashup.models.MashUpHistoryDTO
import com.gis2alk.automashup.services.RoomDBHelper
import com.gis2alk.automashup.viewmodel.JoinUsPref
import com.gis2alk.automashup.viewmodel.MashUpHIstoryViewModel
import com.gis2alk.automashup.widgets.JoinDialog
import com.gis2alk.automashup.widgets.MashUpHistoryItem
import com.google.accompanist.permissions.*
import java.util.Locale


@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomePage(dbHelper: RoomDBHelper, sharedPreferences: SharedPreferences) {
    val joinUsPref = JoinUsPref(sharedPreferences)

    val mashUpHIstoryViewModel = MashUpHIstoryViewModel(dbHelper.mashupHistoryDAO())
    val context = LocalContext.current
    var openJoinUsDialog by remember { mutableStateOf(!joinUsPref.userJoinedCheck()) }
    var numberOfPurchases by rememberSaveable { mutableStateOf(1) }
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE
        )
    )

    val allHistory by mashUpHIstoryViewModel.allHistory.observeAsState(emptyList())
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                stringResource(
                    id = R.string.app_name,
                ), style = TextStyle(fontWeight = FontWeight.Bold)
            )
        })
    }, bottomBar = {
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,

            ) {
            Text(
                "By Gis2alk(Telegram)",
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("https://t.me/gis2alk")
                    }
                    context.startActivity(intent)
                },
                color = if (isSystemInDarkTheme()) Color.White else Color.Blue
            )
        }

    }) {
        if (openJoinUsDialog) {
            JoinDialog(joinUsPref) { value ->
                if (value) {
                    joinUsPref.markasJoined()

                }
                openJoinUsDialog = false
            }
        }
        Column(
            modifier = Modifier.padding(it)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PhoneNumberInput { newValue ->
                    numberOfPurchases = newValue
                }
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(contentColor = if (isSystemInDarkTheme()) Color.White else Color.Yellow),
                    modifier = Modifier.height(IntrinsicSize.Max),
                    onClick = {
                        callButtonCallBack(
                            context,
                            permissionsState,
                            mashUpHIstoryViewModel,
                            numberOfPurchases,
                        )
                        println("Device name: ${ConstantValues.currentDeviceViewIds()}")

                    },
                ) {
                    Image(
                        Icons.Default.Call,
                        contentDescription = "Dial",
                        colorFilter = ColorFilter.tint(
                            if (isSystemInDarkTheme()) Color.White else Color.Black
                        )
                    )
                }
            }
            if (allHistory.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                Text("History", style = TextStyle(fontSize = 25.sp))
            }
            LazyColumn {
                items(allHistory) { history ->
                    MashUpHistoryItem(
                        historyDTO = history, hIstoryViewModel = mashUpHIstoryViewModel
                    )

                }
            }
        }
    }
}


@Composable
@Preview
fun HomePagePreview() {
//    HomePage(dbHelper)
}


fun Context.sendRequest(calledInExistingActivity: Boolean = true) {

    val simCardNames = getSimCardNetworks(this)
    simCardNames.mapIndexed { index, networkName ->
        if (networkName.contains("MTN")) {
            val uri = "tel:*567*1*1*1*2" + Uri.encode("#")
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse(uri)
                ConstantValues.simSlotName.map { putExtra(it, index) }

                if (!calledInExistingActivity) {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }
            this.startActivity(intent)
        } else {
            Toast.makeText(
                this,
                "There sim to be no MTN sim card in this device.",
                Toast.LENGTH_LONG
            )
                .show()
        }

    }

}


@Composable
fun PhoneNumberInput(
    onInputChanged: (Int) -> Unit
) {
    val inputFocus = LocalFocusManager.current
    var inputIsInvalid by rememberSaveable { mutableStateOf(true) }
    var inputText by rememberSaveable { mutableStateOf("1") }
    Column {

        OutlinedTextField(modifier = Modifier.padding(10.dp, 0.dp),
            value = inputText,
            onValueChange = {
                inputText = it
                if (inputText.isNumber()) {
                    onInputChanged.invoke(it.toInt())
                }
            },
            isError = !inputIsInvalid,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    inputIsInvalid = inputText.isNumber()
                    inputFocus.clearFocus()

                },
            ),

            label = { Text("Number of times") })

        if (!inputIsInvalid) {
            Text("Please input numbers only", color = Color.Red)
        }
    }

}

fun String.isPhoneNumber() = this.matches("^0[0-9]{9}".toRegex())

fun String.isNumber() = this.matches(Regex("\\d"))


fun Context.isAccessibilityServiceRunning(): Boolean {
    val accessibilityEnabled =
        Settings.Secure.getInt(this.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED, 0)
    return if (accessibilityEnabled == 1) {
        true
    } else {
        Toast.makeText(
            this,
            "Please switch on the accessibility service for ${this.getString(R.string.app_name)}",
            Toast.LENGTH_LONG
        ).show()
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        this.startActivity(intent)
        false;
    }

}


@SuppressLint("NewApi")
@OptIn(ExperimentalPermissionsApi::class)
fun callButtonCallBack(
    context: Context,
    permissionsState: MultiplePermissionsState,
    mashUpHIstoryViewModel: MashUpHIstoryViewModel,
    numberOfPurchases: Int,
) {
    when (permissionsState.permissions.first().status) {
        PermissionStatus.Granted -> {
            try {
                val isAccessibilityPermitted = context.isAccessibilityServiceRunning()
                if (isAccessibilityPermitted) {
                    mashUpHIstoryViewModel.addOne(
                        MashUpHistoryDTO(
                            total = numberOfPurchases.toInt(),
                            completed = 0,
                            timestamp = Calendar.getInstance().time
                        )
                    )
                    context.sendRequest()

                }
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
        is PermissionStatus.Denied -> {
            try {
                permissionsState.launchMultiplePermissionRequest()
            } catch (e: Exception) {
                Toast.makeText(
                    context, "Please permit us to read yo shit", Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}


// Request the READ_PHONE_STATE permission if not granted
fun requestPhoneStatePermission(activity: Activity) {
    ActivityCompat.requestPermissions(
        activity, arrayOf(Manifest.permission.READ_PHONE_STATE), 0
    )
}

// Get the available SIM card network names
fun getSimCardNetworks(context: Context): List<String> {
    val subscriptionManager =
        context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

    if (ContextCompat.checkSelfPermission(
            context, Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        val subscriptionInfoList = subscriptionManager.activeSubscriptionInfoList

        return subscriptionInfoList.map { it.carrierName.toString() }

    }
    throw Exception("Please permit device to access phone state.")


}


fun getDeviceName(): String {
    val manufacturer = Build.MANUFACTURER
    val model = Build.MODEL
    return if (model.startsWith(manufacturer)) {
        model
    } else {
        "$manufacturer $model".lowercase(Locale.getDefault())
    }
}