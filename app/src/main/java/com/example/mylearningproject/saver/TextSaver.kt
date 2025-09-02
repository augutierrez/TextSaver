package com.example.mylearningproject.saver

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mylearningproject.ui.theme.MyLearningProjectTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun TextSaver(
    saverState: StateFlow<List<SavedText>>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val vmText by saverState.collectAsState()
        var index by remember { mutableIntStateOf(0) }
        val savedText = vmText.getOrNull(index) ?: SavedText(0, "", "", false)
        LaunchedEffect(savedText) {
            Log.i("Antonio", "savedText: ${savedText.text}")
        }
        LazyColumn {
            items(vmText) {
                Text(it.text)
            }
        }
        var currentText by remember { mutableStateOf("") }
        TextField(
            value = savedText.text,
            onValueChange = { newText: String -> currentText = newText }
        )
        val isSynced by remember { derivedStateOf { currentText == savedText.text } }
        Text(
            text = "synced...",
            modifier = Modifier.applyIf(isSynced.not()) {
                graphicsLayer {
                    alpha = 0f
                }
            },
            lineHeight = 100.sp,
            textAlign = TextAlign.Center,
        )
//        Button(onClick = { saverState.onSave(currentText) }) {
//            Text("Save me!")
//        }
        //
        Button(onClick = { index += 1 }) {
            Text("Increase index!")
        }
    }

}

@Composable
fun Modifier.applyIf(boolean: Boolean, modification: Modifier.() -> Modifier): Modifier {
    return if (boolean) {
        this then modification()
    } else {
        this
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyLearningProjectTheme {
        TextSaver(MutableStateFlow(listOf(SavedText(0, "hi", "hello", false))))
    }
}
