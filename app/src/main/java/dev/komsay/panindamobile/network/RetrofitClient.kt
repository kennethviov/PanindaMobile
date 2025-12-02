package dev.komsay.panindamobile.network

import android.content.Context
import dev.komsay.panindamobile.Service.ApiService
import dev.komsay.panindamobile.Service.SharedPrefManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "http://10.0.2.2:8080/"  // Emulator → localhost
    // For real device: use your PC's IP (e.g. http://192.168.1.100:8080/)
    // Or use ngrok for testing: https://your-subdomain.ngrok.io

    private var retrofit: Retrofit? = null

    fun getApi(context: Context): ApiService {
        if (retrofit == null) {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val originalRequest = chain.request()
                    val token = SharedPrefManager.getToken(context)

                    val requestBuilder = originalRequest.newBuilder().apply {
                        header("Accept", "application/json")
                        if (token != null) {
                            addHeader("Authorization", "Bearer $token")
                        }
                    }

                    chain.proceed(requestBuilder.build())
                }
                .addInterceptor(logging)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return retrofit!!.create(ApiService::class.java)
    }

    // Optional: Reset retrofit (useful when user logs out)
    fun clear() {
        retrofit = null
    }
}