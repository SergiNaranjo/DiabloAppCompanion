package API

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface BlizzardAuthApi {

    @POST("token")
    @FormUrlEncoded
    fun getAccessToken(
        @Field("grant_type") grantType: String = "client_credentials"
    ): Call<AuthResponse>
}
