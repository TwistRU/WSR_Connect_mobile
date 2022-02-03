package ru.fefu.wsr_connect_mobile.tasks.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.fefu.wsr_connect_mobile.remote.ApiService
import ru.fefu.wsr_connect_mobile.remote.Result
import ru.fefu.wsr_connect_mobile.remote.models.User

class BoardUsersViewModel : ViewModel() {

    private val apiService = ApiService()

    private val _boardUsers = MutableSharedFlow<List<User>>(replay = 0)
    private val _success = MutableStateFlow(false)
    private val _showLoading = MutableStateFlow(false)

    val boardUsers get() = _boardUsers
    val success get() = _success
    val showLoading get() = _showLoading

    fun getBoardUsers(boardId: Int) {
        viewModelScope.launch {
            apiService.searchUserBoard(boardId, null)
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _boardUsers.emit(it.result.users)
                        }
                        is Result.Error -> {}
                    }
                }
        }
    }
}