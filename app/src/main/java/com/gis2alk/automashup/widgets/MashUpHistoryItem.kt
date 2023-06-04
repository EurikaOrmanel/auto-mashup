package com.gis2alk.automashup.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.gis2alk.automashup.models.MashUpHistoryDTO
import java.text.SimpleDateFormat

@Composable
fun MashUpHistoryItem(historyDTO: MashUpHistoryDTO) {
    val formatter = SimpleDateFormat("c dd MM HH:mm a")
    val formattedDate = formatter.format(historyDTO.timestamp)
    Row(
        modifier = Modifier
            .padding(5.dp)
            .border(
                BorderStroke(2.dp, if (isSystemInDarkTheme()) Color.White else Color.Gray),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(10.dp)

            .fillMaxWidth()
    ) {
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Date:")
                }
                append(formattedDate)

            }
        )
        Text(buildAnnotatedString {
            append("  ${historyDTO.completed}/${historyDTO.total}")
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                Text("Completed")
            }
        })
    }
}