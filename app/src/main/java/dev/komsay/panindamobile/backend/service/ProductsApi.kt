package dev.komsay.panindamobile.backend.service

import dev.komsay.panindamobile.backend.dto.ProductsDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ProductsApi {

    @GET("api/products")
    suspend fun getAllProducts(): List<ProductsDTO>

    @GET("api/products/{id}")
    suspend fun getProduct(@Path("id") id: Long): ProductsDTO

    @GET("api/products/{id}/image")
    suspend fun getProductImage(@Path("id") id: Long): ResponseBody

    @POST("api/products")
    suspend fun addProduct(@Body product: ProductsDTO): ProductsDTO

    @Multipart
    @POST("api/products/with-image")
    suspend fun addProductWithImage(
        @Part("product") productJson: RequestBody,
        @Part image: MultipartBody.Part
    ): ProductsDTO

    @PUT("api/products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Long,
        @Body product: ProductsDTO
    ): ProductsDTO

    @Multipart
    @PUT("api/products/{id}/image")
    suspend fun updateProductImage(
        @Path("id") id: Long,
        @Part image: MultipartBody.Part
    ): ProductsDTO

    @DELETE("api/products/{id}")
    suspend fun deleteProduct(@Path("id") id: Long): ProductsDTO
}
