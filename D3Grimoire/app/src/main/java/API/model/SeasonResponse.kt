package API.model

data class SeasonResponse(
    val season: List<Season>
)

data class Season(
    val id: Int,
    val name: String
)