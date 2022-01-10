package ru.fefu.wsr_connect_mobile.remote.models.request

import com.google.gson.annotations.SerializedName

data class CreateColumnRequestModel(
    @SerializedName("board_id")
    var boardId: Int,

    @SerializedName("column_name")
    var columnName: String,
)