package ru.fefu.wsr_connect_mobile.remote.models.response

import com.google.gson.annotations.SerializedName

data class UserResponseModel(
    @SerializedName("username")
    var username: String,

    @SerializedName("first_name")
    var firstName: String,

    @SerializedName("last_name")
    var lastName: String,

    @SerializedName("email")
    var email: String,

    @SerializedName("img_id")
    var imgId: Int,

    @SerializedName("company_id")
    var companyId: Int,

    @SerializedName("about_me")
    var aboutMe: Int,
)
