package ru.fefu.wsr_connect_mobile.tasks.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.fefu.wsr_connect_mobile.remote.ApiService
import ru.fefu.wsr_connect_mobile.remote.Result
import ru.fefu.wsr_connect_mobile.remote.models.Invitation

class InvitationsViewModel : ViewModel() {

    private val apiService = ApiService()

    private val _invitations = MutableSharedFlow<List<Invitation>>(replay = 0)
    private val _success = MutableStateFlow(false)
    private val _showLoading = MutableStateFlow(false)

    val invitations get() = _invitations
    val success get() = _success
    val showLoading get() = _showLoading

    fun getInvitations() {
        viewModelScope.launch {
            apiService.getInvitations()
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _invitations.emit(it.result.invitations)
                        }
                        is Result.Error -> {}
                    }
                }
        }
    }

    fun acceptInvitation(inviteId: Int) {
        viewModelScope.launch {
            apiService.acceptInvitation(inviteId)
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