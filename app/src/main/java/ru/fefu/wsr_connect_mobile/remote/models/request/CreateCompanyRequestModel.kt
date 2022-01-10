package ru.fefu.wsr_connect_mobile.remote.models.request

import com.google.gson.annotations.SerializedName

data class CreateCompanyRequestModel(
    @SerializedName("company_name")
    var companyName: String,
)
