package ru.fefu.wsr_connect_mobile.tasks.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.fefu.wsr_connect_mobile.remote.ApiService
import ru.fefu.wsr_connect_mobile.remote.Result
import ru.fefu.wsr_connect_mobile.remote.models.User

class SearchUserAppViewModel : ViewModel() {

    private val apiService = ApiService()

    private val _usersApp = MutableSharedFlow<List<User>>(replay = 0)
    private val _success = MutableStateFlow(false)
    private val _showLoading = MutableStateFlow(false)

    val usersApp get() = _usersApp
    val success get() = _success
    val showLoading get() = _showLoading

    fun searchUserApp(search: String) {
        viewModelScope.launch {
            apiService.searchUserApp(search)
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _usersApp.emit(it.result.users)
                        }
                        is Result.Error -> {}
                    }
                }
        }
    }
}