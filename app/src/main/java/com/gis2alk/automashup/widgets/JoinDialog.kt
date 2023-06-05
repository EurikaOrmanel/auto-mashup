package com.gis2alk.automashup.widgets

import android.content.Intent
import android.net.Uri
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.gis2alk.automashup.viewmodel.JoinUsPref
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinDialog(joinUsPref: JoinUsPref, onClickEvent: (userClickedJoined: Boolean) -> Unit) {
    val context = LocalContext.current

    AlertDialog(
        modifier = Modifier
            .background(
                if (isSystemInDarkTheme()) Color.DarkGray else Color.White,
                RoundedCornerShape(10.dp)
            )
            .padding(10.dp),
        onDismissRequest = {
            onClickEvent.invoke(false)
        }) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "Join Gis2alk",
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                "Please take a min of your time to join us on telegram (Gis2alk)"
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    modifier = Modifier.width(IntrinsicSize.Max),
                    shape = RoundedCornerShape(4.dp),
                    onClick = {
                        onClickEvent.invoke(false)
                    }) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(10.dp))
                FilledTonalButton(
                    modifier = Modifier.width(IntrinsicSize.Max),
                    shape = RoundedCornerShape(4.dp),
                    onClick = {

                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("https://t.me/gis2alk")
                        }
                        onClickEvent.invoke(true)
                        joinUsPref.markasJoined()
                        context.startActivity(intent)

                    }
                ) {
                    Text("Yes,sure", color = Color.Green)
                }
            }

        }
    }

}

