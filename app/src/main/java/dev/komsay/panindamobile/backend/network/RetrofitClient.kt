package dev.komsay.panindamobile.backend.network

import android.content.Context
import dev.komsay.panindamobile.backend.service.ApiService
import dev.komsay.panindamobile.backend.service.CategoryApi
import dev.komsay.panindamobile.backend.service.ProductsApi
import dev.komsay.panindamobile.backend.service.SalesApi
import dev.komsay.panindamobile.backend.service.SalesHistoryApi
import dev.komsay.panindamobile.backend.service.SalesItemsApi
import dev.komsay.panindamobile.backend.service.SharedPrefManager
import dev.komsay.panindamobile.backend.service.StockHistoryApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    private var retrofit: Retrofit? = null

    private fun getRetrofit(context: Context): Retrofit {
        if (retrofit == null) {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val originalRequest = chain.request()
                    val token = SharedPrefManager.getToken(context)

                    // Check if this is an auth endpoint (login/signup)
                    val url = originalRequest.url.toString()
                    val isAuthEndpoint = url.contains("/auth/user/login") ||
                            url.contains("/auth/user/signup")
                    val isPublicAuthEndpoint = url.contains("/auth/user/password")


                    val requestBuilder = originalRequest.newBuilder().apply {
                        header("Accept", "application/json")

                        // Only add Authorization header if NOT an auth endpoint
                        if (token != null && !isAuthEndpoint && !isPublicAuthEndpoint) {
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

        return retrofit!!
    }

    fun getApi(context: Context): ApiService {
        return getRetrofit(context).create(ApiService::class.java)
    }

    fun getCategoryApi(context: Context): CategoryApi {
        return getRetrofit(context).create(CategoryApi::class.java)
    }

    fun getProductsApi(context: Context): ProductsApi {
        return getRetrofit(context).create(ProductsApi::class.java)
    }

    fun getSalesApi(context: Context): SalesApi {
        return getRetrofit(context).create(SalesApi::class.java)
    }

    fun getSalesHistoryApi(context: Context): SalesHistoryApi {
        return getRetrofit(context).create(SalesHistoryApi::class.java)
    }

    fun getSalesItemsApi(context: Context): SalesItemsApi {
        return getRetrofit(context).create(SalesItemsApi::class.java)
    }

    fun getStockHistoryApi(context: Context): StockHistoryApi {
        return getRetrofit(context).create(StockHistoryApi::class.java)
    }

    fun clear() {
        retrofit = null
    }
}