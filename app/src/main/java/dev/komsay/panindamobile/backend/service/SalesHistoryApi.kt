package dev.komsay.panindamobile.backend.service

import dev.komsay.panindamobile.backend.dto.SalesHistoryDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SalesHistoryApi {

    @GET("/api/sales_history")
    suspend fun getAllSalesHistory(): List<SalesHistoryDTO>

    @GET("/api/sales_history/{id}")
    suspend fun getSalesHistory(@Path("id") id: Long): SalesHistoryDTO

    @POST("/api/sales_history")
    suspend fun addSalesHistory(@Body salesHistoryDTO: SalesHistoryDTO): SalesHistoryDTO

    @GET("/api/sales_history/filter/today")
    suspend fun getToday(): List<SalesHistoryDTO>

    @GET("/api/sales_history/filter/7days")
    suspend fun getPast7Days(): List<SalesHistoryDTO>

    @GET("/api/sales_history/filter/30days")
    suspend fun getPast30Days(): List<SalesHistoryDTO>
}