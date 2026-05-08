package com.example.projectobcane.communication.auth

import android.util.Log
import com.example.projectobcane.BuildConfig
import com.example.projectobcane.communication.CommunicationResult
import javax.inject.Inject

class AuthRemoteRepositoryImpl @Inject constructor(private val api: AuthApi) : IAuthRemoteRepository {

    override suspend fun login(username: String, password: String): CommunicationResult<AuthResponse> {
        Log.d("AUTH", "Attempting login for user: $username")

        return processResponse {
            api.login(
                grantType = "password",
                clientId = BuildConfig.AUTH_CLIENT_ID,
                username = username,
                password = password
            )
        }.also { result ->
            when (result) {
                is CommunicationResult.Success ->
                    Log.d("AUTH", "Login SUCCESS, token starts with: ${result.data.accessToken.take(20)}...")
                is CommunicationResult.Error ->
                    Log.e("AUTH", "Login ERROR ${result.error.code}: ${result.error.message}")
                is CommunicationResult.ConnectionError ->
                    Log.e("AUTH", "Login CONNECTION ERROR")
                is CommunicationResult.Exception ->
                    Log.e("AUTH", "Login EXCEPTION: ${result.exception.message}", result.exception)
            }
        }
    }
}