package com.proteam.fithub.presentation.di

import com.proteam.fithub.data.remote.repository.SignInRepositoryImpl
import com.proteam.fithub.domain.repository.SignInRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindSignInRepository(signInRepositoryImpl: SignInRepositoryImpl) : SignInRepository
}