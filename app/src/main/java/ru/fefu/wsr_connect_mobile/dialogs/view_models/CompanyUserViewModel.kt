package ru.fefu.wsr_connect_mobile.dialogs.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.fefu.wsr_connect_mobile.remote.ApiService
import ru.fefu.wsr_connect_mobile.remote.Result
import ru.fefu.wsr_connect_mobile.remote.models.User

class CompanyUserViewModel : ViewModel() {

    private val apiService = ApiService()

    private val _user = MutableSharedFlow<User>(replay = 0)
    private val _chat = MutableSharedFlow<Int>(replay = 0)
    private val _success = MutableStateFlow(false)
    private val _showLoading = MutableStateFlow(false)

    val success get() = _success
    val user get() = _user
    val chat get() = _chat
    val showLoading get() = _showLoading

    fun getCompanyUserInfo(userId: Int) {
        viewModelScope.launch {
            apiService.getCompanyUserInfo(userId)
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _user.emit(it.result)
                        }
                        is Result.Error -> {
                        }
                    }
                }
        }
    }

    fun startPrivateChat(userId: Int) {
        viewModelScope.launch {
            apiService.startPrivateChat(userId)
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _chat.emit(it.result)
                        }
                        is Result.Error -> {
                        }
                    }
                }
        }
    }
}