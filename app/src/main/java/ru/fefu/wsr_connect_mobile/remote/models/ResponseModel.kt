package ru.fefu.wsr_connect_mobile.remote.models

data class ResponseModel<T>(
    val success: Boolean,
    val data: T,
    val errors: List<String>,
)