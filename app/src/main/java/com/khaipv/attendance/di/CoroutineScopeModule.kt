package com.khaipv.attendance.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoroutineScopeModule {

    @CoroutineScopeIO
    @Provides
    @Singleton
    fun provideCoroutineScopeIO(): CoroutineScope = CoroutineScope(Dispatchers.IO)

    @CoroutineScopeMain
    @Provides
    @Singleton
    fun provideCoroutineScopeMain(): CoroutineScope = CoroutineScope(Dispatchers.Main)

    @CoroutineScopeDefault
    @Provides
    @Singleton
    fun provideCoroutineScopeDefault(): CoroutineScope = CoroutineScope(Dispatchers.Default)

}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class CoroutineScopeIO

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class CoroutineScopeMain

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class CoroutineScopeDefault
