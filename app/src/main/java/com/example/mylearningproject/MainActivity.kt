package com.example.mylearningproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylearningproject.saver.TextDetails
import com.example.mylearningproject.saver.TextSaver
import com.example.mylearningproject.saver.TextViewModel
import com.example.mylearningproject.ui.theme.MyLearningProjectTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyLearningProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val vm = viewModel<TextViewModel>()
//                    TextSaver(vm.listOfSavedText,modifier = Modifier.padding(innerPadding))
                    TextDetails()
                }
            }
        }
    }
}
