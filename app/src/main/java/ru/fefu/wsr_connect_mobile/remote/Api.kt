package ru.fefu.wsr_connect_mobile.remote

import retrofit2.http.*
import ru.fefu.wsr_connect_mobile.remote.models.request.*
import ru.fefu.wsr_connect_mobile.remote.models.response.*


interface Api {


    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequestModel): LoginResponseModel

    @POST("/auth/registration")
    suspend fun registration(@Body request: RegistrationRequestModel): RegistrationResponseModel


    @GET("/user/info")
    suspend fun getUserInfo(): UserResponseModel

    @POST("/user/info")
    suspend fun changeUserInfo(@Body request: ChangeUserInfoRequestModel)

    @POST("/user/info/avatar")
    suspend fun changeUserAvatar(@Body request: ChangeUserAvatarRequestModel)

    @POST("/user/info/password")
    suspend fun changePassword(@Body request: ChangePasswordRequestModel)


    @POST("/tasks/create_company")
    suspend fun createCompany(@Body request: CreateCompanyRequestModel)

    @POST("/tasks/create_board")
    suspend fun createBoard(@Body request: CreateBoardRequestModel)

    @PUT("/tasks/edit_board")
    suspend fun editBoard(@Body request: EditBoardRequestModel)

    @DELETE("/tasks/delete_board")
    suspend fun deleteBoard(@Query("board_id") boardId: Int)

    @POST("/tasks/create_column")
    suspend fun createColumn(@Body request: CreateColumnRequestModel)

    @PUT("/tasks/edit_column")
    suspend fun editColumn(@Body request: EditColumnRequestModel)

    @DELETE("/tasks/delete_column")
    suspend fun deleteColumn(@Query("board_id") boardId: Int, @Query("column_id") columnId: Int)


    @POST("/tasks/create_card")
    suspend fun createCard(@Body request: CreateCardRequestModel)

    @PUT("/tasks/edit_card")
    suspend fun editCard(@Body request: EditCardRequestModel)

    @DELETE("/tasks/delete_card")
    suspend fun deleteCard(
        @Query("board_id") boardId: Int,
        @Query("column_id") columnId: Int,
        @Query("card_id") cardId: Int,
    )

    @GET("/tasks/users")
    suspend fun getCompanyUsers(
        @Query("context") context: String,
        @Query("company_id") companyId: Int?,
        @Query("board_id") boardId: Int?
    ): CompanyUsersResponseModel

    @POST("/tasks/invite")
    suspend fun sendInvite(@Body request: SendInviteRequestModel)

    @DELETE("/tasks/delete_user")
    suspend fun deleteUser(
        @Query("context") context: String,
        @Query("board_id") boardId: Int?,
        @Query("user_id") userId: Int,
    )

    @GET("/tasks/invitations")
    suspend fun getInvitations(): InvitationsResponseModel

    @POST("/tasks/invitations")
    suspend fun acceptInvitation(@Body request: AcceptInvitationRequestModel)

    @GET("/tasks/boards")
    suspend fun getBoards(): BoardsResponseModel

    @GET("/tasks/columns")
    suspend fun getColumns(@Query("board_id") boardId: Int): ColumnsResponseModel


    ////////////////////chats////////////////////////////////////////////////////////////
    @GET("/user/chats")
    suspend fun getChats(): ChatsResponseModel
}