package ru.fefu.wsr_connect_mobile.messenger.new_models

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
    private val _showLoading = MutableStateFlow(false)

    val showLoading get() = _showLoading
    val chats get() = _chats

    fun getChats() {
        viewModelScope.launch {

            apiService.getChats()
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
}