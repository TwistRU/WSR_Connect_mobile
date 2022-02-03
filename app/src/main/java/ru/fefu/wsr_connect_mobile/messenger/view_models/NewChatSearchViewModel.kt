package ru.fefu.wsr_connect_mobile.messenger.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.fefu.wsr_connect_mobile.remote.ApiService
import ru.fefu.wsr_connect_mobile.remote.Result
import ru.fefu.wsr_connect_mobile.remote.models.User

class NewChatSearchViewModel : ViewModel() {

    private val apiService = ApiService()

    private val _users = MutableSharedFlow<List<User>>(replay = 0)
    private val _showLoading = MutableStateFlow(false)

    val showLoading get() = _showLoading
    val users get() = _users

    fun searchUserCompany(search: String?) {
        viewModelScope.launch {

            apiService.searchUserCompany(search)
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _users.emit(it.result.users)
                        }
                        is Result.Error -> {}
                    }
                }
        }
    }
}