package dev.komsay.panindamobile.backend.service

import dev.komsay.panindamobile.backend.dto.StockHistoryDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface StockHistoryApi {

    @GET("/api/stock_history")
    suspend fun getAllStockHistories(): List<StockHistoryDTO>

    @GET("/api/stock_history/{id}")
    suspend fun getStockHistory(@Path("id") id: Long): StockHistoryDTO

    @POST("/api/stock_history")
    suspend fun addStockHistory(@Body stockHistoryDTO: StockHistoryDTO): StockHistoryDTO

    @PUT("/api/stock_history/{id}")
    suspend fun updateStockHistory(
        @Path("id") id: Long,
        @Body stockHistoryDTO: StockHistoryDTO
    ): StockHistoryDTO

    @DELETE("api/stock_history/{id}")
    suspend fun deleteStockHistory(@Path("id") id: Long): StockHistoryDTO

    @GET("/api/stock_history/filter/today")
    suspend fun filterToday(): List<StockHistoryDTO>

    @GET("/api/stock_history/filter/7days")
    suspend fun filterPast7Days(): List<StockHistoryDTO>

    @GET("/api/stock_history/filter/30days")
    suspend fun filterPast30Days(): List<StockHistoryDTO>
}