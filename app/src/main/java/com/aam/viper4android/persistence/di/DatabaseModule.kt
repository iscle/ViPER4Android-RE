package com.aam.viper4android.persistence.di

import android.content.Context
import androidx.room.Room
import com.aam.viper4android.persistence.ViPERDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideViperDatabase(@ApplicationContext context: Context): ViPERDatabase {
        return Room.databaseBuilder(context, ViPERDatabase::class.java, "ViPERDatabase")
            .build()
    }

    @Provides
    fun provideSettingsDao(viperDatabase: ViPERDatabase) = viperDatabase.settingsDao()

    @Provides
    fun provideSessionsDao(viperDatabase: ViPERDatabase) = viperDatabase.sessionsDao()

    @Provides
    fun providePresetsDao(viperDatabase: ViPERDatabase) = viperDatabase.presetsDao()
}