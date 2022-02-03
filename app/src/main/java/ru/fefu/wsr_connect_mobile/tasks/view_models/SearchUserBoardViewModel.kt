package ru.fefu.wsr_connect_mobile.tasks.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.fefu.wsr_connect_mobile.remote.ApiService
import ru.fefu.wsr_connect_mobile.remote.Result
import ru.fefu.wsr_connect_mobile.remote.models.User

class SearchUserBoardViewModel : ViewModel() {

    private val apiService = ApiService()

    private val _usersBoard = MutableSharedFlow<List<User>>(replay = 0)
    private val _success = MutableSharedFlow<Boolean>(replay = 0)
    private val _showLoading = MutableStateFlow(false)

    val usersBoard get() = _usersBoard
    val success get() = _success
    val showLoading get() = _showLoading

    fun searchUserBoard(boardId: Int, search: String?) {
        viewModelScope.launch {
            apiService.searchUserBoard(boardId, search)
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _usersBoard.emit(it.result.users)
                        }
                        is Result.Error -> {}
                    }
                }
        }
    }

    fun addUsersToCard(users: MutableSet<Int>, cardId: Int) {
        viewModelScope.launch {
            apiService.addUsersToCard(users.toList(), cardId)
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