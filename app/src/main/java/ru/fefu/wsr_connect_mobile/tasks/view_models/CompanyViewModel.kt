package ru.fefu.wsr_connect_mobile.tasks.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.fefu.wsr_connect_mobile.remote.ApiService
import ru.fefu.wsr_connect_mobile.remote.Result
import ru.fefu.wsr_connect_mobile.remote.models.Board
import ru.fefu.wsr_connect_mobile.remote.models.CompanyInfoResponseModel

class CompanyViewModel : ViewModel() {

    private val apiService = ApiService()

    private val _boards = MutableSharedFlow<List<Board>>(replay = 0)
    private val _companyInfo = MutableSharedFlow<CompanyInfoResponseModel>(replay = 0)
    private val _success = MutableStateFlow(false)
    private val _showLoading = MutableStateFlow(false)

    val boards get() = _boards
    val companyInfo get() = _companyInfo
    val success get() = _success
    val showLoading get() = _showLoading

    fun getBoards() {
        viewModelScope.launch {
            apiService.getBoards()
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _boards.emit(it.result.boards)
                        }
                        is Result.Error -> {}
                    }
                }
        }
    }

    fun getCompanyInfo() {
        viewModelScope.launch {
            apiService.getCompanyInfo()
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _companyInfo.emit(it.result)
                        }
                        is Result.Error -> {}
                    }
                }
        }
    }
}