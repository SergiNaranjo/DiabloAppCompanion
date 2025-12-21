package API

import API.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DiabloApiService {

    @GET("d3/data/hero/{slug}")
    fun getHeroClass(
        @Path("slug") slug: String,
        @Query("namespace") namespace: String = "static-d3-eu",
        @Query("locale") locale: String = "es_ES"
    ): Call<HeroClassResponse>

    @GET("d3/data/hero/{classSlug}/skill/{skillSlug}")
    fun getSkill(
        @Path("classSlug") classSlug: String,
        @Path("skillSlug") skillSlug: String,
        @Query("namespace") namespace: String = "static-d3-eu",
        @Query("locale") locale: String = "es_ES"
    ): Call<SkillResponse>

    @GET("d3/data/item/{slugAndId}")
    fun getItem(
        @Path("slugAndId") slugAndId: String,
        @Query("namespace") namespace: String = "static-d3-eu",
        @Query("locale") locale: String = "es_ES"
    ): Call<ItemResponse>
}
