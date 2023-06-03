package com.gis2alk.automashup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.gis2alk.automashup.pages.HomePage
import com.gis2alk.automashup.services.RoomDBHelper
import com.gis2alk.automashup.ui.theme.AutoMashupTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dbHelper =
            Room.databaseBuilder(applicationContext, RoomDBHelper::class.java, "history").build()
        setContent {
            AutoMashupTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomePage(dbHelper = dbHelper)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AutoMashupTheme {
//        HomePage(dbHelper)
    }
}
