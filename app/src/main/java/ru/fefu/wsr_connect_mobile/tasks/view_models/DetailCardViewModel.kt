package ru.fefu.wsr_connect_mobile.tasks.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.fefu.wsr_connect_mobile.remote.ApiService
import ru.fefu.wsr_connect_mobile.remote.Result
import ru.fefu.wsr_connect_mobile.remote.models.DetailCard
import ru.fefu.wsr_connect_mobile.remote.models.User

class DetailCardViewModel : ViewModel() {

    private val apiService = ApiService()

    private val _card = MutableSharedFlow<DetailCard>(replay = 0)
    private val _creator = MutableSharedFlow<User>(replay = 0)
    private val _success = MutableStateFlow(false)
    private val _showLoading = MutableStateFlow(false)

    val card get() = _card
    val creator get() = _creator
    val success get() = _success
    val showLoading get() = _showLoading

    fun getDetailCard(cardId: Int) {
        viewModelScope.launch {
            apiService.getDetailCard(cardId)
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _card.emit(it.result)
                        }
                        is Result.Error -> {}
                    }
                }
        }
    }

    fun getCreatorInfo(userId: Int) {
        viewModelScope.launch {
            apiService.getCompanyUserInfo(userId)
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _creator.emit(it.result)
                        }
                        is Result.Error -> {}
                    }
                }
        }
    }
}