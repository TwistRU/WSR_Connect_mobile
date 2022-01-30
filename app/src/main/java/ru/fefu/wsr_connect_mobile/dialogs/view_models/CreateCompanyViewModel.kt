package ru.fefu.wsr_connect_mobile.dialogs.view_models

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.fefu.wsr_connect_mobile.remote.ApiService
import ru.fefu.wsr_connect_mobile.remote.Result
import java.io.ByteArrayOutputStream
import java.util.*

class CreateCompanyViewModel : ViewModel() {

    private val apiService = ApiService()

    private val _showFieldError = MutableSharedFlow<String>(replay = 0)
    private val _showLoading = MutableStateFlow(false)
    private val _success = MutableStateFlow(false)

    val showFieldError get() = _showFieldError
    val showLoading get() = _showLoading
    val success get() = _success

    fun createCompany(image: Bitmap?, companyName: String) {
        var body: MultipartBody.Part? = null
        if (image != null) {
            val stream = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 80, stream)
            val byteArray = stream.toByteArray()
            body = MultipartBody.Part.createFormData(
                "file", "${Calendar.getInstance().time}",
                byteArray.toRequestBody("image/*".toMediaTypeOrNull(), 0, byteArray.size)
            )
        }

        viewModelScope.launch {
            if (companyName.isBlank()) {
                _showFieldError.emit("field is blank")
                return@launch
            }
            apiService.createCompany(body, companyName)
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