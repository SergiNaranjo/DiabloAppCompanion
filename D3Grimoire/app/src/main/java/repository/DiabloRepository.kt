package API.repository

import API.DiabloApiInstance
import API.model.*
import repository.TokenManager
import retrofit2.Call

object DiabloRepository {

    private fun token(): String =
        TokenManager.token ?: throw IllegalStateException("Token not initialized")

    fun getSeasons(): Call<SeasonResponse> =
        DiabloApiInstance.api.getSeasons(token = token())

    fun getClasses(): Call<ClassesResponse> =
        DiabloApiInstance.api.getClasses(token = token())

    fun getClassDetail(slug: String): Call<ClassDetailResponse> =
        DiabloApiInstance.api.getClassDetail(classSlug = slug, token = token())

    fun getItem(slug: String): Call<ItemResponse> =
        DiabloApiInstance.api.getItemDetail(itemSlug = slug, token = token())
}
