package com.mos1210.todoapp.data

import com.google.gson.JsonNull
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface FirebaseService {

    companion object {
        val instance: FirebaseService by lazy {
            val build = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(TodoRepository.BASE_URL)
                .build()
            build.create(FirebaseService::class.java)
        }
    }

    @GET("todo.json")
    fun getTodoList(): Call<JsonObject>

    @GET("todo/{key}.json")
    fun getTodo(@Path("key") key: String): Call<Todo>

    @POST("todo.json")
    fun createTodo(@Body todo: Todo): Call<JsonObject>

    @PUT("todo/{key}.json")
    fun updateTodo(@Path("key") key: String, @Body todo: Todo): Call<Todo>

    @DELETE("todo/{key}.json")
    fun deleteTodo(@Path("key") key: String): Call<JsonNull>

}