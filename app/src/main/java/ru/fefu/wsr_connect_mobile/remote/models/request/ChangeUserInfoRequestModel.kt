package ru.fefu.wsr_connect_mobile.remote.models.request

import com.google.gson.annotations.SerializedName

data class ChangeUserInfoRequestModel(
    @SerializedName("first_name")
    var firstName: String?,

    @SerializedName("last_name")
    var lastName: String?,

    @SerializedName("email")
    var email: String?,

    @SerializedName("about_me")
    var aboutMe: String?,
)
