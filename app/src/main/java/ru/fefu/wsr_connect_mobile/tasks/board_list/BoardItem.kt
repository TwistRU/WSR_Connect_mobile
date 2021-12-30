package ru.fefu.wsr_connect_mobile.tasks.board_list

import android.graphics.drawable.Drawable

data class BoardItem(
    val id: Int,
    val boardName: String,
    val boardCreateDate: String,
    val boardUserCount: String,
    val boardCreator: String,
    val boardImg: Drawable?,
    val available: Boolean,
)