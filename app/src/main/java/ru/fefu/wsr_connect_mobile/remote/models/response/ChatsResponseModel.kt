package ru.fefu.wsr_connect_mobile.remote.models.response

import com.google.gson.annotations.SerializedName
import ru.fefu.wsr_connect_mobile.remote.models.Chat

data class ChatsResponseModel(
    @SerializedName("chats")
    var chats: List<Chat>,
)
