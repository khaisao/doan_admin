package com.khaipv.attendance.network

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.khaipv.attendance.util.NetworkEvent
import com.khaipv.attendance.util.NetworkState
import com.khaipv.attendance.util.isNetworkAvailable
import com.google.android.gms.common.api.ApiException
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Response
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.nio.charset.Charset
import javax.inject.Inject

class NetworkEventInterceptor @Inject constructor(
    private val networkEvent: NetworkEvent,
    private val context: Context,
    private val gson: Gson
) : Interceptor {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (!context.isNetworkAvailable()) {
            networkEvent.publish(NetworkState.NO_INTERNET)
            throw NetworkException()
        } else {
            try {
                val response = chain.proceed(request)
                val responseBody = response.body
                val source = responseBody?.source()
                source?.request(Long.MAX_VALUE)
                val buffer = source?.buffer()
                val responseBodyString = buffer?.clone()?.readString(Charset.forName("UTF-8"))
                when (response.code) {
                    in 200..299 -> {
                    }
                    HttpURLConnection.HTTP_UNAVAILABLE -> networkEvent.publish(NetworkState.SERVER_NOT_AVAILABLE) // 503
                    HttpURLConnection.HTTP_UNAUTHORIZED -> { } //   401
                    HttpURLConnection.HTTP_NOT_FOUND -> networkEvent.publish(NetworkState.NOT_FOUND) // 404
                    HttpURLConnection.HTTP_FORBIDDEN -> networkEvent.publish(NetworkState.FORBIDDEN) // 403
                    HttpURLConnection.HTTP_BAD_REQUEST -> networkEvent.publish(NetworkState.BAD_REQUEST) // 400
                    else -> {
                        networkEvent.publish(
                            NetworkState.GENERIC(
                                gson.fromJson(responseBodyString, ApiException::class.java)
                            )
                        )
                    }
                }
                return response
            } catch (e: SocketTimeoutException) {
                networkEvent.publish(NetworkState.CONNECTION_LOST)
            } catch (e: Exception) {
                if ("Canceled" != e.message) {
                    networkEvent.publish(NetworkState.ERROR)
                }
            }
            return chain.proceed(request)
        }
    }
}
