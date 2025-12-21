package API

import API.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DiabloApiService {

    @GET("d3/data/season/")
    fun getSeasons(
        @Query("locale") locale: String = "en_US",
        @Query("access_token") token: String
    ): Call<SeasonResponse>

    @GET("d3/data/class/")
    fun getClasses(
        @Query("locale") locale: String = "en_US",
        @Query("access_token") token: String
    ): Call<ClassesResponse>

    @GET("d3/data/class/{slug}/")
    fun getClassDetail(
        @Path("slug") slug: String,
        @Query("locale") locale: String = "en_US",
        @Query("access_token") token: String
    ): Call<ClassDetailResponse>

    @GET("d3/data/item/{slug}/")
    fun getItem(
        @Path("slug") slug: String,
        @Query("locale") locale: String = "en_US",
        @Query("access_token") token: String
    ): Call<ItemResponse>
}
