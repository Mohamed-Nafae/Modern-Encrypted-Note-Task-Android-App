package com.bm.encryptednoteapp.presentation

import android.content.Context.MODE_PRIVATE
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.core.content.edit

@Composable
fun FirstLaunchScreen(navController: NavController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("Welcome to Encrypted Notes")

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val prefs = context.getSharedPreferences("app_prefs", MODE_PRIVATE)
                prefs.edit { putBoolean("first_launch", false) }

                navController.navigate("home") {
                    popUpTo("first_launch") { inclusive = true }
                }
            }
        ) {
            Text("Start")
        }
    }
}