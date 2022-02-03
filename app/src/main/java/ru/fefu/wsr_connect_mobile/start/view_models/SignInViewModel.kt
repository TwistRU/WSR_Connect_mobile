package ru.fefu.wsr_connect_mobile.start.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.fefu.wsr_connect_mobile.common.App
import ru.fefu.wsr_connect_mobile.remote.ApiService
import ru.fefu.wsr_connect_mobile.remote.Result
import ru.fefu.wsr_connect_mobile.remote.models.User

class SignInViewModel : ViewModel() {

    private val apiService = ApiService()

    private val _showUsernameError = MutableSharedFlow<String>(replay = 0)
    private val _showPasswordError = MutableSharedFlow<String>(replay = 0)
    private val _showLoading = MutableStateFlow(false)
    private val _result = MutableSharedFlow<Boolean>(replay = 0)
    private val _info = MutableSharedFlow<User>(replay = 0)

    val showUsernameError get() = _showUsernameError
    val showPasswordError get() = _showPasswordError
    val showLoading get() = _showLoading
    val result get() = _result
    val info get() = _info


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
                            _result.emit(true)
                            App.sharedPreferences.edit().putString("token", it.result.token).apply()
                            App.sharedPreferences.edit().putInt("my_id", it.result.myId).apply()
                        }
                        is Result.Error -> {}
                    }
                }
        }
    }

    fun getProfileInfo() {
        viewModelScope.launch {
            apiService.getProfileInfo()
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _info.emit(it.result)
                        }
                        is Result.Error -> {}
                    }
                }
        }
    }
}