package ru.fefu.wsr_connect_mobile.remote.models.request

import com.google.gson.annotations.SerializedName

data class AcceptInvitationRequestModel(
    @SerializedName("company_id")
    var companyId: Int,
)
