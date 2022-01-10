package ru.fefu.wsr_connect_mobile.remote

import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.fefu.wsr_connect_mobile.remote.models.request.*
import ru.fefu.wsr_connect_mobile.remote.models.response.*


class ApiService {
    private val api = NetworkService().retrofit.create(Api::class.java)

    suspend fun registration(
        username: String,
        email: String,
        password: String,
    ): Flow<Result<RegistrationResponseModel>> =
        flow<Result<RegistrationResponseModel>> {
            emit(
                Result.Success(
                    api.registration(RegistrationRequestModel(username, email, password))
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


    suspend fun getUserInfo(): Flow<Result<UserResponseModel>> =
        flow<Result<UserResponseModel>> {
            emit(Result.Success(api.getUserInfo()))
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
                    api.changePassword(
                        ChangePasswordRequestModel(
                            oldPassword, newPassword
                        )
                    )
                )
            )
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun changeUserAvatar(
        avatar: String?,
    ): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(Result.Success(api.changeUserAvatar(ChangeUserAvatarRequestModel(avatar))))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun getChats(): Flow<Result<ChatsResponseModel>> =
        flow<Result<ChatsResponseModel>> {
            emit(Result.Success(api.getChats()))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun createCompany(
        companyName: String
    ): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(Result.Success(api.createCompany(CreateCompanyRequestModel(companyName))))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun createBoard(
        boardName: String
    ): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(Result.Success(api.createBoard(CreateBoardRequestModel(boardName))))
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
        boardId: Int,
        columnId: Int,
        columnName: String
    ): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(
                Result.Success(
                    api.editColumn(
                        EditColumnRequestModel(
                            boardId,
                            columnId,
                            columnName
                        )
                    )
                )
            )
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun deleteColumn(
        boardId: Int,
        columnId: Int
    ): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(Result.Success(api.deleteColumn(boardId, columnId)))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun createCard(
        boardId: Int,
        columnId: Int,
        cardTitle: String
    ): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(
                Result.Success(
                    api.createCard(
                        CreateCardRequestModel(
                            boardId,
                            columnId,
                            cardTitle
                        )
                    )
                )
            )
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun editCard(
        boardId: Int,
        columnId: Int,
        cardId: Int,
        cardTitle: String,
        cardShortDesc: String,
        cardLongDesc: String,
    ): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(
                Result.Success(
                    api.editCard(
                        EditCardRequestModel(
                            boardId,
                            columnId,
                            cardId,
                            cardTitle,
                            cardShortDesc,
                            cardLongDesc
                        )
                    )
                )
            )
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun deleteCard(
        boardId: Int,
        columnId: Int,
        cardId: Int
    ): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(Result.Success(api.deleteCard(boardId, columnId, cardId)))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun getInvitations(): Flow<Result<InvitationsResponseModel>> =
        flow<Result<InvitationsResponseModel>> {
            emit(Result.Success(api.getInvitations()))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)


    suspend fun acceptInvitation(companyId: Int): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(Result.Success(api.acceptInvitation(AcceptInvitationRequestModel(companyId))))
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


    suspend fun getCompanyUsers(
        context: String,
        companyId: Int?,
        boardId: Int?,
    ): Flow<Result<CompanyUsersResponseModel>> =
        flow<Result<CompanyUsersResponseModel>> {
            emit(Result.Success(api.getCompanyUsers(context, companyId, boardId)))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)

    suspend fun sendInvite(
        context: String,
        boardId: Int?,
        userNickname: String,
        inviteText: String
    ): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(
                Result.Success(
                    api.sendInvite(
                        SendInviteRequestModel(
                            context,
                            boardId,
                            userNickname,
                            inviteText
                        )
                    )
                )
            )
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)

    suspend fun deleteUser(context: String, boardId: Int?, userId: Int): Flow<Result<Unit>> =
        flow<Result<Unit>> {
            emit(Result.Success(api.deleteUser(context, boardId, userId)))
        }
            .catch { emit(Result.Error(it)) }
            .flowOn(Dispatchers.IO)
}