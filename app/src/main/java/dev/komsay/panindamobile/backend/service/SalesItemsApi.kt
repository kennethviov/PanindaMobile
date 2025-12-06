package dev.komsay.panindamobile.backend.service

import dev.komsay.panindamobile.backend.dto.SalesItemsDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface SalesItemsApi {

    @GET("/api/sales-items")
    suspend fun getAllSalesItem(): List<SalesItemsDTO>

    @GET("/api/sales-items/{id}")
    suspend fun getSalesItemById(@Path("id") id: Long): SalesItemsDTO

    @GET("/api/sales-items/{salesId")
    suspend fun getSalesItemBySalesId(@Path("salesId") salesId: Long): List<SalesItemsDTO>

    @GET("/api/sales-items/products/{productId}")
    suspend fun getSalesItemsByProductId(@Path("productId") productId: Long): List<SalesItemsDTO>

}