package ru.fefu.wsr_connect_mobile.tasks.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.fefu.wsr_connect_mobile.remote.ApiService
import ru.fefu.wsr_connect_mobile.remote.Result
import ru.fefu.wsr_connect_mobile.remote.models.Column

class ColumnsListViewModel : ViewModel() {

    private val apiService = ApiService()

    private val _columns = MutableSharedFlow<List<Column>>(replay = 0)
    private val _success = MutableStateFlow(false)
    private val _showLoading = MutableStateFlow(false)

    val columns get() = _columns
    val success get() = _success
    val showLoading get() = _showLoading

    fun getColumns(boardId: Int) {
        viewModelScope.launch {
            apiService.getColumns(boardId)
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _columns.emit(it.result.columns)
                        }
                        is Result.Error -> {}
                    }
                }
        }
    }
}