package ru.fefu.wsr_connect_mobile.profile.new_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.fefu.wsr_connect_mobile.remote.ApiService
import ru.fefu.wsr_connect_mobile.remote.Result
import ru.fefu.wsr_connect_mobile.remote.models.response.UserResponseModel

class ProfileViewModel : ViewModel() {

    private val apiService = ApiService()

    private val _showLoading = MutableStateFlow(false)
    private val _userInfo = MutableSharedFlow<UserResponseModel>(replay = 0)
    private val _showFirstNameResult = MutableSharedFlow<String>(replay = 0)
    private val _showLastNameResult = MutableSharedFlow<String>(replay = 0)
    private val _showEmailResult = MutableSharedFlow<String>(replay = 0)
    private val _showAboutMeResult = MutableSharedFlow<String>(replay = 0)

    val showLoading get() = _showLoading
    val userInfo get() = _userInfo
    val showFirstNameResult get() = _showFirstNameResult
    val showLastNameResult get() = _showLastNameResult
    val showEmailResult get() = _showEmailResult
    val showAboutMeResult get() = _showAboutMeResult

    fun getInfo() {
        viewModelScope.launch {
            apiService.getUserInfo()
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _userInfo.emit(it.result)
                        }
                        is Result.Error -> {}
                    }
                }
        }
    }

    fun changeFirstName(firstName: String?) {
        viewModelScope.launch {
            if (firstName == null || firstName.isBlank()) {
                _showFirstNameResult.emit("first name is blank")
                return@launch
            }
            apiService.changeUserInfo(firstName, null, null, null)
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _showFirstNameResult.emit("success")
                        }
                        is Result.Error -> {}
                    }
                }
        }
    }

    fun changeLastName(lastName: String?) {
        viewModelScope.launch {
            if (lastName == null || lastName.isBlank()) {
                _showLastNameResult.emit("last name is blank")
                return@launch
            }
            apiService.changeUserInfo(null, lastName, null, null)
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _showLastNameResult.emit("success")
                        }
                        is Result.Error -> {}
                    }
                }
        }
    }

    fun changeEmail(email: String?) {
        viewModelScope.launch {
            if (email == null || email.isBlank()) {
                _showEmailResult.emit("email is blank")
                return@launch
            }
            apiService.changeUserInfo(null, null, email, null)
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _showEmailResult.emit("success")
                        }
                        is Result.Error -> {}
                    }
                }
        }
    }

    fun changeAboutMe(aboutMe: String?) {
        viewModelScope.launch {
            apiService.changeUserInfo(null, null, null, aboutMe)
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _showAboutMeResult.emit("success")
                        }
                        is Result.Error -> {}
                    }
                }
        }
    }
}