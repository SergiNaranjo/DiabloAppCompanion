package API

object DiabloImageUrl {
    private const val BASE_ICONS = "https://blzmedia-a.akamaihd.net/d3/icons"
    private const val BASE_PORTRAIT = "https://us.diablo3.blizzard.com/static/images/hero"

    fun item(icon: String, size: String = "large"): String =
        "$BASE_ICONS/items/$size/${icon.removeSuffix(".png")}.png"

    fun skill(icon: String, size: Int = 64): String =
        "$BASE_ICONS/skills/$size/${icon.removeSuffix(".png")}.png"

    fun classPortrait(slug: String, gender: String): String =
        "$BASE_PORTRAIT/$slug/$gender-portrait.jpg"

    fun classGif(slug: String): String {
        return when (slug) {
            "barbarian" -> "https://static.wikia.nocookie.net/diablo/images/b/b3/Barbarian_Male_Selection.gif"
            "crusader" -> "https://static.wikia.nocookie.net/diablo/images/3/35/Crusader_Male_Selection.gif"
            "demon-hunter" -> "https://static.wikia.nocookie.net/diablo/images/7/70/Demon_Hunter_Male_Selection.gif"
            "monk" -> "https://static.wikia.nocookie.net/diablo/images/a/a7/Monk_Male_Selection.gif"
            "necromancer" -> "https://static.wikia.nocookie.net/diablo/images/2/25/Necromancer_Selection.gif"
            "witch-doctor" -> "https://static.wikia.nocookie.net/diablo/images/2/22/Witch_Doctor_Male_Selection.gif"
            "wizard" -> "https://static.wikia.nocookie.net/diablo/images/7/7a/Wizard_Male_Selection.gif"
            else -> "https://static.wikia.nocookie.net/diablo/images/b/b3/Barbarian_Male_Selection.gif"
        }
    }
}