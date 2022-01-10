package ru.fefu.wsr_connect_mobile.remote.models.request

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequestModel(
    @SerializedName("old_password")
    var oldPassword: String?,

    @SerializedName("new_password")
    var newPassword: String?,
)
