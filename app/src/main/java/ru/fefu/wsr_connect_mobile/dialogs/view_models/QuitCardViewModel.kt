package ru.fefu.wsr_connect_mobile.dialogs.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.fefu.wsr_connect_mobile.remote.ApiService
import ru.fefu.wsr_connect_mobile.remote.Result

class QuitCardViewModel : ViewModel() {

    private val apiService = ApiService()

    private val _showLoading = MutableStateFlow(false)
    private val _success = MutableStateFlow(false)

    val showLoading get() = _showLoading
    val success get() = _success

    fun quitCard(cardId: Int) {
        viewModelScope.launch {

            apiService.quitCard(cardId)
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