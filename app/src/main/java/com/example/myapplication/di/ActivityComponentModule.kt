package com.example.myapplication.di

import android.content.Context
import com.example.myapplication.utilities.TextToSpeechManager
import com.example.myapplication.utilities.pref.AppPreferencesHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object ActivityComponentModule {

    @Provides
    @ActivityScoped
    fun provideTextToSpeechManager(
        @ActivityContext context: Context,
        prefs: AppPreferencesHelper
    ): TextToSpeechManager {
        return TextToSpeechManager(context, prefs)
    }
}

