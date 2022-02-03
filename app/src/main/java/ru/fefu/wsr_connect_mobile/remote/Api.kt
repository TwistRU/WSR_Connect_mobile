package ru.fefu.wsr_connect_mobile.remote

import okhttp3.MultipartBody
import retrofit2.http.*
import ru.fefu.wsr_connect_mobile.remote.models.*


interface Api {


    ////////////////////////////////////////__AUTH__///////////////////////////////////////////////


    @POST("/auth/registration")
    suspend fun registration(@Body request: RegistrationRequestModel): RegistrationResponseModel

    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequestModel): LoginResponseModel

    @DELETE("/auth/logout")
    suspend fun logout()


    //////////////////////////////////////__PROFILE__//////////////////////////////////////////////


    @GET("/profile/info")
    suspend fun getProfileInfo(): User

    @PUT("/profile/info")
    suspend fun changeProfileInfo(@Body request: ChangeProfileInfoRequestModel)

    @PUT("/profile/password")
    suspend fun changePassword(@Body request: ChangePasswordRequestModel)

    @PUT("/profile/info/image")
    @Multipart
    suspend fun sendProfileImage(@Part body: MultipartBody.Part): ImageResponseModel

    @DELETE("/profile/info/image")
    suspend fun deleteProfileImage()


    //////////////////////////////////__TASKS__COMPANY__///////////////////////////////////////////


    @GET("/users")
    suspend fun searchUserApp(@Query("search") search: String): UsersResponseModel

    @GET("/company")
    suspend fun getCompanyInfo(): CompanyInfoResponseModel

    @POST("/company")
    suspend fun createCompany(@Query("company_name") companyName: String)

    @Multipart
    @POST("/company")
    suspend fun createCompanyWithImage(
        @Part body: MultipartBody.Part,
        @Query("company_name") companyName: String
    )

    @PUT("/company")
    suspend fun editCompany(
        @Query("company_name") companyName: String,
        @Query("delete_img") deleteImg: Boolean?
    )

    @Multipart
    @PUT("/company")
    suspend fun editCompanyWithImage(
        @Part body: MultipartBody.Part,
        @Query("company_name") companyName: String
    )

    @GET("/company/users")
    suspend fun searchUserCompany(@Query("search") search: String?): UsersResponseModel

    @DELETE("/company/users")
    suspend fun deleteUserCompany(@Query("user_id") userId: Int)

    @POST("/company/users/invite")
    suspend fun sendCompanyInvite(@Body request: SendCompanyInviteRequestModel)

    @GET("/invitations")
    suspend fun getInvitations(): InvitationsResponseModel

    @PUT("/invitations")
    suspend fun acceptInvitation(@Body request: AcceptInvitationRequestModel)

    @GET("/company/user")
    suspend fun getCompanyUserInfo(@Query("user_id") userId: Int): User

    @DELETE("/company/users/quit")
    suspend fun quitCompany()


    //////////////////////////////////__TASKS__BOARD__/////////////////////////////////////////////


    @GET("/company/board")
    suspend fun getBoards(): BoardsResponseModel

    @POST("/company/board")
    suspend fun createBoard(@Query("board_name") boardName: String)

    @Multipart
    @POST("/company/board")
    suspend fun createBoardWithImage(
        @Part body: MultipartBody.Part,
        @Query("board_name") boardName: String
    )

    @PUT("/company/board")
    suspend fun editBoard(
        @Query("board_id") boardId: Int,
        @Query("board_name") boardName: String,
        @Query("delete_img") deleteImg: Boolean?
    )

    @Multipart
    @PUT("/company/board")
    suspend fun editBoardWithImage(
        @Part body: MultipartBody.Part,
        @Query("board_id") boardId: Int,
        @Query("board_name") boardName: String
    )

    @DELETE("/company/board")
    suspend fun deleteBoard(@Query("board_id") boardId: Int)

    @GET("/company/board/users")
    suspend fun searchUserBoard(
        @Query("board_id") boardId: Int,
        @Query("search") search: String?
    ): UsersResponseModel

    @POST("/company/board/users")
    suspend fun addUsersToBoard(
        @Body request: MultipleAddUsersRequestModel,
        @Query("board_id") boardId: Int
    )

    @DELETE("/company/board/users")
    suspend fun deleteUserFromBoard(
        @Query("board_id") boardId: Int,
        @Query("user_id") userId: Int
    )

    @DELETE("/company/board/users/quit")
    suspend fun quitBoard(@Query("board_id") boardId: Int)


    //////////////////////////////////__TASKS__COLUMN__////////////////////////////////////////////


    @GET("/company/board/column")
    suspend fun getColumns(@Query("board_id") boardId: Int): ColumnsResponseModel

    @POST("/company/board/column")
    suspend fun createColumn(@Body request: CreateColumnRequestModel)

    @PUT("/company/board/column")
    suspend fun editColumn(@Body request: EditColumnRequestModel)

    @DELETE("/company/board/column")
    suspend fun deleteColumn(@Query("column_id") columnId: Int)


    //////////////////////////////////__TASKS__CARD__//////////////////////////////////////////////


    @POST("/company/board/column/card")
    suspend fun createCard(@Body request: CreateCardRequestModel)

    @PUT("/company/board/column/card")
    suspend fun editCard(@Body request: EditCardRequestModel)

    @DELETE("/company/board/column/card")
    suspend fun deleteCard(@Query("card_id") cardId: Int)

    @GET("/company/board/column/card/detail")
    suspend fun getDetailCard(@Query("card_id") cardId: Int): DetailCard

    @POST("/company/board/column/card/users")
    suspend fun addUsersToCard(
        @Body request: MultipleAddUsersRequestModel,
        @Query("card_id") cardId: Int
    )

    @DELETE("/company/board/column/card/users")
    suspend fun deleteUserFromCard(
        @Query("card_id") cardId: Int,
        @Query("user_id") userId: Int
    )

    @DELETE("/company/board/column/card/users/quit")
    suspend fun quitCard(@Query("card_id") cardId: Int)


    //////////////////////////////////////__CHATS__////////////////////////////////////////////////


    @GET("/chats")
    suspend fun getChats(@Query("search") search: String?): ChatsResponseModel

    @GET("/chat/new")
    suspend fun startPrivateChat(@Query("user_id") userId: Int): Int

    @POST("/chat/group")
    suspend fun createGroupChat(@Query("chat_name") chatName: String)

    @Multipart
    @POST("/chat/group")
    suspend fun createGroupChatWithImage(
        @Part body: MultipartBody.Part,
        @Query("chat_name") chatName: String,
    )

    @PUT("/chat/group")
    suspend fun editGroupChat(
        @Query("chat_id") chatId: Int,
        @Query("chat_name") chatName: String,
        @Query("delete_img") deleteImg: Boolean?
    )

    @Multipart
    @PUT("/chat/group")
    suspend fun editGroupChatWithImage(
        @Part body: MultipartBody.Part,
        @Query("chat_id") chatId: Int,
        @Query("chat_name") chatName: String,
    )

    @DELETE("/chat/group")
    suspend fun deleteGroupChat(@Query("chat_id") chatId: Int)

    @POST("/chat/group/users")
    suspend fun addUsersToGroupChat(
        @Body request: MultipleAddUsersRequestModel,
        @Query("chat_id") chatId: Int
    )

    @DELETE("/chat/group/users")
    suspend fun deleteUserFromGroupChat(
        @Query("chat_id") chatId: Int,
        @Query("user_id") userId: Int
    )

    @PUT("/chat/mute")
    suspend fun muteChat(@Body request: StatusChatRequestModel)

    @PUT("/chat/pin")
    suspend fun pinChat(@Body request: StatusChatRequestModel)

    @DELETE("/chat/quit")
    suspend fun quitChat(@Query("chat_id") chatId: Int)


    /////////////////////////////////////__IN_CHAT__///////////////////////////////////////////////


    @GET("/chat/info")
    suspend fun getChatInfo(@Query("chat_id") chatId: Int): ChatInfoResponseModel

    @GET("/chat/messages")
    suspend fun getMessages(@Query("chat_id") chatId: Int): MessagesResponseModel

    @POST("/chat/message")
    suspend fun sendMessage(@Body request: MessageRequestModel)

    @POST("/chat/message/image")
    @Multipart
    suspend fun sendImageMessage(
        @Part body: MultipartBody.Part,
        @Query("chat_id") chatId: Int
    )

    @PUT("/chat/message")
    suspend fun editMessage(@Body request: MessageRequestModel)

    @DELETE("/chat/message")
    suspend fun deleteMessage(@Query("message_id") messageId: Int)
}