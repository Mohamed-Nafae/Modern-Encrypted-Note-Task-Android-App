package com.bm.encryptednoteapp.presentation.editaddnote

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.bm.encryptednoteapp.domian.models.Note
import com.bm.encryptednoteapp.domian.models.NoteTag
import com.bm.encryptednoteapp.presentation.editaddnote.components.Footer
import com.bm.encryptednoteapp.ui.theme.colorAccent
import com.bm.encryptednoteapp.ui.theme.colorNoteColor3
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EditAddNoteScreen(
    viewModel: EditAddNotesViewModel,
    onBackClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    val note = viewModel.state.value.noteWithTasks.note
    val tasks = viewModel.state.value.noteWithTasks.tasks
    val snackBarHostState = remember { SnackbarHostState() }

    var isTagMenuExpanded by remember { mutableStateOf(false) }
    var taskText by remember { mutableStateOf(Pair<String, Int>("", -1)) }
    var isNewTask by remember { mutableStateOf(false) }


    val sdf = remember { SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault()) }
    val formattedDate = remember(note.createdAt) {
        sdf.format(Date(note.createdAt))
    }

    val context = LocalContext.current

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                viewModel.onEvent(
                    EditAddNotesEvents.OnImageSelected(
                        uri = it,
                        contentResolver = context.contentResolver,
                        internalDir = context.filesDir // App's internal storage
                    )
                )
            }
        }
    )

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is EditAddNotesViewModel.UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        message = event.message
                    )
                }

                is EditAddNotesViewModel.UiEvent.SaveNote -> {
                    onBackClick()
                }

            }
        }
    }

    BackHandler {
        viewModel.leaveDialogVisible.value = true
    }


    if (viewModel.leaveDialogVisible.value) {
        AlertDialog(
            onDismissRequest = {
                viewModel.leaveDialogVisible.value = false
            },
            title = {
                Text(text = "Unsaved Changes", fontWeight = FontWeight.Bold)
            },
            text = {
                Text(text = "You have unsaved changes. Do you want to save them before leaving or discard them?")
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.leaveDialogVisible.value = false
                    viewModel.onEvent(EditAddNotesEvents.OnClearAllConfirm)
                    onBackClick()
                }) {
                    Text("Discard", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.leaveDialogVisible.value = false
                    viewModel.onEvent(EditAddNotesEvents.SaveNote)
                }) {
                    Text("Save")
                }
            })
    }

    if (viewModel.isClearAllDialogVisible.value) {
        AlertDialog(
            onDismissRequest = { viewModel.isClearAllDialogVisible.value = false },
            title = {
                Text(text = "Clear All Content", fontWeight = FontWeight.Bold)
            },
            text = {
                Text(text = "Are you sure you want to clear all the content in this note?")
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onEvent(EditAddNotesEvents.OnClearAllConfirm)
                }) {
                    Text("Clear", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.isClearAllDialogVisible.value = false }) {
                    Text("Cancel")
                }
            })
    }

    if (viewModel.saveDialogVisible.value) {
        AlertDialog(
            onDismissRequest = { viewModel.saveDialogVisible.value = false },
            title = {
                Text(text = "Save Note", fontWeight = FontWeight.Bold)
            },
            text = {
                Text(text = "Are you sure you want to save this note?")
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onEvent(EditAddNotesEvents.SaveNote)
                }) {
                    Text("Save", color = Color.Green)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.saveDialogVisible.value = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (viewModel.isTaskDialogVisible.value) {
        AlertDialog(onDismissRequest = {
            viewModel.isTaskDialogVisible.value = false
            isNewTask = false
            taskText = Pair("", -1)
        }, title = {
            Text(
                text = if (isNewTask) "Add New Task" else "Edit Task",
                fontWeight = FontWeight.Bold
            )
        }, text = {
            OutlinedTextField(
                value = taskText.first,
                onValueChange = { taskText = taskText.copy(first = it) },
                placeholder = { Text("Task description") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }, confirmButton = {
            TextButton(onClick = {
                if (isNewTask) {
                    viewModel.onEvent(EditAddNotesEvents.AddTask(taskText.first))
                    taskText = Pair("", -1)
                } else {
                    if (taskText.second != -1)
                        viewModel.onEvent(
                            EditAddNotesEvents.UpdateTask(
                                taskText.second,
                                taskText.first
                            )
                        )
                    if (taskText.first.isNotBlank())
                        taskText = Pair("", -1)
                }
            }) {
                Text(if (isNewTask) "Add" else "update")
            }
        }, dismissButton = {
            TextButton(onClick = {
                viewModel.isTaskDialogVisible.value = false
                isNewTask = false
                taskText = Pair("", -1)
            }) {
                Text("Cancel")
            }
        })
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    viewModel.leaveDialogVisible.value = true
                }) {
                    Icon(
                        Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }, bottomBar = {
            Footer(
                color = MaterialTheme.colorScheme.background,
                textColor = MaterialTheme.colorScheme.onSurfaceVariant,
                id = Note.noteColors.indexOf(Color(note.color)),
                addImage = {
                    focusManager.clearFocus()
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                addTask = {
                    viewModel.isTaskDialogVisible.value = true
                    isNewTask = true
                    focusManager.clearFocus()
                },
                clearAll = {
                    viewModel.isClearAllDialogVisible.value = true
                    focusManager.clearFocus()
                },
                save = {
                    viewModel.saveDialogVisible.value = true
                    focusManager.clearFocus()
                },
                pickColor = {
                    viewModel.onEvent(EditAddNotesEvents.ChangeColor(it.toArgb()))
                    focusManager.clearFocus()
                })
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 25.dp, start = 15.dp, end = 10.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                ///3. Display the androidx.compose.foundation.Image if it exists
                note.imagePath?.let { path ->
                    AsyncImage(
                        model = path, // Coil uses 'model' for the data source
                        contentDescription = "Note Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )

                    // Optional: Add a delete button to remove the image from the note
                    IconButton(
                        onClick = {
                            viewModel.onEvent(EditAddNotesEvents.OnImageDelete(note.imagePath))
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete Image",
                            tint = colorNoteColor3
                        )
                    }


                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            // Title TextField
            BasicTextField(
                value = note.title,
                onValueChange = { viewModel.onEvent(EditAddNotesEvents.EnteredTitle(it)) },
                textStyle = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    if (note.title.isEmpty()) {
                        Text(
                            text = "Note Title",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                            )
                        )
                    }
                    innerTextField()
                })

            // Creation Date
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Subtitle / Description TextField
            Row(
                modifier = Modifier
                    .padding(top = 15.dp, bottom = 5.dp)
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min), verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .width(5.dp)
                        .fillMaxHeight()
                        .background(color = colorAccent, shape = RoundedCornerShape(50))
                )
                Spacer(Modifier.width(10.dp))
                BasicTextField(
                    value = note.subtitle,
                    onValueChange = { viewModel.onEvent(EditAddNotesEvents.EnteredSubtitle(it)) },
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        if (note.subtitle.isEmpty()) {
                            Text(
                                text = "Subtitle", style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                                )
                            )
                        }
                        innerTextField()
                    })
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Content TextField
            BasicTextField(
                value = note.content,
                onValueChange = { viewModel.onEvent(EditAddNotesEvents.EnteredContent(it)) },
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp),
                decorationBox = { innerTextField ->
                    if (note.content.isEmpty()) {
                        Text(
                            text = "Start typing your note here...",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                            )
                        )
                    }
                    innerTextField()
                })

            // Tag Dropdown Menu
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        isTagMenuExpanded = !isTagMenuExpanded
                        focusManager.clearFocus()
                    }
                    .clip(RoundedCornerShape(2.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Default.Tag, // Optional: Adding a small tag icon looks great
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.6f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = note.tag.displayName,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.weight(1f))
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Tag")
                }
                DropdownMenu(
                    modifier = Modifier
                        .fillMaxWidth(0.93f)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    expanded = isTagMenuExpanded,
                    onDismissRequest = { isTagMenuExpanded = false },
                    offset = DpOffset((-10).dp, 6.dp),
                    shadowElevation = 0.dp,
                    tonalElevation = 0.dp
                ) {
                    NoteTag.entries.forEach { tag ->
                        DropdownMenuItem(text = {
                            Text(
                                tag.displayName,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }, onClick = {
                            viewModel.onEvent(EditAddNotesEvents.ChangeTag(tag))
                            isTagMenuExpanded = false
                        })
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            tasks.forEach {
                TaskRowEdit(
                    modifier = Modifier.padding(vertical = 5.dp),
                    task = it.description,
                    isDone = it.isDone,
                    doneColor = colorAccent,
                    onCheckedChange = {
                        viewModel.onEvent(
                            EditAddNotesEvents.UpdateTask(
                                it.position,
                                it.description,
                                !it.isDone
                            )
                        )
                    },
                    onEditClick = {
                        viewModel.isTaskDialogVisible.value = true
                        isNewTask = false
                        taskText = Pair(it.description, it.position)
                    },
                    onDeleteClick = {
                        viewModel.onEvent(EditAddNotesEvents.DeleteTask(it.position))
                    }
                )
            }
        }
    }
}

@Composable
fun TaskRowEdit(
    modifier: Modifier = Modifier,
    doneColor: Color,
    task: String,
    isDone: Boolean,
    onCheckedChange: () -> Unit,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onCheckedChange, modifier = Modifier.size(28.dp)) {
            if (isDone) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Completed",
                    tint = doneColor,
                    modifier = Modifier.size(25.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 1.dp)
                        .size(23.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.Transparent)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.secondary,
                            RoundedCornerShape(50)
                        )
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))
        Box(
            Modifier
                .weight(1f)
                .clickable {
                    onEditClick()
                }) {
            Text(
                text = task,
                style = MaterialTheme.typography.bodySmall.copy(
                    textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        IconButton(onClick = {
            onDeleteClick()
        }, modifier = Modifier.size(20.dp)) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.Red.copy(alpha = 0.5f)
            )
        }
    }
}
