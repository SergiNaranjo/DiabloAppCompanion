package API.repository

import API.DiabloApiInstance
import API.model.*
import retrofit2.Call

object DiabloRepository {

    fun getSeasons(token: String): Call<SeasonResponse> {
        return DiabloApiInstance.api.getSeasons(token = token)
    }

    fun getClasses(token: String): Call<ClassesResponse> {
        return DiabloApiInstance.api.getClasses(token = token)
    }

    fun getClassDetail(slug: String, token: String): Call<ClassDetailResponse> {
        return DiabloApiInstance.api.getClassDetail(slug, token = token)
    }

    fun getItem(slug: String, token: String): Call<ItemResponse> {
        return DiabloApiInstance.api.getItem(slug, token = token)
    }
}
