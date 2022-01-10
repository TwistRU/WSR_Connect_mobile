package ru.fefu.wsr_connect_mobile.remote.models.response

import com.google.gson.annotations.SerializedName

data class RegistrationResponseModel(
    @SerializedName("token")
    var token: String,
)
