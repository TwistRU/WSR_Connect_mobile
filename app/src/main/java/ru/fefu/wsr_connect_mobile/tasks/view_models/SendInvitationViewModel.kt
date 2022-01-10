package ru.fefu.wsr_connect_mobile.tasks.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.fefu.wsr_connect_mobile.remote.ApiService
import ru.fefu.wsr_connect_mobile.remote.Result

class SendInvitationViewModel : ViewModel() {

    private val apiService = ApiService()

    private val _showNicknameError = MutableSharedFlow<String>(replay = 0)
    private val _showInviteTextError = MutableSharedFlow<String>(replay = 0)
    private val _showLoading = MutableStateFlow(false)
    private val _success = MutableStateFlow(false)

    val showNicknameError get() = _showNicknameError
    val showInviteTextError get() = _showInviteTextError
    val showLoading get() = _showLoading
    val success get() = _success

    fun sendInvite(
        context: String,
        boardId: Int?,
        userNickname: String,
        inviteText: String
    ) {
        viewModelScope.launch {
            if (userNickname.isBlank()) {
                _showNicknameError.emit("nickname is blank")
                return@launch
            }

            if (inviteText.isBlank()) {
                _showInviteTextError.emit("text is blank")
                return@launch
            }
            apiService.sendInvite(
                context,
                boardId,
                userNickname,
                inviteText
            )
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