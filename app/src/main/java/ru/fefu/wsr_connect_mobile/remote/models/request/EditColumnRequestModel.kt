package ru.fefu.wsr_connect_mobile.remote.models.request

import com.google.gson.annotations.SerializedName

data class EditColumnRequestModel(
    @SerializedName("board_id")
    var boardId: Int,

    @SerializedName("column_id")
    var columnId: Int,

    @SerializedName("column_title")
    var columnTitle: String,
)
