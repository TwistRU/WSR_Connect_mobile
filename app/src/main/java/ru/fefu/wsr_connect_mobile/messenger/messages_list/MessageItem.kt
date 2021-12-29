package ru.fefu.wsr_connect_mobile.messenger.messages_list

import android.graphics.drawable.Drawable

sealed class MessageItem

class ItemSimpleMessage(
    val id: Int,
    val read: Boolean,
    val body: String,
    val time: String,
) : MessageItem()

class ItemImageMessage(
    val id: Int,
    val read: Boolean,
    val img: Drawable?,
    val name: String,
    val size: String,
    val time: String,
) : MessageItem()

class ItemReplyMessage(
    val id: Int,
    val read: Boolean,
    val body: String,
    val user: String,
    val reply: String,
    val time: String,
) : MessageItem()

class ItemSimpleMessageOther(
    val id: Int,
    val body: String,
    val time: String,
) : MessageItem()

class ItemImageMessageOther(
    val id: Int,
    val img: Drawable?,
    val name: String,
    val size: String,
    val time: String,
) : MessageItem()

class ItemReplyMessageOther(
    val id: Int,
    val body: String,
    val user: String,
    val reply: String,
    val time: String,
) : MessageItem()
