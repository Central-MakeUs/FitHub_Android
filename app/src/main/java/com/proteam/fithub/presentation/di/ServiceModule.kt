package com.proteam.fithub.presentation.di

import com.proteam.fithub.data.remote.service.SignInService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    @Singleton
    fun provideSignInService(retrofit : Retrofit) : SignInService = retrofit.create(SignInService::class.java)
}