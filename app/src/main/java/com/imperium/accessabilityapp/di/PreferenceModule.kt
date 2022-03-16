package com.imperium.weatherapplication.di

import android.content.Context
import android.content.SharedPreferences
import com.imperium.accessabilityapp.Uils.PREFERENCE_NAME
import com.imperium.accessabilityapp.Uils.SharedPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object PreferenceModule {
    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context) =
        context.getSharedPreferences(
            PREFERENCE_NAME, Context.MODE_PRIVATE
        )

    @Singleton
    @Provides
    fun provideSharedPreference(preferences: SharedPreferences) =
        SharedPreference(preferences)
}