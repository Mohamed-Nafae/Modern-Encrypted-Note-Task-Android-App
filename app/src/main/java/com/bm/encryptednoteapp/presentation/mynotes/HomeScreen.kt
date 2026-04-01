package com.bm.encryptednoteapp.presentation.mynotes

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bm.encryptednoteapp.domian.util.BiometricPromptManager
import com.bm.encryptednoteapp.domian.util.BiometricPromptManager.*
import com.bm.encryptednoteapp.domian.util.NoteOrder
import com.bm.encryptednoteapp.domian.util.OrderType
import com.bm.encryptednoteapp.presentation.mynotes.components.NoteCard
import com.bm.encryptednoteapp.presentation.mynotes.components.SearchBar
import com.bm.encryptednoteapp.ui.theme.colorAccent
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MyNotesViewModel,
    isAuthenticated: Boolean,
    promptManager: BiometricPromptManager,
    onAuthSuccess: () -> Unit
) {

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    val biometricResult by promptManager.promptResult.collectAsState(null)

    val focusManager = LocalFocusManager.current
    val state = viewModel.state.value
    val isSystemDark = isSystemInDarkTheme()
    val isDarkTheme by remember { mutableStateOf(isSystemDark) }
    val sdf = remember { SimpleDateFormat("EEE d, MMM", Locale.getDefault()) }

    val enrollLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            println("Activity result: $it")
        }
    )

    LaunchedEffect(biometricResult) {
        biometricResult?.let {
            when (it) {

                is BiometricResult.AuthenticationError -> {
                    snackBarHostState.showSnackbar("Authentication Error: ${it.error}")
                }

                BiometricResult.AuthenticationFailed -> {
                    snackBarHostState.showSnackbar("Authentication Failed")
                }

                BiometricResult.AuthenticationNotSet -> {
                    onAuthSuccess()
                    state.selectedNote?.let { note ->
                        viewModel.onEvent(HomeScreenEvents.ToggleEncryption(note))
                    }
                    val result = snackBarHostState.showSnackbar(
                        message = "Authentication not set, for security purposes, please: ",
                        actionLabel = "set it",
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed)
                        if (Build.VERSION.SDK_INT >= 30) {
                            val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                                putExtra(
                                    Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                                    BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                                )
                            }
                            enrollLauncher.launch(enrollIntent)
                        }
                }

                BiometricResult.AuthenticationSucceeded -> {
                    onAuthSuccess()
                    state.selectedNote?.let { note ->
                        viewModel.onEvent(HomeScreenEvents.ToggleEncryption(note))
                    }
                    snackBarHostState.showSnackbar("Authentication Succeeded")
                }

                BiometricResult.FeatureUnavailable -> {
                    snackBarHostState.showSnackbar("Feature Unavailable")
                }

                BiometricResult.HardwareUnavailable -> {
                    snackBarHostState.showSnackbar("Hardware Unavailable")
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .offset(-(16.dp), 44.dp)
                    .shadow(7.dp, RoundedCornerShape(50), spotColor = colorAccent),
                shape = RoundedCornerShape(50),
                containerColor = colorAccent,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(20.dp),
                onClick = {
                    navController.navigate("edit_note")
                }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Note",
                    modifier = Modifier.size(40.dp)
                )
            }
        }, bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxHeight(0.09f)
                    .drawWithContent {
                        // First, draw the content of the BottomAppBar itself
                        drawContent()
                        // Then, draw the shadow on top of it
                        drawIntoCanvas { canvas ->
                            val shadowHeight = -40.dp.toPx()
                            val paint = android.graphics.Paint().apply {
                                isAntiAlias = true
                                shader = android.graphics.LinearGradient(
                                    0f,
                                    0f,
                                    0f,
                                    shadowHeight,
                                    if (isDarkTheme) android.graphics.Color.argb(
                                        80, 0, 0, 0
                                    )
                                    else android.graphics.Color.LTGRAY, // Start color (semi-transparent black)
                                    android.graphics.Color.TRANSPARENT,      // End color (fully transparent)
                                    android.graphics.Shader.TileMode.CLAMP
                                )
                            }
                            canvas.nativeCanvas.drawRect(0f, 0f, size.width, shadowHeight, paint)
                        }
                    },
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,

                ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box { // Box is needed to anchor the DropdownMenu to the button
                        IconButton(
                            modifier = Modifier
                                .width(50.dp)
                                .fillMaxHeight(),
                            onClick = { viewModel.onEvent(event = HomeScreenEvents.ToggleOrderSection) } // Open menu
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.Sort,
                                    contentDescription = "Filter Notes",
                                    modifier = Modifier.size(30.dp)
                                )
                                Text(
                                    text = "Order", style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }

                        // This is the Drop-up menu
                        DropdownMenu(
                            expanded = state.isOrderSectionVisible,
                            onDismissRequest = { viewModel.onEvent(event = HomeScreenEvents.ToggleOrderSection) },

                            offset = DpOffset((-12).dp, (-5).dp),
                            modifier = Modifier.background(MaterialTheme.colorScheme.background),
                            tonalElevation = 0.dp,
                            shadowElevation = 0.dp,
                            border = null

                        ) {
                            DropdownMenuItem(text = { Text("Title") }, onClick = {
                                viewModel.onEvent(
                                    HomeScreenEvents.Order(
                                        NoteOrder.Title(
                                            state.noteOrder.orderType
                                        )
                                    )
                                )
                                viewModel.onEvent(event = HomeScreenEvents.ToggleOrderSection)
                            })
                            DropdownMenuItem(text = { Text("Date") }, onClick = {
                                viewModel.onEvent(
                                    HomeScreenEvents.Order(
                                        NoteOrder.Date(
                                            state.noteOrder.orderType
                                        )
                                    )
                                )
                                viewModel.onEvent(event = HomeScreenEvents.ToggleOrderSection)
                            })
                            DropdownMenuItem(text = { Text("Tag") }, onClick = {
                                viewModel.onEvent(
                                    HomeScreenEvents.Order(
                                        NoteOrder.Gender(
                                            state.noteOrder.orderType
                                        )
                                    )
                                )
                                viewModel.onEvent(event = HomeScreenEvents.ToggleOrderSection)
                            })
                            DropdownMenuItem(text = { Text("Color") }, onClick = {
                                viewModel.onEvent(
                                    HomeScreenEvents.Order(
                                        NoteOrder.Color(
                                            state.noteOrder.orderType
                                        )
                                    )
                                )
                                viewModel.onEvent(event = HomeScreenEvents.ToggleOrderSection)
                            })
                            HorizontalDivider()
                            DropdownMenuItem(text = { Text("Ascending") }, onClick = {
                                viewModel.onEvent(
                                    HomeScreenEvents.Order(
                                        state.noteOrder.copy(
                                            OrderType.Ascending
                                        )
                                    )
                                )
                                viewModel.onEvent(event = HomeScreenEvents.ToggleOrderSection)
                            })
                            DropdownMenuItem(text = { Text("Descending") }, onClick = {
                                viewModel.onEvent(
                                    HomeScreenEvents.Order(
                                        state.noteOrder.copy(
                                            OrderType.Descending
                                        )
                                    )
                                )
                                viewModel.onEvent(event = HomeScreenEvents.ToggleOrderSection)
                            })
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    IconButton(
                        onClick = {
                            state.selectedNote?.let {
                                if (it.note.isEncrypted) {
                                    scope.launch {
                                        snackBarHostState.showSnackbar("you can't edit an encrypted note")
                                    }
                                } else {
                                    navController.navigate("edit_note?noteId=${state.selectedNote.note.noteId}")
                                }
                            }
                        },
                        modifier = Modifier
                            .width(30.dp)
                            .fillMaxHeight()
                    ) {
                        Column {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit",
                                modifier = Modifier.size(30.dp)
                            )
                            Text(text = "Edit", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                    Spacer(modifier = Modifier.width(2.dp))
                    IconButton(
                        onClick = {
                            state.selectedNote?.let { note ->
                                if (note.note.isEncrypted && !isAuthenticated) {
                                    promptManager.showBiometricPrompt(
                                        "Authenticate",
                                        "Confirm your identity to change encryption"
                                    )
                                } else {
                                    viewModel.onEvent(HomeScreenEvents.ToggleEncryption(note))
                                }
                            }
                        },
                        modifier = Modifier
                            .width(65.dp)
                            .fillMaxHeight()
                    ) {

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                if (state.selectedNote?.note?.isEncrypted == true)
                                    Icons.Outlined.Lock
                                else
                                    Icons.Outlined.LockOpen,
                                //Icons.Outlined.EnhancedEncryption,
                                contentDescription = "Toggle Encryption",
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (state.selectedNote?.note?.isEncrypted == true)
                                    "Encrypted"
                                else
                                    "Decrypted",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                    IconButton(
                        onClick = {
                            if (state.selectedNote != null) {
                                viewModel.onEvent(
                                    HomeScreenEvents.DeleteNote(
                                        noteId = state.selectedNote.note.noteId
                                    )
                                )
                                scope.launch {
                                    val result = snackBarHostState.showSnackbar(
                                        message = "Note Deleted",
                                        actionLabel = "undo",
                                        duration = SnackbarDuration.Short
                                    )
                                    if (result == SnackbarResult.ActionPerformed)
                                        viewModel.onEvent(HomeScreenEvents.RestoreNote)
                                    else
                                        viewModel.onEvent(HomeScreenEvents.DeleteImage)

                                }
                            }
                        }, modifier = Modifier
                            .width(50.dp)
                            .fillMaxHeight()
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.Red.copy(alpha = 0.7f),
                                modifier = Modifier.size(30.dp)
                            )
                            Text(
                                text = "Delete", color = Color.Red
                                , style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }, containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 16.dp, start = 8.dp, end = 8.dp)
                .clickable(
                    interactionSource = null,
                    indication = null
                ) { focusManager.clearFocus() }

        ) {

            // Header
            Text(
                text = "My Notes",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 16.dp)
            )

            // Search Bar
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 24.dp),
                value = state.searchQuery,
                onvalueChange = {
                    viewModel.onEvent(HomeScreenEvents.SearchNote(it))
                }
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // This creates a 2-column grid
                modifier = Modifier.fillMaxSize()
            ) {

                items(state.notes) { noteWithTasks ->
                    val formattedDate = sdf.format(Date(noteWithTasks.note.createdAt))
                    NoteCard(
                        title = noteWithTasks.note.title,
                        description = noteWithTasks.note.subtitle,
                        creatingDate = formattedDate,
                        tasks = noteWithTasks.tasks.map {
                            it.description to it.isDone
                        },
                        isSelected = state.selectedNote == noteWithTasks,
                        tag = noteWithTasks.note.tag.name,
                        containerColor = Color(noteWithTasks.note.color),
                        onClick = {
                            focusManager.clearFocus()
                            viewModel.onEvent(HomeScreenEvents.SelectNote(noteWithTasks.note.noteId))
                        },
                        height = 300.dp,
                        imagePath = noteWithTasks.note.imagePath
                    )
                }
            }

        }
    }
}
