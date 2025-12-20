package API.model

data class ItemResponse(
    val name: String,
    val icon: String,
    val itemLevel: Int,
    val requiredLevel: Int,
    val damage: String,
    val attacksPerSecond: Float
)

