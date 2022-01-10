package ru.fefu.wsr_connect_mobile.remote.models.response

import com.google.gson.annotations.SerializedName
import ru.fefu.wsr_connect_mobile.remote.models.Invitation

data class InvitationsResponseModel(
    @SerializedName("invitations")
    var invitations: List<Invitation>,
)
