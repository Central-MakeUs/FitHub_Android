package com.proteam.fithub.presentation.di

import com.proteam.fithub.data.remote.repository.ArticleRepositoryImpl
import com.proteam.fithub.data.remote.repository.CertificateRepositoryImpl
import com.proteam.fithub.data.remote.repository.CommentRepositoryImpl
import com.proteam.fithub.data.remote.repository.ExerciseRepositoryImpl
import com.proteam.fithub.data.remote.repository.SignInRepositoryImpl
import com.proteam.fithub.data.remote.repository.SignUpRepositoryImpl
import com.proteam.fithub.domain.repository.ArticleRepository
import com.proteam.fithub.domain.repository.CertificateRepository
import com.proteam.fithub.domain.repository.CommentRepository
import com.proteam.fithub.domain.repository.ExerciseRepository
import com.proteam.fithub.domain.repository.SignInRepository
import com.proteam.fithub.domain.repository.SignUpRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindSignInRepository(signInRepositoryImpl: SignInRepositoryImpl) : SignInRepository

    @Binds
    abstract fun bindSignUpRepository(signUpRepositoryImpl: SignUpRepositoryImpl) : SignUpRepository

    @Binds
    abstract fun bindExerciseRepository(exerciseRepositoryImpl: ExerciseRepositoryImpl) : ExerciseRepository

    @Binds
    abstract fun bindCertificateRepository(certificateRepositoryImpl: CertificateRepositoryImpl) : CertificateRepository

    @Binds
    abstract fun bindCommentRepository(commentRepositoryImpl: CommentRepositoryImpl) : CommentRepository

    @Binds
    abstract fun bindArticleRepository(articleRepositoryImpl: ArticleRepositoryImpl) : ArticleRepository
}