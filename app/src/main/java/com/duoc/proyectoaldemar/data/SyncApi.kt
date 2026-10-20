package com.duoc.proyectoaldemar.data

import com.duoc.proyectoaldemar.model.Muestra
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface SyncApi {
    @POST("posts")
    suspend fun enviar(@Body muestra: Muestra): Any
}

fun crearSyncApi(): SyncApi = Retrofit.Builder()
    .baseUrl("https://jsonplaceholder.typicode.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(SyncApi::class.java)