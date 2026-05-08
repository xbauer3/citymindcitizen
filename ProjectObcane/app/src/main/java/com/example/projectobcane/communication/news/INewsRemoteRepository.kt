package com.example.projectobcane.communication.news

import com.example.projectobcane.communication.CommunicationResult
import com.example.projectobcane.communication.IBaseRemoteRepository

interface INewsRemoteRepository : IBaseRemoteRepository {
    suspend fun getAllNews(): CommunicationResult<NewsResponse>
}

