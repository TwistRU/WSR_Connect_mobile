package ru.fefu.wsr_connect_mobile.messenger.view_models

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.fefu.wsr_connect_mobile.remote.ApiService
import ru.fefu.wsr_connect_mobile.remote.Result
import ru.fefu.wsr_connect_mobile.remote.models.ChatInfoResponseModel
import ru.fefu.wsr_connect_mobile.remote.models.Message
import java.io.ByteArrayOutputStream
import java.util.*

class InChatViewModel : ViewModel() {

    private val apiService = ApiService()

    private val _showLoading = MutableStateFlow(false)
    private val _messages = MutableSharedFlow<List<Message>>(replay = 0)
    private val _chatInfo = MutableSharedFlow<ChatInfoResponseModel>(replay = 0)
    private val _successSend = MutableSharedFlow<Boolean>(replay = 0)
    private val _successEdit = MutableSharedFlow<Boolean>(replay = 0)

    val showLoading get() = _showLoading
    val successSend get() = _successSend
    val successEdit get() = _successEdit
    val messages get() = _messages
    val chatInfo get() = _chatInfo

    fun sendMessage(chatId: Int, messageBody: String) {
        viewModelScope.launch {

            apiService.sendMessage(chatId, null, messageBody)
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _successSend.emit(true)
                        }
                        is Result.Error -> {
                            _successSend.emit(false)
                        }
                    }
                }
        }
    }

    fun replyMessage(chatId: Int, repliedMessageId: Int, messageBody: String) {
        viewModelScope.launch {

            apiService.sendMessage(chatId, repliedMessageId, messageBody)
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _successSend.emit(true)
                        }
                        is Result.Error -> {
                            _successSend.emit(false)
                        }
                    }
                }
        }
    }

    fun editMessage(chatId: Int, messageId: Int, messageBody: String) {
        viewModelScope.launch {

            apiService.editMessage(chatId, messageId, messageBody)
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _successEdit.emit(true)
                        }
                        is Result.Error -> {
                            _successEdit.emit(false)
                        }
                    }
                }
        }
    }

    fun sendImage(image: Bitmap, chatId: Int) {
        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val body = MultipartBody.Part.createFormData(
            "file", "${Calendar.getInstance().time}",
            byteArray.toRequestBody("image/*".toMediaTypeOrNull(), 0, byteArray.size)
        )
        viewModelScope.launch {
            apiService.sendImageMessage(body, chatId)
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _successSend.emit(true)
                        }
                        is Result.Error -> {
                            _successSend.emit(false)
                        }
                    }
                }
        }
    }

    fun getMessages(chatId: Int) {
        viewModelScope.launch {

            apiService.getMessages(chatId)
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _messages.emit(it.result.messages)
                        }
                        is Result.Error -> {}
                    }
                }
        }
    }

    fun getChatInfo(chatId: Int) {
        viewModelScope.launch {

            apiService.getChatInfo(chatId)
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _chatInfo.emit(it.result)
                        }
                        is Result.Error -> {}
                    }
                }
        }
    }
}