package ru.fefu.wsr_connect_mobile.remote.models

import com.google.gson.annotations.SerializedName

data class Card(
    @SerializedName("card_id")
    var cardId: Int,

    @SerializedName("card_title")
    var cardTitle: String,

    @SerializedName("card_short_desc")
    var cardShortDesc: String,

    @SerializedName("card_long_desc")
    var cardLongDesc: String,

    @SerializedName("card_crete_date")
    var cardCreateDate: String,

    @SerializedName("card_creator")
    var cardCreator: String,
)
