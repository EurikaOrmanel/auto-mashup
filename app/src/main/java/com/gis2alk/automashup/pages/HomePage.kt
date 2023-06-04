package com.gis2alk.automashup.pages

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.gis2alk.automashup.models.MashUpHistoryDTO
import com.gis2alk.automashup.services.RoomDBHelper
import com.gis2alk.automashup.viewmodel.MashUpHIstoryViewModel
import com.gis2alk.automashup.widgets.MashUpHistoryItem
import com.google.accompanist.permissions.PermissionStatus


@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomePage(dbHelper: RoomDBHelper) {
    val mashUpHIstoryViewModel = MashUpHIstoryViewModel(dbHelper.mashupHistoryDAO())
    val context = LocalContext.current
    var numberOfPurchases by rememberSaveable { mutableStateOf(1) }
    val permissionsState =
        rememberPermissionState(permission = Manifest.permission.CALL_PHONE)

    val allHistory by mashUpHIstoryViewModel.allHistory.observeAsState(emptyList())
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    stringResource(
                        id = R.string.app_name,
                    ),
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
            }
            )
        },
        bottomBar = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "By Gis2alk(Telegram)",
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("https://t.me/gis2alk")
                        }
                        context.startActivity(intent)
                    }, color = if (isSystemInDarkTheme()) Color.White else Color.Blue
                )
            }

        }
    ) {
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
                        colorFilter = ColorFilter.tint(if (isSystemInDarkTheme()) Color.White else Color.Yellow)
                    )
//                    Text("Buy Mashup")
                }
            }
            LazyColumn {
                items(allHistory) { history ->
                    MashUpHistoryItem(historyDTO = history)

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
    onInputChanged: (Int) -> Unit
) {
    val inputFocus = LocalFocusManager.current
    var inputIsInvalid by rememberSaveable { mutableStateOf(true) }
    var inputText by rememberSaveable { mutableStateOf("1") }
    Column {

        OutlinedTextField(
            modifier = Modifier.padding(10.dp, 0.dp),
            value = inputText,
            onValueChange = {
                inputText = it
                if (inputText.isNumber()) {
                    onInputChanged.invoke(it.toInt())
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