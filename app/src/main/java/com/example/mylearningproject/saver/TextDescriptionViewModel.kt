package com.example.mylearningproject.saver

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TextDescriptionViewModel() : ViewModel() {
    private var restInterface: TextApiService
    val state = mutableStateOf<SavedText?>(null)
    init {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://textsaver-b21d8-default-rtdb.firebaseio.com/")
            .build()
        restInterface = retrofit.create(TextApiService::class.java)

        viewModelScope.launch {
            val savedText = getTextDescription(2)
            state.value = savedText
        }

    }

    private suspend fun getTextDescription(id: Int): SavedText {
        return withContext(Dispatchers.IO) {
            val responseMap = restInterface.getSavedText(id)
            return@withContext responseMap.values.first()
        }
    }
}
