package com.gis2alk.automashup.widgets

import android.Manifest
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.gis2alk.automashup.models.MashUpHistoryDTO
import com.gis2alk.automashup.pages.callButtonCallBack
import com.gis2alk.automashup.viewmodel.MashUpHIstoryViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import java.text.SimpleDateFormat

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MashUpHistoryItem(historyDTO: MashUpHistoryDTO, hIstoryViewModel: MashUpHIstoryViewModel) {
    val formatter = SimpleDateFormat("c d, LLL HH:mm")
    val formattedDate = formatter.format(historyDTO.timestamp)
    val permissionsState =
        rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_PHONE_STATE
            )
        )
    val context = LocalContext.current
    ListItem(leadingContent = {
        IconButton(
            onClick = { hIstoryViewModel.deleteOne(historyDTO) },
            colors = IconButtonDefaults.iconButtonColors(contentColor = if (isSystemInDarkTheme()) Color.White else Color.Black),
        ) {
            Image(
                Icons.Default.Delete, contentDescription = "Delete History",
                colorFilter = ColorFilter.tint(if (isSystemInDarkTheme()) Color.White else Color.Black)
            )
        }
    }, trailingContent = {
        IconButton(
            onClick = {
                callButtonCallBack(
                    context,
                    permissionsState,
                    hIstoryViewModel,
                    historyDTO.total,
                )
            },
            colors = IconButtonDefaults.iconButtonColors(contentColor = if (isSystemInDarkTheme()) Color.White else Color.Black),
        ) {
            Image(
                Icons.Default.Refresh, contentDescription = "Delete History",
                colorFilter = ColorFilter.tint(
                    if (isSystemInDarkTheme()) Color.White else Color.Black
                )
            )
        }
    }, headlineContent = {
        Column(
            modifier = Modifier
                .padding(5.dp)
                .border(
                    BorderStroke(2.dp, if (isSystemInDarkTheme()) Color.White else Color.Gray),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Date: ")
                    }
                    append(formattedDate)
                }
            )

            Text(buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Completed: ")

                }
                append("${historyDTO.completed}/${historyDTO.total}")

            })
        }
    })

}