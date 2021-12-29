package ru.fefu.wsr_connect_mobile.tasks.invitation_list

import android.graphics.drawable.Drawable

data class InvitationItem(
    val id: Int,
    val companyName: String,
    val invitationBody: String,
    val companyImg: Drawable?,
)