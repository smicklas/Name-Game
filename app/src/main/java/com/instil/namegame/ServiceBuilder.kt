package com.instil.namegame

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.converter.moshi.MoshiConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET


object ServiceBuilder {
    const val BASE_URL = "https://namegame.willowtreeapps.com/api/v1.0/"

    /* Moshi Makes it easy to parse JSON into objects
        you can use GSON instead if you want*/
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    //retrofit instance
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .build()

//Then we are going to create the interface which is going to help to handle our GET method to call the API

    interface NameGameAPIService{
        @GET("profiles")
        fun getProperty():
                Deferred<List<Profile>>
    }

/* Singleton to create this instance only once
and get it ready every time we call it. */

    object NameGameAPI {
        val retrofitService : NameGameAPIService by lazy {
            retrofit.create(NameGameAPIService::class.java)
        }
    }
}