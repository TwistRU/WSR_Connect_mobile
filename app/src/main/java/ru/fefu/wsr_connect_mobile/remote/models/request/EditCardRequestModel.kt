package ru.fefu.wsr_connect_mobile.remote.models.request

import com.google.gson.annotations.SerializedName

data class EditCardRequestModel(
    @SerializedName("board_id")
    var boardId: Int,

    @SerializedName("column_id")
    var columnId: Int,

    @SerializedName("card_id")
    var cardId: Int,

    @SerializedName("card_title")
    var cardTitle: String,

    @SerializedName("card_short_desc")
    var cardShortDesc: String,

    @SerializedName("card_long_desc")
    var cardLongDesc: String,
)
