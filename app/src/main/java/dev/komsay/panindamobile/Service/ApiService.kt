package dev.komsay.panindamobile.Service

import dev.komsay.panindamobile.dto.LoginResponse
import dev.komsay.panindamobile.dto.LoginUsersDTO
import dev.komsay.panindamobile.dto.RegisterUsersDTO
import dev.komsay.panindamobile.dto.Users
import retrofit2.Call;
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    //Register
    @POST("auth/user/signup")
    fun registerUser(@Body registerUsersDTO: RegisterUsersDTO): Call<Users>

    //Login
    @POST("auth/user/login")
    fun loginUser(@Body loginUsersDTO: LoginUsersDTO): Call<LoginResponse>

    //Products

}