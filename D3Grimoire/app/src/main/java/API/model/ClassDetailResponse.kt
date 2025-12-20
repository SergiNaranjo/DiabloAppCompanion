package API.model

data class ClassDetailResponse(
    val skills: Skills
)

data class Skills(
    val active: List<Skill>,
    val passive: List<Skill>
)

data class Skill(
    val name: String,
    val description: String,
    val icon: String,
    val level: Int
)