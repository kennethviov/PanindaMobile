package dev.komsay.panindamobile.backend.service

import dev.komsay.panindamobile.backend.dto.CategoryDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface CategoryApi {

    @POST("/api/categories")
    suspend fun createCategory(@Body categoryDTO: CategoryDTO): CategoryDTO

    @GET("/api/categories")
    suspend fun getAllCategories(): List<CategoryDTO>

    @GET("api/categories/{categoryId}")
    suspend fun getCategoryById(@Path("categoryId") categoryId: UUID): CategoryDTO

    @GET("api/categories/search")
    suspend fun getCategoryByName(@Body name: String): CategoryDTO

    @PUT("/api/categories/{categoryId}")
    suspend fun updateCategory(
        @Path("categoryId") categoryId: UUID,
        @Body categoryDTO: CategoryDTO
    ): CategoryDTO

    @DELETE("/api/categories/{categoryId}")
    suspend fun deleteCategory(@Path("categoryId") categoryId: UUID): Void

}