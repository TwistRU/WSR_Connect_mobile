package ru.fefu.wsr_connect_mobile.remote.models.request

import com.google.gson.annotations.SerializedName

data class CreateCardRequestModel(
    @SerializedName("board_id")
    var boardId: Int,

    @SerializedName("column_id")
    var columnId: Int,

    @SerializedName("card_title")
    var cardTitle: String,
)
