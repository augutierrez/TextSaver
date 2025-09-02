package com.example.mylearningproject.saver

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mylearningproject.SavedTextApplication
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.net.UnknownHostException

class TextViewModel : ViewModel() {

    private var restInterface: TextApiService

    private var savedTextDao = SavedTextDb.getDaoInstance(SavedTextApplication.getAppContext())

    private val _listOfSavedText = MutableStateFlow(listOf(SavedText(0, "", "", false)))
    val listOfSavedText = _listOfSavedText.asStateFlow()
    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
    }

    init {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://textsaver-b21d8-default-rtdb.firebaseio.com/")
            .build()
        restInterface = retrofit.create(TextApiService::class.java)

        getSavedText()
    }


    private suspend fun getAllSavedText(): List<SavedText> {
        return withContext(Dispatchers.IO) {
            try {
                refreshCache()
            } catch (e: Exception) {
                when (e) {
                    is UnknownHostException,
                    is ConnectException,
                    is HttpException -> {
                        if (savedTextDao.getAll().isEmpty()) {
                            throw Exception("Something went wrong. We have no data.")
                        }
                    }

                    else -> throw e
                }
            }
            return@withContext savedTextDao.getAll()

        }
    }

    private suspend fun refreshCache() {
        val remoteTexts = restInterface.getSavedText()
        val pinnedTexts = savedTextDao.getAllPinned()
        runCatching {
            savedTextDao.addAll(remoteTexts)
            savedTextDao.updateAll(
                pinnedTexts.map { PartialSavedText(it.id, true) }
            )
        }.onFailure { Log.i("Antonio", "Room cache failed") }
    }

    fun onPinned(id: Int, isPinned: Boolean) {
        val texts = _listOfSavedText.value.toMutableList()
        val itemIndex = texts.indexOfFirst { it.id == id }
        val item = texts[itemIndex]
        texts[itemIndex] = item.copy(isPinned = isPinned)
        viewModelScope.launch(errorHandler) {
            val updatedTexts = onPinnedText(id, isPinned)
            _listOfSavedText.value = updatedTexts
        }
    }

    private suspend fun onPinnedText(id: Int, isPinned: Boolean) =
        withContext(Dispatchers.IO) {
            savedTextDao.update(PartialSavedText(id, isPinned))
            savedTextDao.getAll()
        }


    private fun getSavedText() {
        viewModelScope.launch(errorHandler) {
            val textList: List<SavedText> = getAllSavedText()
            _listOfSavedText.value = textList
        }
    }
}
