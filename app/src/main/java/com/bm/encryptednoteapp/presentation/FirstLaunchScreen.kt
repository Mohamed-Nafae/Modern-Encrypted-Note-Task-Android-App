package com.bm.encryptednoteapp.presentation

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Hail
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun FirstLaunchScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val pages = listOf(
        OnboardingPage(
            title = "Welcome to Cipher Note",
            description = "Experience a modern, secure way to manage your thoughts and tasks with industry-standard encryption.",
            icon = Icons.Default.Hail
        ),
        OnboardingPage(
            title = "Selection Mechanism",
            description = "Manage notes with a tap. Selected notes feature a yellow border, allowing you to easily edit, delete, or encrypt them using the quick action bar.",
            icon = Icons.Default.Sort
        ),
        OnboardingPage(
            title = "Biometric Security",
            description = "Your privacy is our priority. Use your fingerprint or face ID to unlock your secure vault and access encrypted notes.",
            icon = Icons.Default.Fingerprint
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // 1. Skip Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = { onFinish(context, navController) }) {
                Text("Skip", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        // 2. Swipable Content
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { index ->
            OnboardingContent(pages[index], isFeaturePage = index == 1)
        }

        // 3. Bottom Controls
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Custom Pager Indicator
            PagerIndicator(pagerState.pageCount, pagerState.currentPage)

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (pagerState.currentPage < pages.size - 1) {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    } else {
                        onFinish(context, navController)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(56.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = if (pagerState.currentPage == pages.size - 1) "Get Started" else "Next",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun OnboardingContent(page: OnboardingPage, isFeaturePage: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isFeaturePage) {
            // Shows a mock-up of the yellow border and action bar
            SelectionFeatureIllustration()
        } else {
            Icon(
                imageVector = page.icon,
                contentDescription = null,
                modifier = Modifier.size(140.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = page.title,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 34.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = page.description,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun SelectionFeatureIllustration() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Mock Note with Yellow Border
        Box(
            modifier = Modifier
                .size(width = 160.dp, height = 100.dp)
                .border(2.dp, Color(0xFFFFD700), RoundedCornerShape(12.dp)) // Yellow Border
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Column {
                Box(modifier = Modifier.fillMaxWidth(0.6f).height(8.dp).background(Color.Gray.copy(0.3f), CircleShape))
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.fillMaxWidth(0.9f).height(8.dp).background(Color.Gray.copy(0.2f), CircleShape))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Mock Bottom Action Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ActionItem(Icons.Default.Sort, "Order")
            ActionItem(Icons.Default.Edit, "Edit")
            ActionItem(Icons.Outlined.Lock, "Encrypt")
            ActionItem(Icons.Default.Delete, "Delete", Color.Red)
        }
    }
}

@Composable
fun ActionItem(icon: ImageVector, label: String, tint: Color = MaterialTheme.colorScheme.primary) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null, tint = tint, modifier = Modifier.size(20.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, color = tint)
    }
}

@Composable
fun PagerIndicator(pageCount: Int, currentPage: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val isSelected = currentPage == index
            val width by animateDpAsState(targetValue = if (isSelected) 24.dp else 8.dp, label = "width")

            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(width)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    )
            )
        }
    }
}

private fun onFinish(context: Context, navController: NavController) {
    val prefs = context.getSharedPreferences("app_prefs", MODE_PRIVATE)
    prefs.edit { putBoolean("first_launch", false) }
    navController.navigate("home") {
        popUpTo("first_launch") { inclusive = true }
    }
}

data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: ImageVector
)