package ru.fefu.wsr_connect_mobile.remote.models

import com.google.gson.annotations.SerializedName

data class Board(
    @SerializedName("board_id")
    var boardId: Int,

    @SerializedName("board_name")
    var boardName: String,

    @SerializedName("board_create_date")
    var boardCreateDate: String,

    @SerializedName("board_creator")
    var boardCreator: String,

    @SerializedName("board_user_count")
    var boardUserCount: String,

    @SerializedName("is_available")
    var isAvailable: Boolean,

    @SerializedName("img_id")
    var imgId: Int?,
)
