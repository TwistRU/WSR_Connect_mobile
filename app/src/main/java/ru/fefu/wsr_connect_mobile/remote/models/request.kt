package ru.fefu.wsr_connect_mobile.remote.models

import com.google.gson.annotations.SerializedName

data class RegistrationRequestModel(
    @SerializedName("username")
    var username: String,

    @SerializedName("first_name")
    var firstName: String,

    @SerializedName("last_name")
    var lastName: String,

    @SerializedName("email")
    var email: String,

    @SerializedName("password")
    var password: String,
)

data class LoginRequestModel(
    @SerializedName("username")
    var username: String,

    @SerializedName("password")
    var password: String,
)

data class ChangeProfileInfoRequestModel(
    @SerializedName("first_name")
    var firstName: String?,

    @SerializedName("last_name")
    var lastName: String?,

    @SerializedName("email")
    var email: String?,

    @SerializedName("about_me")
    var aboutMe: String?,
)

data class ChangePasswordRequestModel(
    @SerializedName("old_password")
    var oldPassword: String?,

    @SerializedName("new_password")
    var newPassword: String?,
)

data class SendCompanyInviteRequestModel(
    @SerializedName("user_id")
    var userId: Int,

    @SerializedName("invite_text")
    var inviteText: String,
)

data class AcceptInvitationRequestModel(
    @SerializedName("invite_id")
    var inviteId: Int,
)

data class CreateColumnRequestModel(
    @SerializedName("board_id")
    var boardId: Int,

    @SerializedName("column_name")
    var columnName: String,
)

data class EditColumnRequestModel(
    @SerializedName("column_id")
    var columnId: Int,

    @SerializedName("column_title")
    var columnTitle: String,
)

data class CreateCardRequestModel(
    @SerializedName("board_id")
    var boardId: Int,

    @SerializedName("column_id")
    var columnId: Int,

    @SerializedName("card_title")
    var cardTitle: String,

    @SerializedName("card_short_desc")
    var cardShortDesc: String,

    @SerializedName("card_long_desc")
    var cardLongDesc: String?,

    @SerializedName("deadline")
    var deadline: String?,
)

data class EditCardRequestModel(
    @SerializedName("card_id")
    var cardId: Int,

    @SerializedName("card_title")
    var cardTitle: String,

    @SerializedName("deadline")
    var deadline: String?,

    @SerializedName("card_short_desc")
    var cardShortDesc: String,

    @SerializedName("card_long_desc")
    var cardLongDesc: String?
)

data class StatusChatRequestModel(
    @SerializedName("chat_id")
    var chatId: Int,

    @SerializedName("status")
    var status: Boolean,
)

data class MessageRequestModel(
    @SerializedName("chat_id")
    var chatId: Int,

    @SerializedName("message_id")
    var messageId: Int?,

    @SerializedName("replied_message_id")
    var repliedMessageId: Int?,

    @SerializedName("message_body")
    var messageBody: String,
)

data class MultipleAddUsersRequestModel(
    @SerializedName("users")
    var users: List<Int>
)