package com.aam.viper4android.persistence.di

import android.content.Context
import androidx.room.Room
import com.aam.viper4android.persistence.ViPERDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideViperDatabase(@ApplicationContext context: Context): ViPERDatabase {
        return Room.databaseBuilder(context, ViPERDatabase::class.java, "ViPERDatabase")
            .build()
    }
}