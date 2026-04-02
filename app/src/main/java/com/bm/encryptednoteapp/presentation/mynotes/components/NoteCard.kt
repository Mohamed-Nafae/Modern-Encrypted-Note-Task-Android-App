package com.bm.encryptednoteapp.presentation.mynotes.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.bm.encryptednoteapp.ui.theme.EncriptedNotesTheme
import com.bm.encryptednoteapp.ui.theme.colorAccent
import com.bm.encryptednoteapp.ui.theme.colorNoteColor3
import com.bm.encryptednoteapp.ui.theme.colorPrimaryDark
import positionAsFooter


@Composable
fun NoteCard(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    doneColor: Color = colorAccent,
    tag: String,
    title: String,
    subtitle: String = "",
    description: String,
    creatingDate: String,
    height: Dp,
    tasks: List<Pair<String, Boolean>> = emptyList(),
    imagePath: String? = null,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val titleColor = when (containerColor) {
        colorPrimaryDark -> MaterialTheme.colorScheme.primary
        Color.Blue, Color.Red -> Color.White
        Color.Green, Color.Yellow -> colorPrimaryDark
        else -> MaterialTheme.colorScheme.primary
    }

    val contentColor = when (containerColor) {
        colorPrimaryDark -> MaterialTheme.colorScheme.onSurfaceVariant.copy(0.8f)
        Color.Blue, Color.Red -> Color.White.copy(alpha = 0.8f)
        Color.Green, Color.Yellow -> colorPrimaryDark.copy(alpha = 0.7f)
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    ElevatedCard(
        onClick = onClick,
        modifier = if (isSelected) {
            modifier
                .padding(6.dp)
                .fillMaxWidth()
                .requiredHeight(height)
                .border(2.dp, colorAccent, RoundedCornerShape(16.dp))
        } else {
            modifier
                .padding(6.dp)
                .fillMaxWidth()
                .requiredHeight(height)
        },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (containerColor == colorPrimaryDark) MaterialTheme.colorScheme.surfaceVariant else containerColor,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            imagePath?.let { path ->
                AsyncImage(
                    model = path, // Coil uses 'model' for the data source
                    contentDescription = "Note Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = if (tasks.isEmpty()) height / 3 else height / 4)
                        .aspectRatio(16f / 9f),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            Column(
                modifier = Modifier
                    .padding(top = if(imagePath== null) 16.dp else 7.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .fillMaxWidth()
            ) {
                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = titleColor,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if(subtitle.isNotBlank() && imagePath == null) {
                    Text(
                        modifier = Modifier.padding(start = 15.dp),
                        text = description,
                        style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                        color = contentColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Description (optional)
                if (description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = contentColor,
                        maxLines = if (tasks.isNotEmpty() || imagePath != null) 3 else 7,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Task list (if it's a note)
                if (tasks.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = if (description.isNotBlank() || imagePath != null) height / 4 else height / 2)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        tasks.forEach { (task, done) ->
                            TaskRow(
                                task = task,
                                isDone = done,
                                doneColor = doneColor,
                                contentColor = contentColor
                            )
                        }
                    }
                }

                NoteCardFooter(tag = tag, date = creatingDate, contentColor = contentColor)
            }

        }
    }
}

@Composable
fun TaskRow(
    modifier: Modifier = Modifier,
    doneColor: Color,
    contentColor: Color,
    task: String,
    isDone: Boolean = false
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isDone) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Completed",
                tint = doneColor,
                modifier = Modifier.size(18.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .padding(horizontal = 1.dp)
                    .size(16.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.Transparent)
                    .border(1.dp, contentColor, RoundedCornerShape(50))
            )
        }

        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = task,
            style = MaterialTheme.typography.bodySmall.copy(
                textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None,
                color = contentColor
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun NoteCardFooter(
    tag: String,
    date: String,
    contentColor: Color
) {
    Row(
        modifier = Modifier
            .positionAsFooter()
            .fillMaxWidth()
            .heightIn(min=30.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedCard(
            shape = RoundedCornerShape(25),
            colors = CardDefaults.outlinedCardColors(
                containerColor = Color.Transparent,
                contentColor = contentColor
            ),
            border = BorderStroke(1.dp, contentColor.copy(alpha = 0.5f)),
            modifier = Modifier.heightIn(min = 20.dp)
        ) {
            Text(
                text = tag,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }

        Text(
            text = date,
            style = MaterialTheme.typography.labelSmall,
            color = contentColor
        )
    }
}

@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = false,
    showBackground = true
)
@Preview(
    name = "Light Mode",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi = false,
    showBackground = true
)
@Composable
fun NoteCardPreview() {
    EncriptedNotesTheme {
        Surface(Modifier.fillMaxWidth()) {
            NoteCard(
                title = "Today Tasks",
                subtitle = "Quick tasks to get done ",
                description = "Quick tasks to get done",
                creatingDate = "Sun, 2 Apr",
                tasks = listOf(
                    "Buy Eggs" to true,
                    "Go to Library" to false,
                    "Doing Exercise" to false,
                    "dkjfj" to true,
                    "jdfkljkl" to false
                ),
                onClick = {},
                height = 300.dp,
                tag = "Notes"
            )
        }
    }
}
