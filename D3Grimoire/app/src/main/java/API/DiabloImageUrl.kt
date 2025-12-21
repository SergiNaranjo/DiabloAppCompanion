package API

object DiabloImageUrl {
    private const val BASE = "https://blzmedia-a.akamaihd.net/d3/icons"

    fun item(icon: String, size: String = "large"): String {
        val cleanIcon = icon.removeSuffix(".png")
        return "$BASE/items/$size/$cleanIcon.png"
    }

    fun hero(icon: String, size: Int = 64): String =
        "$BASE/hero/$size/${icon.removeSuffix(".png")}.png"

    fun skill(icon: String, size: Int = 64): String =
        "$BASE/skills/$size/${icon.removeSuffix(".png")}.png"
}