package com.rk.movies.di

import android.content.Context
import com.rk.movies.util.GlobalClass
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
    fun provideGlobalClass(@ApplicationContext context: Context): GlobalClass {
        return GlobalClass.getInstance(context)
    }
}
