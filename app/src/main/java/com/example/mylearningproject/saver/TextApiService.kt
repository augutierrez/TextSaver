package com.example.mylearningproject.saver

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface TextApiService {
    @GET("myText.json")
    suspend fun getSavedText(): List<SavedText>

    @GET("myText.json?orderBy=\"id\"")
    suspend fun getSavedText(@Query("equalTo") id: Int): Map<String,SavedText>
}

data class SavedText(
    @SerializedName("id") // not really necessary since they match
    val id: Int,
    @SerializedName("text")
    val text: String,
    val description: String
)

@Stable
class TextSaverState() {
    init {
        Log.i("Antonio", "Saver state created")
    }

    private var _currentText by mutableStateOf("")
    val currentText get() = _currentText
    val onTextChange = { newText: String -> _currentText = newText }
    val isSynced = false

    private var _savedText by mutableStateOf("")
    val savedText get() = _savedText
    val onSave = { string: String -> _savedText = string }
    // Create a custom Saver for SaverState
}
