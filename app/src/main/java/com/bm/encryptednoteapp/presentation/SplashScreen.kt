package com.bm.encryptednoteapp.presentation

import android.content.Context.MODE_PRIVATE
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bm.encryptednoteapp.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController
) {
    val context = LocalContext.current

    LaunchedEffect(true) {
        delay(1000)

        val prefs = context.getSharedPreferences("app_prefs", MODE_PRIVATE)
        val isFirstLaunch = prefs.getBoolean("first_launch", true)

        if (isFirstLaunch) {
            navController.navigate("first_launch") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            navController.navigate("home") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.size(300.dp).offset(0.dp,-(50).dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            "Cipher Note",
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.offset(0.dp, 75.dp)
        )
        Text(
            "Modern Encrypted Note & Task Manager",
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.offset(0.dp, 120.dp)
        )
    }
}
