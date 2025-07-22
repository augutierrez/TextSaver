package com.example.mylearningproject.saver

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TextDetails(){
    val vm = viewModel<TextDescriptionViewModel>()
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        vm.state.value?.let {
            Text(it.description)
        }
    }
}
