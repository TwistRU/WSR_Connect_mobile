package ru.fefu.wsr_connect_mobile.remote.models.request

import com.google.gson.annotations.SerializedName

data class LoginRequestModel(
    @SerializedName("username")
    var username: String,

    @SerializedName("password")
    var password: String,
)
