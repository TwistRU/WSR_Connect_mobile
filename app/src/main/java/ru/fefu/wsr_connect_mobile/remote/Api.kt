package ru.fefu.wsr_connect_mobile.remote

import okhttp3.MultipartBody
import retrofit2.http.*
import ru.fefu.wsr_connect_mobile.remote.models.*


interface Api {


    //////////////////////////////////__AUTH__////////////////////////////////////////////


    @POST("/auth/registration")
    suspend fun registration(@Body request: RegistrationRequestModel): RegistrationResponseModel

    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequestModel): LoginResponseModel

    @DELETE("/auth/logout")
    suspend fun logout()


    //////////////////////////////////__USER__////////////////////////////////////////////


    @GET("/user/info")
    suspend fun getUserInfo(): User

    @PUT("/user/info")
    suspend fun changeUserInfo(@Body request: ChangeUserInfoRequestModel)

    @PUT("/user/password")
    suspend fun changeUserPassword(@Body request: ChangeUserPasswordRequestModel)

    @PUT("/user/info/image")
    @Multipart
    suspend fun sendProfileImage(@Part body: MultipartBody.Part): ImageResponseModel


    //////////////////////////////////__TASKS__COMPANY__////////////////////////////////////////////


    @GET("/users")
    suspend fun searchUserApp(@Query("search") search: String) : UsersResponseModel

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
    suspend fun editCompanyInfo(@Query("company_name") companyName: String)

    @Multipart
    @PUT("/company")
    suspend fun editCompanyInfoWithImage(
        @Part body: MultipartBody.Part,
        @Query("company_name") companyName: String
    )

    @GET("/company/users")
    suspend fun searchUserCompany(@Query("search") search: String?) : UsersResponseModel

    @DELETE("/company/users")
    suspend fun deleteUserCompany(@Query("user_id") userId: Int)

    @POST("/company/users/invite")
    suspend fun sendCompanyInvite(@Body request: SendCompanyInviteRequestModel)

    @GET("/company/user")
    suspend fun getCompanyUserInfo(@Query("user_id") userId: Int): User


    //////////////////////////////////__TASKS__BOARD__////////////////////////////////////////////


    @GET("/company/board")
    suspend fun getBoards(): BoardsResponseModel

    @POST("/company/board")
    suspend fun createBoard(@Query("board_name") boardName: String)

    @Multipart
    @POST("/company/board")
    suspend fun createBoardWithImage(@Part body: MultipartBody.Part, @Query("board_name") boardName: String)

    @PUT("/company/board")
    suspend fun editBoard(@Body request: EditBoardRequestModel)

    @DELETE("/company/board")
    suspend fun deleteBoard(@Query("board_id") boardId: Int)

    @GET("/company/board/users")
    suspend fun searchUserBoard(@Query("board_id") boardId: Int, @Query("search") search: String?): UsersResponseModel

    @POST("/company/board/users")
    suspend fun addUserBoard(@Query("board_id") boardId: Int, @Query("user_id") userId: Int)

    @DELETE("/company/board/users")
    suspend fun deleteUserBoard(@Query("board_id") boardId: Int, @Query("user_id") userId: Int)


    //////////////////////////////////__TASKS__COLUMN__////////////////////////////////////////////


    @GET("/company/board/column")
    suspend fun getColumns(@Query("board_id") boardId: Int): ColumnsResponseModel

    @POST("/company/board/column")
    suspend fun createColumn(@Body request: CreateColumnRequestModel)

    @PUT("/company/board/column")
    suspend fun editColumn(@Body request: EditColumnRequestModel)

    @DELETE("/company/board/column")
    suspend fun deleteColumn(@Query("column_id") columnId: Int)


    //////////////////////////////////__TASKS__CARD__////////////////////////////////////////////


    @GET("/company/board/column/card/detail")
    suspend fun getDetailCard(@Query("card_id") cardId: Int): DetailCard

    @POST("/company/board/column/card")
    suspend fun createCard(@Body request: CreateCardRequestModel)

    @PUT("/company/board/column/card")
    suspend fun editCard(@Body request: EditCardRequestModel)

    @DELETE("/company/board/column/card")
    suspend fun deleteCard(@Query("card_id") cardId: Int)

    @POST("/company/board/column/card/users")
    suspend fun addUserCard(@Query("card_id") cardId: Int, @Query("user_id") userId: Int)

    @DELETE("/company/board/column/card/users")
    suspend fun deleteUserCard(@Query("card_id") cardId: Int, @Query("user_id") userId: Int)


    @GET("/invitations")
    suspend fun getInvitations(): InvitationsResponseModel

    @PUT("/invitations")
    suspend fun acceptInvitation(@Body request: AcceptInvitationRequestModel)


    //////////////////////////////////__CHATS__////////////////////////////////////////////


    @GET("/chats")
    suspend fun getChats(@Query("search") search: String?): ChatsResponseModel

    @GET("/chat/new")
    suspend fun startChat(@Query("user_id") userId: Int): Int

    @PUT("/chat/mute")
    suspend fun muteChat(@Body request: MuteChatRequestModel)

    @PUT("/chat/pin")
    suspend fun pinChat(@Body request: PinChatRequestModel)

    @DELETE("/chat/quit")
    suspend fun quitChat(@Query("chat_id") chatId: Int)


    //////////////////////////////////__IN_CHAT__////////////////////////////////////////////


    @GET("/chat/info")
    suspend fun getChatInfo(@Query("chat_id") chatId: Int): ChatInfoResponseModel

    @POST("/chat/users")
    suspend fun addUserChat(@Query("chat_id") chatId: Int, @Query("user_id") userId: Int)

    @DELETE("/chat/users")
    suspend fun deleteUserChat(@Query("chat_id") chatId: Int, @Query("user_id") userId: Int)

    @GET("/chat/messages")
    suspend fun getMessages(@Query("chat_id") chatId: Int): MessagesResponseModel

    @POST("/chat/message")
    suspend fun sendMessage(@Body request: MessageRequestModel)

    @POST("/chat/message/image")
    @Multipart
    suspend fun sendImageMessage(@Part body: MultipartBody.Part, @Query("chat_id") chatId: Int)

    @PUT("/chat/message")
    suspend fun editMessage(@Body request: MessageRequestModel)

    @DELETE("/chat/message")
    suspend fun deleteMessage(@Query("message_id") messageId: Int)
}