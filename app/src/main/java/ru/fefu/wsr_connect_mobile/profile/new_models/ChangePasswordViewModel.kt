package ru.fefu.wsr_connect_mobile.profile.new_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.fefu.wsr_connect_mobile.remote.ApiService
import ru.fefu.wsr_connect_mobile.remote.Result

class ChangePasswordViewModel : ViewModel() {

    private val apiService = ApiService()

    private val _showCurrentPasswordError = MutableSharedFlow<String>(replay = 0)
    private val _showNewPasswordError = MutableSharedFlow<String>(replay = 0)
    private val _showNewPasswordConfirmError = MutableSharedFlow<String>(replay = 0)
    private val _success = MutableStateFlow(false)
    private val _showLoading = MutableStateFlow(false)

    val showCurrentPasswordError get() = _showCurrentPasswordError
    val showNewPasswordError get() = _showNewPasswordError
    val showNewPasswordConfirmError get() = _showNewPasswordConfirmError
    val success get() = _success
    val showLoading get() = _showLoading

    fun changePassword(currentPassword: String, newPassword: String, newPasswordConfirm: String) {
        viewModelScope.launch {

            if (currentPassword.isBlank()) {
                _showCurrentPasswordError.emit("field is blank")
                return@launch
            }
            if (newPassword.isBlank()) {
                _showNewPasswordError.emit("field is blank")
                return@launch
            }
            if (newPasswordConfirm != newPassword) {
                _showNewPasswordConfirmError.emit("password is not the same")
                return@launch
            }

            apiService.changePassword(currentPassword, newPassword)
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _success.emit(true)
                        }
                        is Result.Error -> {
                            _success.emit(false)
                        }
                    }
                }
        }
    }
}