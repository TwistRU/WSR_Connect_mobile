package ru.fefu.wsr_connect_mobile.tasks.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.fefu.wsr_connect_mobile.remote.ApiService
import ru.fefu.wsr_connect_mobile.remote.Result
import ru.fefu.wsr_connect_mobile.remote.models.CompanyUser

class CompanyUsersListViewModel : ViewModel() {

    private val apiService = ApiService()

    private val _companyUsers = MutableSharedFlow<List<CompanyUser>>(replay = 0)
    private val _success = MutableStateFlow(false)
    private val _showLoading = MutableStateFlow(false)

    val companyUsers get() = _companyUsers
    val success get() = _success
    val showLoading get() = _showLoading

    fun getCompanyUsers(
        context: String,
        companyId: Int?,
        boardId: Int?,
    ) {
        viewModelScope.launch {
            apiService.getCompanyUsers(
                context,
                companyId,
                boardId
            )
                .onStart { _showLoading.value = true }
                .onCompletion { _showLoading.value = false }
                .collect {
                    when (it) {
                        is Result.Success -> {
                            _companyUsers.emit(it.result.companyUsers)
                        }
                        is Result.Error -> {}
                    }
                }
        }
    }
}