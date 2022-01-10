package ru.fefu.wsr_connect_mobile.tasks.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.fefu.wsr_connect_mobile.remote.ApiService
import ru.fefu.wsr_connect_mobile.remote.Result

class EditColumnViewModel : ViewModel() {

    private val apiService = ApiService()

    private val _showFieldError = MutableSharedFlow<String>(replay = 0)
    private val _showLoading = MutableStateFlow(false)
    private val _success = MutableStateFlow(false)

    val showFieldError get() = _showFieldError
    val showLoading get() = _showLoading
    val success get() = _success

    fun editBoard(boardId: Int, columnId: Int, columnTitle: String) {
        viewModelScope.launch {
            if (columnTitle.isBlank()) {
                _showFieldError.emit("field is blank")
                return@launch
            }
            apiService.editColumn(boardId, columnId, columnTitle)
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