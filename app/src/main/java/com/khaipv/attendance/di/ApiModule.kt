package com.khaipv.attendance.di

import android.content.Context
import android.text.TextUtils
import com.khaipv.attendance.BuildConfig
import com.khaipv.attendance.network.ApiInterface
import com.khaipv.attendance.network.NetworkEventInterceptor
import com.khaipv.attendance.network.TokenAuthenticator
import com.khaipv.attendance.util.NetworkEvent
import com.example.core.pref.RxPreferences
import com.example.core.utils.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.lang.reflect.Type
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().setLenient().create()
    }

    @Provides
    @Singleton
    fun provideApiInterface(gson: Gson, client: OkHttpClient):
            ApiInterface {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(ApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideHttpClient(
        cache: Cache?,
        rxPreferences: RxPreferences,
        networkEventInterceptor: NetworkEventInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .sslSocketFactory(
                provideSSLSocketFactory()!!,
                provideUnTrustManager()[0] as X509TrustManager
            )
            .cache(cache)
            .addInterceptor(
                Interceptor { chain: Interceptor.Chain ->
                    val request = if (!TextUtils.isEmpty(rxPreferences.getToken().toString())) {
                        chain.request()
                            .newBuilder()
                            .header("Accept", "application/json")
                            .addHeader(
                                "Authorization",
                                "Bearer ${rxPreferences.getToken().toString()}"
                            )
                            .build()
                    } else {
                        chain.request()
                            .newBuilder()
                            .header("Accept", "application/json")
                            .build()
                    }

                    chain.proceed(request)
                }
            )
            .addInterceptor(loggingInterceptor)
            .authenticator(tokenAuthenticator)
            .addInterceptor(networkEventInterceptor)
            .connectTimeout(Constants.DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(Constants.DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(Constants.DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideCache(@ApplicationContext application: Context): Cache {
        val cacheSize = 10 * 1024 * 1024.toLong() // 10 MB
        val httpCacheDirectory = File(application.cacheDir, "http-cache")
        return Cache(httpCacheDirectory, cacheSize)
    }

    @Provides
    fun provideGsonClient(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun providerNetworkEvent(
        @CoroutineScopeMain coroutineScopeMain: CoroutineScope
    ) =
        NetworkEvent(coroutineScopeMain)

    @Provides
    @Singleton
    fun providerNetworkInterceptor(
        networkEvent: NetworkEvent,
        @ApplicationContext context: Context,
        gson: Gson
    ) = NetworkEventInterceptor(networkEvent, context, gson)

    @Provides
    @Singleton
    fun providerAuthenticator(
        rxPreferences: RxPreferences,
        gson: Gson,
        networkEvent: NetworkEvent,
    ) = TokenAuthenticator(rxPreferences, gson, networkEvent)

    @Singleton
    @Provides
    fun provideSSLSocketFactory(): SSLSocketFactory? {
        // Install the all-trusting trust manager
        var sslContext: SSLContext? = null
        try {
            sslContext = SSLContext.getInstance("SSL")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        try {
            sslContext!!.init(null, provideUnTrustManager(), SecureRandom())
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }

        // Create an ssl socket factory with our all-trusting manager
        return sslContext!!.socketFactory
    }

    @Singleton
    @Provides
    fun provideUnTrustManager(): Array<TrustManager> {
        return arrayOf(
            object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }
        )
    }
}

class NullOnEmptyConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val delegate: Converter<ResponseBody, Any> =
            retrofit.nextResponseBodyConverter(this, type, annotations)
        return Converter { body -> if (body.contentLength() == 0L) null else delegate.convert(body) }
    }
}
