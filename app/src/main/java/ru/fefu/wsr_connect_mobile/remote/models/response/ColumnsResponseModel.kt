package ru.fefu.wsr_connect_mobile.remote.models.response

import com.google.gson.annotations.SerializedName
import ru.fefu.wsr_connect_mobile.remote.models.Column

data class ColumnsResponseModel(
    @SerializedName("columns")
    var columns: List<Column>,
)
