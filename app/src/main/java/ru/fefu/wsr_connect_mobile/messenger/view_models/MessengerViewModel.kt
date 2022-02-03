package ru.fefu.wsr_connect_mobile.messenger.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.fefu.wsr_connect_mobile.remote.ApiService
import ru.fefu.wsr_connect_mobile.remote.Result
import ru.fefu.wsr_connect_mobile.remote.models.Chat

class MessengerViewModel : ViewModel() {

    private val apiService = ApiService()

    private val _chats = MutableSharedFlow<List<Chat>>(replay = 0)
    private val _searchResult = MutableSharedFlow<List<Chat>>(replay = 0)
    private val _success = MutableSharedFlow<Boolean>(replay = 0)
    private val _showLoading = MutableStateFlow(false)

    val showLoading get() = _showLoading
    val chats get() = _chats
    val searchResult get() = _searchResult
    val success get() = _success

    fun getChats() {
        viewModelScope.launch {

            apiService.getChats(null)
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _chats.emit(it.result.chats)
                        }
                        is Result.Error -> {}
                    }
                }
        }
    }

    fun searchChat(search: String) {
        viewModelScope.launch {

            apiService.getChats(search)
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _searchResult.emit(it.result.chats)
                        }
                        is Result.Error -> {}
                    }
                }
        }
    }

    fun muteChat(chatId: Int, status: Boolean) {
        viewModelScope.launch {

            apiService.muteChat(chatId, status)
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _success.emit(true)
                        }
                        is Result.Error -> {}
                    }
                }
        }
    }

    fun pinChat(chatId: Int, status: Boolean) {
        viewModelScope.launch {

            apiService.pinChat(chatId, status)
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _success.emit(true)
                        }
                        is Result.Error -> {}
                    }
                }
        }
    }
}