package com.example.mylearningproject.saver

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.SavedStateHandle
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

class TextViewModel(private val stateHandle: SavedStateHandle) : ViewModel() {

    private var restInterface: TextApiService

    private var savedTextDao = SavedTextDb.getDaoInstance(SavedTextApplication.getAppContext())

    private val _listOfSavedText = MutableStateFlow(listOf(SavedText(0, "", "")))
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
                val texts = restInterface.getSavedText()
                savedTextDao.addAll(texts)
                return@withContext texts
            } catch (e: Exception) {
                when (e) {
                    is UnknownHostException,
                    is ConnectException,
                    is HttpException -> {
                        Log.i("Antonio", "returning from dao")
                        return@withContext savedTextDao.getAll()
                    }

                    else -> throw e
                }
            }

        }
    }

    private fun getSavedText() {
        viewModelScope.launch(errorHandler) {
            val text: List<SavedText> = getAllSavedText()
            _listOfSavedText.value = text
        }
//        Log.i("Antonio", "Calling getSavedText")
//        savedTextCall = restInterface.getSavedText()
//        savedTextCall.enqueue(object : Callback<List<SavedText>> {
//            override fun onResponse(
//                call: Call<List<SavedText>>,
//                response: Response<List<SavedText>>
//            ) {
//                Log.i("Antonio", "Successfully receives saved text")
//                response.body()?.let {
//                    Log.i("Antonio", "body: $it")
//                    _listOfSavedText.value = it
//                }
//            }
//
//            override fun onFailure(call: Call<List<SavedText>>, t: Throwable) {
//                Log.e("Antonio", "API call failed", t)
//            }
//        })
    }

    /////////////////////////////////
    val saverState = TextSaverState()

    fun onSaveState(string: String) {
        stateHandle[KEY] = string
        saverState.onSave(string)
    }

    companion object {
        const val KEY = "savedText"
    }
}


@Composable
fun rememberSaverState(): TextSaverState {
    return rememberSaveable(saver = SaverStateSaver) {
        TextSaverState()
    }
}


private val SaverStateSaver = Saver<TextSaverState, String>(
    save = { it.currentText },
    restore = { restoredText ->
        TextSaverState().apply { onTextChange(restoredText) }
    }
)
