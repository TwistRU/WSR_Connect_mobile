package ru.fefu.wsr_connect_mobile.tasks.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.fefu.wsr_connect_mobile.remote.ApiService
import ru.fefu.wsr_connect_mobile.remote.Result
import ru.fefu.wsr_connect_mobile.remote.models.User

class SearchUserForGroupChatViewModel : ViewModel() {

    private val apiService = ApiService()

    private val _usersCompany = MutableSharedFlow<List<User>>(replay = 0)
    private val _success = MutableSharedFlow<Boolean>(replay = 0)
    private val _showLoading = MutableStateFlow(false)

    val usersCompany get() = _usersCompany
    val success get() = _success
    val showLoading get() = _showLoading

    fun searchUserCompany(search: String?) {
        viewModelScope.launch {
            apiService.searchUserCompany(search)
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _usersCompany.emit(it.result.users)
                        }
                        is Result.Error -> {}
                    }
                }
        }
    }

    fun addUsersToGroupChat(users: MutableSet<Int>, chatId: Int) {
        viewModelScope.launch {
            apiService.addUsersToGroupChat(users.toList(), chatId)
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _success.emit(true)
                        }
                        is Result.Error -> {}
                    }
                }
        }
    }
}