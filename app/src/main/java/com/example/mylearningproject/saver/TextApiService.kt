package com.example.mylearningproject.saver

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface TextApiService {
    @GET("myText.json")
    suspend fun getSavedText(): List<SavedText>
}

@Entity(tableName = "texts")
data class SavedText(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id") // not really necessary since they match
    val id: Int,
    @ColumnInfo(name = "text")
    @SerializedName("text")
    val text: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "is_pinned")
    val isPinned: Boolean = false,
)
