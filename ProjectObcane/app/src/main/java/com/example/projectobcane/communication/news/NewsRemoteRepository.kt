package com.example.projectobcane.communication.news

import com.example.projectobcane.communication.CommunicationResult
import javax.inject.Inject

class NewsRemoteRepositoryImpl @Inject constructor(
    private val api: NewsApi
) : INewsRemoteRepository {

    override suspend fun getAllNews(): CommunicationResult<NewsResponse> {
        return processResponse { api.getAllNews() }
    }
}