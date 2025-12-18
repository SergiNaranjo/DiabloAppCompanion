package API.repository

import API.DiabloApiInstance
import API.model.SeasonResponse
import retrofit2.Call

class DiabloRepository {

    fun getSeasons(token: String): Call<SeasonResponse> =
        DiabloApiInstance.api.getSeasons(token = token)

    fun getClasses(token: String) =
        DiabloApiInstance.api.getClasses(token = token)

    fun getClassDetail(classSlug: String, token: String) =
        DiabloApiInstance.api.getClassDetail(classSlug = classSlug, token = token)

    fun getItemDetail(itemSlug: String, token: String) =
        DiabloApiInstance.api.getItemDetail(itemSlug = itemSlug, token = token)
}
