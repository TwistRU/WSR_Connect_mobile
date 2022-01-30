package ru.fefu.wsr_connect_mobile.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import ru.fefu.wsr_connect_mobile.remote.models.*


class ApiService {
    private val api = NetworkService().retrofit.create(Api::class.java)

    suspend fun registration(
        username: String,
        firstName: String,
        lastName: String,
        email: String,
        password: String,
    ): Flow<Result<RegistrationResponseModel>> =
        flow<Result<RegistrationResponseModel>> {
            emit(
                Result.Success(
                    api.registration(
                        RegistrationRequestModel(
                            username,
                            firstName,
                            lastName,
                            email,
                            password
                        )
                    )
                )
            )
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun login(
        username: String,
        password: String
    ): Flow<Result<LoginResponseModel>> =
        flow<Result<LoginResponseModel>> {
            emit(Result.Success(api.login(LoginRequestModel(username, password))))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun logout(): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(Result.Success(api.logout()))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun searchUserApp(search: String): Flow<Result<UsersResponseModel>> =
        flow<Result<UsersResponseModel>> {
            emit(Result.Success(api.searchUserApp(search)))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun getUserInfo(): Flow<Result<User>> =
        flow<Result<User>> {
            emit(Result.Success(api.getUserInfo()))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)

    suspend fun getCompanyUserInfo(userId: Int): Flow<Result<User>> =
        flow<Result<User>> {
            emit(Result.Success(api.getCompanyUserInfo(userId)))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun changeUserInfo(
        firstName: String?,
        lastName: String?,
        email: String?,
        aboutMe: String?,
    ): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(
                Result.Success(
                    api.changeUserInfo(
                        ChangeUserInfoRequestModel(
                            firstName,
                            lastName,
                            email,
                            aboutMe
                        )
                    )
                )
            )
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun changePassword(
        oldPassword: String?,
        newPassword: String?,
    ): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(
                Result.Success(
                    api.changeUserPassword(
                        ChangeUserPasswordRequestModel(
                            oldPassword, newPassword
                        )
                    )
                )
            )
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun sendProfileImage(body: MultipartBody.Part): Flow<Result<ImageResponseModel>> =
        flow<Result<ImageResponseModel>> {
            emit(Result.Success(api.sendProfileImage(body)))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun getChats(search: String?): Flow<Result<ChatsResponseModel>> =
        flow<Result<ChatsResponseModel>> {
            emit(Result.Success(api.getChats(search)))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun createCompany(
        body: MultipartBody.Part?,
        companyName: String
    ): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            if (body !== null)
                emit(Result.Success(api.createCompanyWithImage(body, companyName)))
            else
                emit(Result.Success(api.createCompany(companyName)))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun getCompanyInfo(): Flow<Result<CompanyInfoResponseModel>> =
        flow<Result<CompanyInfoResponseModel>> {
            emit(Result.Success(api.getCompanyInfo()))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun createBoard(
        body: MultipartBody.Part?,
        boardName: String
    ): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            if (body != null)
                emit(Result.Success(api.createBoardWithImage(body, boardName)))
            else
                emit(Result.Success(api.createBoard(boardName)))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun editBoard(
        boardId: Int,
        boardName: String
    ): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(Result.Success(api.editBoard(EditBoardRequestModel(boardId, boardName))))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun deleteBoard(
        boardId: Int
    ): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(Result.Success(api.deleteBoard(boardId)))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun createColumn(
        boardId: Int,
        columnName: String
    ): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(Result.Success(api.createColumn(CreateColumnRequestModel(boardId, columnName))))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun editColumn(
        columnId: Int,
        columnTitle: String
    ): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(Result.Success(api.editColumn(EditColumnRequestModel(columnId, columnTitle))))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun deleteColumn(
        columnId: Int
    ): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(Result.Success(api.deleteColumn(columnId)))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun createCard(
        boardId: Int,
        columnId: Int,
        cardTitle: String,
        cardShortDesc: String,
        cardLongDesc: String?,
        deadline: String?
    ): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(
                Result.Success(
                    api.createCard(
                        CreateCardRequestModel(
                            boardId,
                            columnId,
                            cardTitle,
                            cardShortDesc,
                            cardLongDesc,
                            deadline
                        )
                    )
                )
            )
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun getDetailCard(cardId: Int): Flow<Result<DetailCard>> =
        flow<Result<DetailCard>> {
            emit(Result.Success(api.getDetailCard(cardId)))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun editCard(
        cardId: Int,
        cardTitle: String,
        deadline: String?,
        cardShortDesc: String,
        cardLongDesc: String?
    ): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(
                Result.Success(
                    api.editCard(
                        EditCardRequestModel(
                            cardId,
                            cardTitle,
                            deadline,
                            cardShortDesc,
                            cardLongDesc,
                        )
                    )
                )
            )
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun deleteCard(cardId: Int): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(Result.Success(api.deleteCard(cardId)))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun getInvitations(): Flow<Result<InvitationsResponseModel>> =
        flow<Result<InvitationsResponseModel>> {
            emit(Result.Success(api.getInvitations()))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun acceptInvitation(inviteId: Int): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(
                Result.Success(
                    api.acceptInvitation(
                        AcceptInvitationRequestModel(
                            inviteId,
                        )
                    )
                )
            )
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun getBoards(): Flow<Result<BoardsResponseModel>> =
        flow<Result<BoardsResponseModel>> {
            emit(Result.Success(api.getBoards()))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun getColumns(boardId: Int): Flow<Result<ColumnsResponseModel>> =
        flow<Result<ColumnsResponseModel>> {
            emit(Result.Success(api.getColumns(boardId)))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun searchUserCompany(search: String?): Flow<Result<UsersResponseModel>> =
        flow<Result<UsersResponseModel>> {
            emit(Result.Success(api.searchUserCompany(search)))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun searchUserBoard(boardId: Int, search: String?): Flow<Result<UsersResponseModel>> =
        flow<Result<UsersResponseModel>> {
            emit(Result.Success(api.searchUserBoard(boardId, search)))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun sendCompanyInvite(userId: Int, inviteText: String): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(
                Result.Success(
                    api.sendCompanyInvite(
                        SendCompanyInviteRequestModel(
                            userId,
                            inviteText
                        )
                    )
                )
            )
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun deleteUserCompany(userId: Int): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(Result.Success(api.deleteUserCompany(userId)))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun deleteUserBoard(boardId: Int, userId: Int): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(Result.Success(api.deleteUserBoard(boardId, userId)))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun muteChat(chatId: Int, status: Boolean): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(Result.Success(api.muteChat(MuteChatRequestModel(chatId, status))))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun pinChat(chatId: Int, status: Boolean): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(Result.Success(api.pinChat(PinChatRequestModel(chatId, status))))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun quitChat(chatId: Int): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(Result.Success(api.quitChat(chatId)))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun sendMessage(
        chatId: Int,
        repliedMessageId: Int?,
        messageBody: String
    ): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(
                Result.Success(
                    api.sendMessage(
                        MessageRequestModel(
                            chatId,
                            null,
                            repliedMessageId,
                            messageBody
                        )
                    )
                )
            )
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun editMessage(
        chatId: Int,
        messageId: Int,
        messageBody: String
    ): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(
                Result.Success(
                    api.editMessage(
                        MessageRequestModel(
                            chatId,
                            messageId,
                            null,
                            messageBody
                        )
                    )
                )
            )
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun deleteMessage(messageId: Int): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(Result.Success(api.deleteMessage(messageId)))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun sendImageMessage(body: MultipartBody.Part, chatId: Int): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(Result.Success(api.sendImageMessage(body, chatId)))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun getMessages(chatId: Int): Flow<Result<MessagesResponseModel>> =
        flow<Result<MessagesResponseModel>> {
            emit(Result.Success(api.getMessages(chatId)))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun getChatInfo(chatId: Int): Flow<Result<ChatInfoResponseModel>> =
        flow<Result<ChatInfoResponseModel>> {
            emit(Result.Success(api.getChatInfo(chatId)))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun getChatId(userId: Int): Flow<Result<Int>> =
        flow<Result<Int>> {
            emit(Result.Success(api.startChat(userId)))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)
}