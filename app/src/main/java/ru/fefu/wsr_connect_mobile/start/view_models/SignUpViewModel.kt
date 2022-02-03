package ru.fefu.wsr_connect_mobile.start.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.fefu.wsr_connect_mobile.common.App
import ru.fefu.wsr_connect_mobile.remote.ApiService
import ru.fefu.wsr_connect_mobile.remote.Result

class SignUpViewModel : ViewModel() {

    private val apiService = ApiService()

    private val _showUsernameError = MutableSharedFlow<String>(replay = 0)
    private val _showFirstNameError = MutableSharedFlow<String>(replay = 0)
    private val _showLastNameError = MutableSharedFlow<String>(replay = 0)
    private val _showEmailError = MutableSharedFlow<String>(replay = 0)
    private val _showPasswordError = MutableSharedFlow<String>(replay = 0)
    private val _showPasswordConfirmError = MutableSharedFlow<String>(replay = 0)
    private val _showLoading = MutableStateFlow(false)
    private val _result = MutableSharedFlow<Boolean>(replay = 0)

    val showUsernameError get() = _showUsernameError
    val showFirstNameError get() = _showFirstNameError
    val showLastNameError get() = _showLastNameError
    val showEmailError get() = _showEmailError
    val showPasswordError get() = _showPasswordError
    val showPasswordConfirmError get() = _showPasswordConfirmError
    val showLoading get() = _showLoading
    val result get() = _result

    fun signUpClicked(
        username: String, firstName: String,
        lastName: String, email: String,
        password: String, passwordConfirm: String
    ) {
        viewModelScope.launch {
            if (username.isBlank()) {
                _showUsernameError.emit("username is blank")
                return@launch
            }
            if (firstName.isBlank()) {
                _showFirstNameError.emit("first name is blank")
                return@launch
            }
            if (lastName.isBlank()) {
                _showLastNameError.emit("last name is blank")
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
            apiService.registration(username, firstName, lastName, email, password)
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _result.emit(true)
                            App.sharedPreferences.edit().putString("token", it.result.token).apply()
                            App.sharedPreferences.edit().putInt("my_id", it.result.myId).apply()
                        }
                        is Result.Error -> {}
                    }
                }
        }
    }
}