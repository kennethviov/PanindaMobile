package dev.komsay.panindamobile.network

import android.content.Context
import dev.komsay.panindamobile.Service.ApiService
import dev.komsay.panindamobile.Service.SharedPrefManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    fun getApi(context: Context): ApiService {

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val token = SharedPrefManager.getToken(context)
                val request = chain.request().newBuilder()

                if (token != null) {
                    request.addHeader("Authorization", "Bearer $token")
                }

                chain.proceed(request.build())
            }
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }
}
