package ru.fefu.wsr_connect_mobile.start.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.fefu.wsr_connect_mobile.remote.ApiService
import ru.fefu.wsr_connect_mobile.remote.Result
import ru.fefu.wsr_connect_mobile.remote.models.User

class SplashViewModel : ViewModel() {

    private val apiService = ApiService()

    private val _showLoading = MutableStateFlow(false)
    private val _info = MutableSharedFlow<User>(replay = 0)

    val showLoading get() = _showLoading
    val info get() = _info

    fun getUserInfo() {
        viewModelScope.launch {
            apiService.getUserInfo()
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