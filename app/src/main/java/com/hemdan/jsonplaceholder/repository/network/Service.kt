package com.hemdan.jsonplaceholder.repository.network

import com.hemdan.jsonplaceholder.repository.network.model.NetworkAddPost
import com.hemdan.jsonplaceholder.repository.network.model.NetworkPost
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface JSONPlaceholderService {
    @GET("posts")
    suspend fun getPosts(): List<NetworkPost>

    @PUT("posts/{id}")
    suspend fun updatePost(@Path("id")id: Int, @Body post: NetworkPost): NetworkPost

    @POST("posts")
    suspend fun addPost(@Body post: NetworkAddPost): NetworkPost

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: Int)
}
const val BASE_URL = "https://jsonplaceholder.typicode.com/"
object Network {

    val intercepter = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BASIC
    }

    val client = OkHttpClient.Builder().apply {
        this.addInterceptor(intercepter)
            // time out setting
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(20,TimeUnit.SECONDS)
            .writeTimeout(25,TimeUnit.SECONDS)

    }.build()
    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(client)
        .build()

    val jsonPlaceHolder = retrofit.create(JSONPlaceholderService::class.java)
}