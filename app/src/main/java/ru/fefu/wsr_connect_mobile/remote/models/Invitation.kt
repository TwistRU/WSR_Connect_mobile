package ru.fefu.wsr_connect_mobile.remote.models

import com.google.gson.annotations.SerializedName

data class Invitation(
    @SerializedName("invite_id")
    var inviteId: Int,

    @SerializedName("company_id")
    var companyId: Int,

    @SerializedName("company_name")
    var companyName: String,

    @SerializedName("invite_body")
    var inviteBody: String,
)
