package ru.fefu.wsr_connect_mobile.remote.models

import com.google.gson.annotations.SerializedName

data class CompanyUser(
    @SerializedName("user_id")
    var userId: Int,

    @SerializedName("user_first_name")
    var userFirstName: String,

    @SerializedName("user_last_name")
    var userLastName: String,

    @SerializedName("user_email")
    var userEmail: String,

    @SerializedName("img_id")
    var imgId: Int?,
)
