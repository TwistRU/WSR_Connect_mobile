package ru.fefu.wsr_connect_mobile.remote.models.request

import com.google.gson.annotations.SerializedName

data class SendInviteRequestModel(
    @SerializedName("context")
    var context: String,

    @SerializedName("board_id")
    var board_id: Int?,

    @SerializedName("user_nickname")
    var userNickname: String,

    @SerializedName("invite_text")
    var inviteText: String,
)
