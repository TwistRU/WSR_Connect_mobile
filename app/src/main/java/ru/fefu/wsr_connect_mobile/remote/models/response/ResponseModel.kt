package ru.fefu.wsr_connect_mobile.remote.models.response

data class ResponseModel<T>(
    val success: Boolean,
    val data: T,
    val errors: List<String>,
)