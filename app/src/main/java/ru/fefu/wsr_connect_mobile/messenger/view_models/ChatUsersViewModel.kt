package ru.fefu.wsr_connect_mobile.messenger.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.fefu.wsr_connect_mobile.remote.ApiService
import ru.fefu.wsr_connect_mobile.remote.Result
import ru.fefu.wsr_connect_mobile.remote.models.ChatInfoResponseModel
import ru.fefu.wsr_connect_mobile.remote.models.User

class ChatUsersViewModel : ViewModel() {

    private val apiService = ApiService()

    private val _chatUsers = MutableSharedFlow<ChatInfoResponseModel>(replay = 0)
    private val _success = MutableStateFlow(false)
    private val _showLoading = MutableStateFlow(false)

    val chatUsers get() = _chatUsers
    val success get() = _success
    val showLoading get() = _showLoading

    fun getChatInfo(chatId: Int) {
        viewModelScope.launch {
            apiService.getChatInfo(chatId)
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _chatUsers.emit(it.result)
                        }
                        is Result.Error -> {}
                    }
                }
        }
    }
}