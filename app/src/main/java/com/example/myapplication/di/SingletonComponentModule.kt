package com.example.myapplication.di

import android.app.Application
import android.content.Context
import com.example.myapplication.utilities.pref.AppPreferencesHelper
import com.example.myapplication.utils.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context = application

    @Singleton
    @Provides
    fun providePreferencesHelper(@ApplicationContext context: Context) =
        AppPreferencesHelper(context, AppConstants.PREF_NAME)

    // SessionRepository and ModuleProgressRepository use @Inject constructor + @Singleton
    // so Hilt wires them automatically — no @Provides needed here
}
