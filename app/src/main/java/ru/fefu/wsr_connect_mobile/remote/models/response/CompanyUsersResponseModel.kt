package ru.fefu.wsr_connect_mobile.remote.models.response

import com.google.gson.annotations.SerializedName
import ru.fefu.wsr_connect_mobile.remote.models.CompanyUser

data class CompanyUsersResponseModel(
    @SerializedName("company_users")
    var companyUsers: List<CompanyUser>,
)
