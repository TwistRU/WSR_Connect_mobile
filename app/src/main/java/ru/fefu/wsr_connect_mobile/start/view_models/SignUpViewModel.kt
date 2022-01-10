package ru.fefu.wsr_connect_mobile.start.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.fefu.wsr_connect_mobile.App
import ru.fefu.wsr_connect_mobile.remote.ApiService
import ru.fefu.wsr_connect_mobile.remote.Result

class SignUpViewModel : ViewModel() {

    private val apiService = ApiService()

    private val _showUsernameError = MutableSharedFlow<String>(replay = 0)
    private val _showEmailError = MutableSharedFlow<String>(replay = 0)
    private val _showPasswordError = MutableSharedFlow<String>(replay = 0)
    private val _showPasswordConfirmError = MutableSharedFlow<String>(replay = 0)
    private val _showLoading = MutableStateFlow(false)
    private val _result = MutableStateFlow("")

    val showUsernameError get() = _showUsernameError
    val showEmailError get() = _showEmailError
    val showPasswordError get() = _showPasswordError
    val showPasswordConfirmError get() = _showPasswordConfirmError
    val showLoading get() = _showLoading

    fun signUpClicked(username: String, email: String, password: String, passwordConfirm: String) {
        viewModelScope.launch {
            if (username.isBlank()) {
                _showUsernameError.emit("username is blank")
                return@launch
            }
            if (email.isBlank()) {
                _showEmailError.emit("email is blank")
                return@launch
            }
            if (password.isBlank()) {
                _showPasswordError.emit("password is blank")
                return@launch
            }
            if (passwordConfirm.isBlank() || password != passwordConfirm) {
                _showPasswordConfirmError.emit("password is not the same")
                return@launch
            }
            apiService.registration(username, email, password)
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