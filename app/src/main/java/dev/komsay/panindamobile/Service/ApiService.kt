// dev.komsay.panindamobile.Service.ApiService.kt
package dev.komsay.panindamobile.Service

import dev.komsay.panindamobile.dto.LoginResponse
import dev.komsay.panindamobile.dto.LoginUsersDTO
import dev.komsay.panindamobile.dto.ProductDTO
import dev.komsay.panindamobile.dto.RegisterUsersDTO
import dev.komsay.panindamobile.dto.Users
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // Auth
    @POST("auth/user/signup")
    fun registerUser(@Body registerUsersDTO: RegisterUsersDTO): Call<Users>

    @POST("auth/user/login")
    fun loginUser(@Body loginUsersDTO: LoginUsersDTO): Call<LoginResponse>

    // Products
    @GET("api/products")
    fun getAllProducts(): Call<List<ProductDTO>>

    // Get product image (returns raw bytes)
    @GET("api/products/{id}/image")
    fun getProductImage(@Path("id") id: Long): Call<okhttp3.ResponseBody>
}