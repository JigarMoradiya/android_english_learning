package com.example.myapplication.di

import com.example.myapplication.data.api.AuthApi
import com.example.myapplication.data.api.ContentApi
import com.example.myapplication.data.api.base.HeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HeaderInterceptor(
                    orgIdProvider = { "77609163-2e8a-4bfa-91ad-a1fa3cdc6a5b" },  // later replace with AppPreferences/org manager
                    authIdProvider = { null } // can be null before login
//                    authIdProvider = { AppPreferences.authId } // can be null before login
                )
            )
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()


    @Provides
    @Singleton
    @Named("Auth")
    fun provideAuthRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://v2.abacuspro.in/apis/application/v2/student/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @Named("Content")
    fun provideContentRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://content.example.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideAuthApi(@Named("Auth") retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideContentApi(@Named("Content") retrofit: Retrofit): ContentApi =
        retrofit.create(ContentApi::class.java)
}
