package com.example.projectobcane.communication.auth

import com.example.projectobcane.communication.CommunicationResult
import com.example.projectobcane.communication.IBaseRemoteRepository

interface IAuthRemoteRepository : IBaseRemoteRepository {
    suspend fun login(username: String, password: String): CommunicationResult<AuthResponse>
}