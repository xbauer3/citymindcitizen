package com.example.projectobcane.communication.news

import retrofit2.Response
import retrofit2.http.GET

interface NewsApi {
    @GET("v1/newsfeed/news/all/")
    suspend fun getAllNews(): Response<NewsResponse>
}