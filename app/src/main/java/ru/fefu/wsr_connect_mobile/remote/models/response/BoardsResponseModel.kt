package ru.fefu.wsr_connect_mobile.remote.models.response

import com.google.gson.annotations.SerializedName
import ru.fefu.wsr_connect_mobile.remote.models.Board

data class BoardsResponseModel(
    @SerializedName("boards")
    var boards: List<Board>,
)
