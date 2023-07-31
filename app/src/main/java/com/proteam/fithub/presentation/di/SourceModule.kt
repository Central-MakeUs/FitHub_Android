package com.proteam.fithub.presentation.di

import com.proteam.fithub.data.remote.source.ArticleRemoteSource
import com.proteam.fithub.data.remote.source.CertificateRemoteSource
import com.proteam.fithub.data.remote.source.CommentRemoteSource
import com.proteam.fithub.data.remote.source.ExerciseRemoteSource
import com.proteam.fithub.data.remote.source.HomeRemoteSource
import com.proteam.fithub.data.remote.source.SearchRemoteSource
import com.proteam.fithub.data.remote.source.SignInRemoteSource
import com.proteam.fithub.data.remote.source.SignUpRemoteSource
import com.proteam.fithub.domain.source.ArticleSource
import com.proteam.fithub.domain.source.CertificateSource
import com.proteam.fithub.domain.source.CommentSource
import com.proteam.fithub.domain.source.ExerciseSource
import com.proteam.fithub.domain.source.HomeSource
import com.proteam.fithub.domain.source.SearchSource
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

    @Binds
    abstract fun bindExerciseRemoteSource(exerciseRemoteSource: ExerciseRemoteSource) : ExerciseSource

    @Binds
    abstract fun bindCertificateRemoteSource(certificateRemoteSource: CertificateRemoteSource) : CertificateSource

    @Binds
    abstract fun bindCommentRemoteSource(commentRemoteSource: CommentRemoteSource) : CommentSource

    @Binds
    abstract fun bindArticleRemoteSource(articleRemoteSource: ArticleRemoteSource) : ArticleSource

    @Binds
    abstract fun bindSearchRemoteSource(searchRemoteSource: SearchRemoteSource) : SearchSource

    @Binds
    abstract fun bindHomeRemoteSource(homeRemoteSource: HomeRemoteSource) : HomeSource
}