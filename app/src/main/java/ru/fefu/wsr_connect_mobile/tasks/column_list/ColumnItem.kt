package ru.fefu.wsr_connect_mobile.tasks.column_list


data class ColumnItem(
    val id: Int,
    val columnTitle: String,
    val cardList: List<CardItem>
)

data class CardItem(
    val id: Int,
    val cardTitle: String,
    val cardShortDesc: String,
    val cardLongDesc: String,
    val cardCreateDate: String,
    val cardCreator: String,
)