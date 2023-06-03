package com.gis2alk.automashup.pages

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.gis2alk.automashup.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.gis2alk.automashup.models.MashUpHistoryDTO
import com.gis2alk.automashup.services.RoomDBHelper
import com.gis2alk.automashup.viewmodel.MashUpHIstoryViewModel
import com.google.accompanist.permissions.PermissionStatus


@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomePage(dbHelper: RoomDBHelper) {
    val mashUpHIstoryViewModel = MashUpHIstoryViewModel(dbHelper.mashupHistoryDAO())
    val context = LocalContext.current
    var numberOfPurchases by rememberSaveable { mutableStateOf("") }
    val permissionsState =
        rememberPermissionState(permission = Manifest.permission.CALL_PHONE)

//    val allHistory by mashUpHIstoryViewModel.allHistory.observeAsState()
    Scaffold(
        topBar = {
            SmallTopAppBar(title = {
                Text(
                    stringResource(
                        id = R.string.app_name,
                    ),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                PhoneNumberInput { newValue ->
                    numberOfPurchases = newValue
                }
                OutlinedButton(
                    modifier = Modifier
                        .height(IntrinsicSize.Max),
                    onClick = {
                        when (permissionsState.status) {
                            PermissionStatus.Granted -> {
                                context.sendRequest()
                                mashUpHIstoryViewModel.addOne(
                                    MashUpHistoryDTO(
                                        total = numberOfPurchases.toInt(),
                                        completed = 0,
                                        timestamp = Calendar.getInstance().time
                                    )
                                )
                            }
                            is PermissionStatus.Denied -> {
                                try {
                                    permissionsState.launchPermissionRequest()
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        context,
                                        "Please permit us to read yo shit",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    },
                ) {
                    Image(
                        Icons.Default.Call, contentDescription = "Dial",
                    )
//                    Text("Buy Mashup")
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
    val uri = "tel:*567*1*1*1*2" + Uri.encode("#")
    val intent = Intent(Intent.ACTION_CALL).apply {
        data = Uri.parse(uri)
        if (!calledInExistingActivity) {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
    this.startActivity(intent)
}


@Composable
fun PhoneNumberInput(
    onInputChanged: (String) -> Unit
) {
    val inputFocus = LocalFocusManager.current
    var inputIsInvalid by rememberSaveable { mutableStateOf(true) }
    var inputText by rememberSaveable { mutableStateOf("") }
    Column {

        OutlinedTextField(
//            modifier = Modifier.,
            value = inputText,
            onValueChange = {
                inputText = it
                if (inputText.isNumber()) {
                    onInputChanged.invoke(it)
                }
            },
            isError = !inputIsInvalid,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    inputIsInvalid = inputText.isPhoneNumber()
                    inputFocus.clearFocus()
                },
            ),

            label = { Text("Number of times") }
        )

        if (!inputIsInvalid) {
            Text("Please input a valid phone number", color = Color.Red)
        }
    }

}

fun String.isPhoneNumber() = this.matches("^0[0-9]{9}".toRegex())

fun String.isNumber() = this.matches(Regex("\\d"))