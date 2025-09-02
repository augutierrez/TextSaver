package com.example.mylearningproject.saver

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylearningproject.SavedTextApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TextDescriptionViewModel(stateHandle: SavedStateHandle) : ViewModel() {
    private var savedTextDao = SavedTextDb.getDaoInstance(SavedTextApplication.getAppContext())
    val state = mutableStateOf<SavedText?>(null)

    init {
        val id = stateHandle.get<Int>("text_id") ?: 0
        viewModelScope.launch {
            val savedText = getTextDescription(id)
            state.value = savedText
        }

    }

    private suspend fun getTextDescription(id: Int): SavedText {
        return withContext(Dispatchers.IO) {
            savedTextDao.getById(id)
        }
    }
}
