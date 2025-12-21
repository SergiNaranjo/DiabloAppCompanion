package API.model

import com.google.gson.annotations.SerializedName

data class HeroClassResponse(
    val slug: String,
    val name: String,

    @SerializedName("maleName")
    val maleName: String,

    @SerializedName("femaleName")
    val femaleName: String,

    val icon: String,

    @SerializedName("skillCategories")
    val skillCategories: List<SkillCategory>,

    val skills: HeroSkills
)

data class SkillCategory(
    val slug: String,
    val name: String
)

data class HeroSkills(
    val active: List<HeroSkill>,
    val passive: List<HeroSkill>
)

data class HeroSkill(
    val slug: String,
    val name: String,
    val icon: String,
    val level: Int,
    val description: String
)
