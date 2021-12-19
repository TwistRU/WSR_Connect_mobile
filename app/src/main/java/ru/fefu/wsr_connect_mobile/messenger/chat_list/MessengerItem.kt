package ru.fefu.wsr_connect_mobile.messenger.chat_list

import android.graphics.drawable.Drawable

sealed class MessengerItem

class ItemNewChat(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val Image: Drawable?,
) : MessengerItem()

class ItemChat(
    val id: Int,
    val chatName: String,
    val lastMessage: String,
    val isMute: Boolean,
    val isPinned: Boolean,
    val Image: Drawable?,
) : MessengerItem()
