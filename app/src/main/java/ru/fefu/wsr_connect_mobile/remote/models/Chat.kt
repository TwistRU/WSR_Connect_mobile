package ru.fefu.wsr_connect_mobile.remote.models

import com.google.gson.annotations.SerializedName

data class Chat(
    @SerializedName("chat_id")
    var chatId: Int,

    @SerializedName("chat_name")
    var chatName: String,

    @SerializedName("last_message")
    var lastMessage: Message?,

    @SerializedName("is_mute")
    var isMute: Boolean,

    @SerializedName("is_pinned")
    var isPinned: Boolean,

    @SerializedName("chat_image")
    var chatImage: String?,
)
