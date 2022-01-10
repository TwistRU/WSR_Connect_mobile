package ru.fefu.wsr_connect_mobile.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.fefu.wsr_connect_mobile.App
import ru.fefu.wsr_connect_mobile.remote.interceptors.AuthInterceptor
import java.util.concurrent.TimeUnit


class NetworkService {

    private val httpClient =
        OkHttpClient.Builder()
            .connectTimeout(10L, TimeUnit.SECONDS)
            .readTimeout(10L, TimeUnit.SECONDS)
            .writeTimeout(10L, TimeUnit.SECONDS)
            .callTimeout(10L, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            })
            .addInterceptor(AuthInterceptor(App.sharedPreferences))
            .build()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://wsr-connect-api.herokuapp.com")
//            .baseUrl("https://f383-5-143-9-247.ngrok.io")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

sealed class Result<T> {
    class Success<T>(val result: T) : Result<T>()
    class Error<T>(val e: Throwable) : Result<T>()
}