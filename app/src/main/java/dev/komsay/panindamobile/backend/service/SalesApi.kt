package dev.komsay.panindamobile.backend.service

import dev.komsay.panindamobile.backend.dto.SalesDTO
import dev.komsay.panindamobile.backend.dto.TopProductsDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SalesApi {

    @GET("/api/sales")
    suspend fun getAllSales(): List<SalesDTO>

    @GET("/api/sales/{id}")
    suspend fun getSale(@Path("id") id: Long): SalesDTO

    @POST("/api/sales/{productId}")
    suspend fun sell(
        @Path("productId") productId: String,
        quantity: Int
    ): SalesDTO

    @POST("/api/sales")
    suspend fun sellMultiple(@Body salesDTO: SalesDTO): SalesDTO

    @GET("/api/sales/range")
    suspend fun getSalesByDate(
        @Query("start") start: String,
        @Query("end") end: String
    ): List<SalesDTO>

    @GET("/api/sales/stats/top-products")
    suspend fun getTopProducts(
        @Query("limit") limit: Int = 10,
        @Query("start") start: String? = null,
        @Query("end") end: String? = null
    ): List<TopProductsDTO>
}