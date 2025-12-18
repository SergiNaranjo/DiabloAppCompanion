package API

import API.model.ClassDetailResponse
import API.model.ClassesResponse
import API.model.ItemResponse
import API.model.SeasonResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DiabloApiService {

    @GET("d3/data/season")
    fun getSeasons(
        @Query("namespace") namespace: String = "d3",
        @Query("locale") locale: String = "en_US",
        @Query("access_token") token: String
    ): Call<SeasonResponse>

    @GET("d3/data/hero")
    fun getClasses(
        @Query("namespace") namespace: String = "d3",
        @Query("locale") locale: String = "en_US",
        @Query("access_token") token: String
    ): Call<ClassesResponse>

    @GET("d3/data/hero/{classSlug}")
    fun getClassDetail(
        @Path("classSlug") classSlug: String,
        @Query("namespace") namespace: String = "d3",
        @Query("locale") locale: String = "en_US",
        @Query("access_token") token: String
    ): Call<ClassDetailResponse>

    @GET("d3/data/item/{itemSlug}")
    fun getItemDetail(
        @Path("itemSlug") itemSlug: String,
        @Query("namespace") namespace: String = "d3",
        @Query("locale") locale: String = "en_US",
        @Query("access_token") token: String
    ): Call<ItemResponse>
}
