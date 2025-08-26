package com.example.mylearningproject.saver

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TextDetails() {
    val vm = viewModel<TextDescriptionViewModel>()
    Box(Modifier
        .fillMaxSize()
        .background(Color.Gray), contentAlignment = Alignment.Center) {
        Box(Modifier.padding(20.dp)) {
            vm.state.value?.let {
                Text(it.description,
                    Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.LightGray)
                        .padding(20.dp)
                )
            }
        }
    }
}
