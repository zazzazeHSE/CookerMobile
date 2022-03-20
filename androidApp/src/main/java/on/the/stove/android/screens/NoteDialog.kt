package on.the.stove.android.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import on.the.stove.presentation.recipeNote.RecipeNoteAction
import on.the.stove.presentation.recipeNote.RecipeNoteStore

@Composable
fun NoteDialog(isClosed: MutableState<Boolean>, recipeId: String) {
    val store = remember { RecipeNoteStore(recipeId = recipeId) }
    val state = store.observeState().collectAsState()

    LaunchedEffect(store) {
        store.reduce(RecipeNoteAction.Init)
    }

    if (!isClosed.value) {
        NoteDialogContent(
            isClosed = isClosed,
            content = state.value.content,
            onContentChanged = { content ->
                store.reduce(action = RecipeNoteAction.SetContent(content = content))
            }
        )
    }
}

@Composable
fun NoteDialogContent(
    isClosed: MutableState<Boolean>,
    content: String,
    onContentChanged: (String) -> Unit
) {
    Dialog(
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            securePolicy = SecureFlagPolicy.SecureOff,
        ),
        onDismissRequest = {
            isClosed.value = true
        }, content = {
            Surface(
                border = BorderStroke(1.dp, MaterialTheme.colors.primary),
                shape = RoundedCornerShape(corner = CornerSize(24.dp)),
                contentColor = contentColorFor(backgroundColor)
            ) {
                Column {
                    NoteDialogHeader(onCloseDialog = { isClosed.value = true })
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(MaterialTheme.colors.primary)
                    )
                    NoteDialogInput(content = content, onContentChanged = onContentChanged)
                }
            }
        }
    )
}

@Composable
private fun NoteDialogHeader(onCloseDialog: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            modifier = Modifier.weight(1f),
            text = "Заметка",
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        IconButton(modifier = Modifier, onClick = onCloseDialog) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = "Close",
                tint = MaterialTheme.colors.primary,
            )
        }
    }
}

@Composable
private fun NoteDialogInput(content: String, onContentChanged: (String) -> Unit) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        value = content,
        onValueChange = onContentChanged,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
        ),
        placeholder = { Text("Введите текст, который хотите сохранить...") }
    )
}
