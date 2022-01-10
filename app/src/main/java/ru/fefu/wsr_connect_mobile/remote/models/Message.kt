package ru.fefu.wsr_connect_mobile.remote.models

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("chat_id")
    var chatId: Int,

    @SerializedName("creator_id")
    var creatorID: Int,

    @SerializedName("created_at")
    var createdAt: String,

    @SerializedName("message_id")
    var messageId: Int,

    @SerializedName("message_body")
    var messageBody: String,

    @SerializedName("read")
    var read: Boolean,

    @SerializedName("parent_message")
    var parentMessage: Message?,
)
