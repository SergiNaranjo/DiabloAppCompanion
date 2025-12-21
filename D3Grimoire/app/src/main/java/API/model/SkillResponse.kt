package API.model

import com.google.gson.annotations.SerializedName

data class SkillResponse(

    @SerializedName("id")
    val id: Int,

    @SerializedName("slug")
    val slug: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("icon")
    val icon: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("simple_description")
    val simpleDescription: String,

    @SerializedName("skill_category_slug")
    val skillCategorySlug: String,

    @SerializedName("tooltip_url")
    val tooltipUrl: String,

    @SerializedName("level")
    val level: Int,

    @SerializedName("cooldown")
    val cooldown: Double,

    @SerializedName("is_primary")
    val isPrimary: Boolean
)
