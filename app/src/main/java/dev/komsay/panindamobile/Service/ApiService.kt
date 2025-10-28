package dev.komsay.panindamobile.Service

import dev.komsay.panindamobile.dto.RegisterUsersDTO
import dev.komsay.panindamobile.dto.Users
import retrofit2.Call;
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/user/signup")
    fun registerUser(@Body registerUsersDTO: RegisterUsersDTO): Call<Users>
}