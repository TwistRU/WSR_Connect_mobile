package ru.fefu.wsr_connect_mobile.remote.models

import com.google.gson.annotations.SerializedName

data class RegistrationResponseModel(
    @SerializedName("token")
    var token: String,

    @SerializedName("my_id")
    var myId: Int,
)

data class LoginResponseModel(
    @SerializedName("token")
    var token: String,

    @SerializedName("my_id")
    var myId: Int,
)

data class CompanyInfoResponseModel(
    @SerializedName("company_name")
    var companyName: String,

    @SerializedName("mine")
    var mine: Boolean,

    @SerializedName("img_url")
    var imgUrl: String?
)

data class InvitationsResponseModel(
    @SerializedName("invitations")
    var invitations: List<Invitation>,
)

data class BoardsResponseModel(
    @SerializedName("boards")
    var boards: List<Board>,
)

data class ColumnsResponseModel(
    @SerializedName("columns")
    var columns: List<Column>,
)

data class ChatsResponseModel(
    @SerializedName("chats")
    var chats: List<Chat>,
)

data class DetailCard(
    @SerializedName("card_id")
    var cardId: Int,

    @SerializedName("card_title")
    var cardTitle: String,

    @SerializedName("card_short_desc")
    var cardShortDesc: String,

    @SerializedName("card_long_desc")
    var cardLongDesc: String?,

    @SerializedName("create_date")
    var createDate: String,

    @SerializedName("deadline")
    var deadline: String?,

    @SerializedName("card_creator")
    var cardCreator: String,

    @SerializedName("card_creator_id")
    var cardCreatorId: Int,

    @SerializedName("users")
    var users: List<User>,
)

data class ChatInfoResponseModel(
    @SerializedName("chat_name")
    var chatName: String,

    @SerializedName("mine")
    var mine: Boolean,

    @SerializedName("img_url")
    var imgUrl: String?,

    @SerializedName("users")
    var users: List<User>
)

data class UsersResponseModel(
    @SerializedName("users")
    var users: List<User>
)

data class ImageResponseModel(
    @SerializedName("url")
    var url: String,
)

data class MessagesResponseModel(
    @SerializedName("messages")
    var messages: List<Message>,
)