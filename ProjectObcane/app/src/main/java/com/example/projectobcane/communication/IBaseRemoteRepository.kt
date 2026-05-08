package com.example.projectobcane.communication

import retrofit2.Response
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException




interface IBaseRemoteRepository {

    suspend fun <T: Any> processResponse(
        request: suspend () -> Response<T>
    ): CommunicationResult<T> {
        try {
            val call = request()
            return when {
                call.isSuccessful -> {
                    val body = call.body()
                    if (body != null) {
                        CommunicationResult.Success(body)
                    } else {
                        // 201 Created or 204 No Content with empty body — still success
                        @Suppress("UNCHECKED_CAST")
                        CommunicationResult.Success(Unit as T)
                    }
                }
                else -> CommunicationResult.Error(
                    CommunicationError(
                        code = call.code(),
                        message = call.errorBody()?.string()
                    )
                )
            }
        } catch (unknownHostException: UnknownHostException) {
            return CommunicationResult.ConnectionError()
        } catch (socketEx: SocketTimeoutException){
            return CommunicationResult.ConnectionError()
        } catch (interruptedEx: InterruptedIOException){
            return CommunicationResult.ConnectionError()
        }
        catch (exception: Exception){
            return CommunicationResult.Exception(exception)
        }




    }
}
