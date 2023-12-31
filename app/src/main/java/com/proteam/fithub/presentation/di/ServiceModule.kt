package com.proteam.fithub.presentation.di

import com.proteam.fithub.data.remote.service.AlarmService
import com.proteam.fithub.data.remote.service.ArticleService
import com.proteam.fithub.data.remote.service.BookmarkService
import com.proteam.fithub.data.remote.service.CertificateService
import com.proteam.fithub.data.remote.service.CommentService
import com.proteam.fithub.data.remote.service.ExerciseService
import com.proteam.fithub.data.remote.service.HomeService
import com.proteam.fithub.data.remote.service.LocationService
import com.proteam.fithub.data.remote.service.MyPageService
import com.proteam.fithub.data.remote.service.RecordService
import com.proteam.fithub.data.remote.service.ReportService
import com.proteam.fithub.data.remote.service.SearchService
import com.proteam.fithub.data.remote.service.SignInService
import com.proteam.fithub.data.remote.service.SignUpService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Provides
    @Singleton
    fun provideSignInService(retrofit : Retrofit) : SignInService = retrofit.create(SignInService::class.java)

    @Provides
    @Singleton
    fun provideSignUpService(retrofit: Retrofit) : SignUpService = retrofit.create(SignUpService::class.java)

    @Provides
    @Singleton
    fun provideExerciseService(retrofit: Retrofit) : ExerciseService = retrofit.create(ExerciseService::class.java)

    @Provides
    @Singleton
    fun provideCertificateService(retrofit: Retrofit) : CertificateService = retrofit.create(CertificateService::class.java)
    @Provides
    @Singleton
    fun provideRecordService(retrofit: Retrofit) : RecordService = retrofit.create(RecordService::class.java)

    @Provides
    @Singleton
    fun provideCommentService(retrofit: Retrofit) : CommentService = retrofit.create(CommentService::class.java)

    @Provides
    @Singleton
    fun provideArticleService(retrofit: Retrofit) : ArticleService = retrofit.create(ArticleService::class.java)

    @Provides
    @Singleton
    fun provideSearchService(retrofit: Retrofit) : SearchService = retrofit.create(SearchService::class.java)

    @Provides
    @Singleton
    fun provideHomeService(retrofit: Retrofit) : HomeService = retrofit.create(HomeService::class.java)

    @Provides
    @Singleton
    fun provideReportService(retrofit: Retrofit) : ReportService = retrofit.create(ReportService::class.java)

    @Provides
    @Singleton
    fun provideBookmarkService(retrofit: Retrofit) : BookmarkService = retrofit.create(BookmarkService::class.java)

    @Provides
    @Singleton
    fun provideMyPageService(retrofit: Retrofit) : MyPageService = retrofit.create(MyPageService::class.java)

    @Provides
    @Singleton
    fun provideAlarmService(retrofit : Retrofit) : AlarmService = retrofit.create(AlarmService::class.java)

    @Provides
    @Singleton
    fun provideLocationService(retrofit: Retrofit) : LocationService = retrofit.create(LocationService::class.java)
}