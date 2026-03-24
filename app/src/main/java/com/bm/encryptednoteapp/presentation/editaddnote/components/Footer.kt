package com.bm.encryptednoteapp.presentation.editaddnote.components

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material.icons.outlined.AddTask
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bm.encryptednoteapp.domian.models.Note
import com.bm.encryptednoteapp.ui.theme.colorSearchIcon

@Composable
fun Footer(
    modifier: Modifier = Modifier,
    id:Int,
    color: Color,
    textColor: Color,
    addImage: () -> Unit = {},
    addTask: () -> Unit = {},
    clearAll: () -> Unit,
    save: () -> Unit = {},
    pickColor: (Color) -> Unit
){
    var selectedId by remember { mutableIntStateOf(0) }
    selectedId = id
    val color2 = MaterialTheme.colorScheme.surfaceVariant
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 18.dp,
                shape = RoundedCornerShape(topEndPercent = 20, topStartPercent = 20),
                clip = false
            )
            .background(
                shape = RoundedCornerShape(topEndPercent = 20, topStartPercent = 20),
                color = color
            )

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = 10.dp, start = 15.dp, end = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ColorShapeSelection(
                color = color2,
                selected = selectedId == 0,
                onclick = {
                    selectedId = 0
                    pickColor(Note.noteColors[0])
                })
            ColorShapeSelection(
                color = Note.noteColors[1],
                selected = selectedId == 1,
                onclick = {
                    selectedId = 1
                    pickColor(Note.noteColors[1])
                })
            ColorShapeSelection(
                color = Note.noteColors[2],
                selected = selectedId == 2,
                onclick = { selectedId = 2
                    pickColor(Note.noteColors[2])
                })
            ColorShapeSelection(
                color = Note.noteColors[3],
                selected = selectedId == 3,
                onclick = {
                    selectedId = 3
                    pickColor(Note.noteColors[3])
                })
            ColorShapeSelection(
                color = Note.noteColors[4],
                selected = selectedId == 4,
                onclick = {
                    selectedId = 4
                    pickColor(Note.noteColors[4])
                })
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier.padding(horizontal = 10.dp),
                text = "Pick Color",
                color = textColor,
                style = MaterialTheme.typography.labelSmall,
                textDecoration = TextDecoration.Underline
            )
        }
        FooterElement(icon = Icons.Outlined.AddAPhoto, text = "Add Image", color = textColor, onClick = addImage)
        FooterElement(icon= Icons.Outlined.AddTask, text= "Add new Task", color= textColor, onClick = addTask )
        FooterElement(icon = Icons.Outlined.Clear, text = "Clear All", color = Color.Red.copy(alpha = 0.6f), onClick = clearAll)
        FooterElement(icon = Icons.Outlined.Save, text = "Save", color = colorSearchIcon, onClick = save)
    }
}

@Composable
fun FooterElement(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    color: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(top = 10.dp, bottom = 10.dp, start = 22.dp, end = 15.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick)
        ,
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = text, tint = color)
        Spacer(modifier = Modifier.width(15.dp))
        Text(text = text, color = color, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun ColorShapeSelection(
    modifier: Modifier = Modifier,
    color: Color,
    selected: Boolean,
    onclick: () -> Unit
) {
    Box(
        modifier
            .padding(horizontal = 10.dp)
            .size(25.dp)
            .border(1.dp, color, shape = CircleShape)
            .padding(3.dp)
    )
    {
        Box(
            Modifier
                .size(25.dp)
                .background(color, shape = CircleShape)
                .align(Alignment.Center)
                .padding(3.dp)
                .clickable(true, onClick = onclick)
        ) {
            if (selected) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "check",
                    tint = MaterialTheme.colorScheme.background
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FooterPreview() {
    Footer(
        color = Color.White,
        textColor = Color.DarkGray,
        addImage = {},
        addTask = {},
        clearAll = {},
        save = {},
        pickColor = {},
        id= 0
    )
}