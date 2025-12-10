// dev.komsay.panindamobile.Service.ApiService.kt
package dev.komsay.panindamobile.backend.service

import dev.komsay.panindamobile.backend.dto.LoginResponseDTO
import dev.komsay.panindamobile.backend.dto.LoginUsersDTO
import dev.komsay.panindamobile.backend.dto.RegisterUsersDTO
import dev.komsay.panindamobile.backend.dto.UpdatePasswordDTO
import dev.komsay.panindamobile.backend.dto.UserDTO
import dev.komsay.panindamobile.ui.data.Product
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    // Auth
    @POST("auth/user/signup")
    fun registerUser(@Body registerUsersDTO: RegisterUsersDTO): Call<UserDTO>

    @POST("auth/user/login")
    fun loginUser(@Body loginUsersDTO: LoginUsersDTO): Call<LoginResponseDTO>

    @PUT("auth/user/password")
    fun updatePassword(@Body updatePasswordDTO: UpdatePasswordDTO): Call<UserDTO>

    @PUT("auth/user/edit")
    fun editUser(@Body editUserDto: UpdatePasswordDTO): Call<UserDTO>
}