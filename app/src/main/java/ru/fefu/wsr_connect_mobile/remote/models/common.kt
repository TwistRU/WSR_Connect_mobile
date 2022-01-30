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

    @SerializedName("board_creator_id")
    var boardCreatorId: Int,

    @SerializedName("board_user_count")
    var boardUserCount: String,

    @SerializedName("is_available")
    var isAvailable: Boolean,

    @SerializedName("img_url")
    var imgUrl: String?,
)

data class Card(
    @SerializedName("card_id")
    var cardId: Int,

    @SerializedName("card_title")
    var cardTitle: String,

    @SerializedName("card_short_desc")
    var cardShortDesc: String,

    @SerializedName("create_date")
    var createDate: String,

    @SerializedName("deadline")
    var deadline: String?,

    @SerializedName("card_creator")
    var cardCreator: String,

    @SerializedName("card_creator_id")
    var cardCreatorId: Int,
)

data class Chat(
    @SerializedName("chat_id")
    var chatId: Int,

    @SerializedName("chat_name")
    var chatName: String,

    @SerializedName("last_message")
    var lastMessage: Message?,

    @SerializedName("mute")
    var mute: Boolean,

    @SerializedName("pin")
    var pin: Boolean,

    @SerializedName("img_url")
    var imgUrl: String?,
)

data class Column(
    @SerializedName("column_id")
    var columnId: Int,

    @SerializedName("column_title")
    var columnTitle: String,

    @SerializedName("cards")
    var cards: List<Card>
)

data class Invitation(
    @SerializedName("invite_id")
    var inviteId: Int,

    @SerializedName("company_name")
    var companyName: String,

    @SerializedName("invite_body")
    var inviteBody: String,

    @SerializedName("img_url")
    var imgUrl: String?,
)

data class Message(
    @SerializedName("chat_id")
    var chatId: Int,

    @SerializedName("created_at")
    var createdAt: String,

    @SerializedName("creator_id")
    var creatorId: Int,

    @SerializedName("creator_name")
    var creatorName: String,

    @SerializedName("edit")
    var edit: Boolean,

    @SerializedName("img_url")
    var imgUrl: String?,

    @SerializedName("message_body")
    var messageBody: String,

    @SerializedName("message_id")
    var messageId: Int,

    @SerializedName("mine")
    var mine: Boolean,

    @SerializedName("parent_message")
    var parentMessage: Message?,

    @SerializedName("read")
    var read: Boolean,
)

data class User(
    @SerializedName("username")
    var username: String,

    @SerializedName("user_id")
    var userId: Int,

    @SerializedName("first_name")
    var firstName: String,

    @SerializedName("last_name")
    var lastName: String,

    @SerializedName("email")
    var email: String,

    @SerializedName("company_id")
    var companyId: Int?,

    @SerializedName("about_me")
    var aboutMe: String?,

    @SerializedName("img_url")
    var imgUrl: String?,
)