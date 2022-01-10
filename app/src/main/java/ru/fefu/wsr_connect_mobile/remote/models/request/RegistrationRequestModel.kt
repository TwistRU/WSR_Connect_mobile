package ru.fefu.wsr_connect_mobile.remote.models.request

import com.google.gson.annotations.SerializedName

data class RegistrationRequestModel(
    @SerializedName("username")
    var username: String,

    @SerializedName("email")
    var email: String,

    @SerializedName("password")
    var password: String,
)
