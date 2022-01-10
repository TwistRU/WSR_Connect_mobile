package ru.fefu.wsr_connect_mobile.tasks.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.fefu.wsr_connect_mobile.remote.ApiService
import ru.fefu.wsr_connect_mobile.remote.Result

class EditCardViewModel : ViewModel() {

    private val apiService = ApiService()

    private val _showTitleError = MutableSharedFlow<String>(replay = 0)
    private val _showShortDescError = MutableSharedFlow<String>(replay = 0)
    private val _showLoading = MutableStateFlow(false)
    private val _success = MutableStateFlow(false)

    val showTitleError get() = _showTitleError
    val showShortDescError get() = _showShortDescError
    val showLoading get() = _showLoading
    val success get() = _success

    fun editCard(
        boardId: Int,
        columnId: Int,
        cardId: Int,
        cardTitle: String,
        cardShortDesc: String,
        cardLongDesc: String
    ) {
        viewModelScope.launch {
            if (cardTitle.isBlank()) {
                _showTitleError.emit("title is blank")
                return@launch
            }

            if (cardShortDesc.isBlank()) {
                _showTitleError.emit("short description is blank")
                return@launch
            }
            apiService.editCard(
                boardId,
                columnId,
                cardId,
                cardTitle,
                cardShortDesc,
                cardLongDesc
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