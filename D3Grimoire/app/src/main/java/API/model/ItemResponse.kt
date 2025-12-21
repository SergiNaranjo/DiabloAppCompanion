package API.model

import com.google.gson.annotations.SerializedName

data class ItemResponse(
    val name: String,
    val icon: String,
    val itemLevel: Int,
    val requiredLevel: Int,
    val damage: String,
    val attacksPerSecond: Float,
    @SerializedName("typeName") val typeName: String
)