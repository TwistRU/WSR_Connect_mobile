package ru.fefu.wsr_connect_mobile.remote.models

import com.google.gson.annotations.SerializedName

data class Column(
    @SerializedName("column_id")
    var columnId: Int,

    @SerializedName("column_title")
    var columnTitle: String,

    @SerializedName("cards")
    var cards: List<Card>
)
