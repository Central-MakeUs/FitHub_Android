package com.proteam.fithub.presentation.di

import com.proteam.fithub.data.remote.source.SignInRemoteSource
import com.proteam.fithub.data.remote.source.SignUpRemoteSource
import com.proteam.fithub.domain.source.SignInSource
import com.proteam.fithub.domain.source.SignUpSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SourceModule {

    @Binds
    abstract fun bindSignInRemoteSource(signInRemoteSource: SignInRemoteSource) : SignInSource

    @Binds
    abstract fun bindSignUpRemoteSource(signUpRemoteSource: SignUpRemoteSource) : SignUpSource
}