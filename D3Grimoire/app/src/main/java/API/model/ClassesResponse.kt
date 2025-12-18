package API.model

data class ClassesResponse(
    val classes: List<HeroClass>
)

data class HeroClass(
    val slug: String,
    val name: String,
    val genderedName: GenderedName
)

data class GenderedName(
    val male: String,
    val female: String
)

