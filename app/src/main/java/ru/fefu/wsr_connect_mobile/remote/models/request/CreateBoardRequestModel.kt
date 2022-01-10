package ru.fefu.wsr_connect_mobile.remote.models.request

import com.google.gson.annotations.SerializedName

data class CreateBoardRequestModel(
    @SerializedName("board_name")
    var boardName: String,
)
