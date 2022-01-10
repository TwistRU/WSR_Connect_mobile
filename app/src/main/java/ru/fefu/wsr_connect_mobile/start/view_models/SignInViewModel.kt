package ru.fefu.wsr_connect_mobile.start.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.fefu.wsr_connect_mobile.App
import ru.fefu.wsr_connect_mobile.remote.ApiService
import ru.fefu.wsr_connect_mobile.remote.Result

class SignInViewModel : ViewModel() {

    private val apiService = ApiService()

    private val _showUsernameError = MutableSharedFlow<String>(replay = 0)
    private val _showPasswordError = MutableSharedFlow<String>(replay = 0)
    private val _showLoading = MutableStateFlow(false)
    private val _result = MutableStateFlow("")

    val showUsernameError get() = _showUsernameError
    val showPasswordError get() = _showPasswordError
    val showLoading get() = _showLoading
    val result get() = _result

    fun signInClicked(username: String, password: String) {
        viewModelScope.launch {
            if (username.isBlank()) {
                _showUsernameError.emit("username is blank")
                return@launch
            }
            if (password.isBlank()) {
                _showPasswordError.emit("password is blank")
                return@launch
            }
            apiService.login(username, password)
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            App.sharedPreferences.edit().putString("token", it.result.token).apply()
                        }
                        is Result.Error -> {}
                    }
                }
        }
    }
}