package com.proteam.fithub.data.remote.service

import com.proteam.fithub.data.remote.response.ResponseArticleData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookmarkService {

    @GET("home/book-mark/{categoryId}")
    suspend fun requestBookmarkData(
        @Path("categoryId") categoryId : Int,
        @Query("pageIndex") pageIndex : Int
    ) : ResponseArticleData
}