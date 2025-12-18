package GrimoireApi

import android.provider.MediaStore.Images.Thumbnails

data class GrimoireResponse() {
    val code: Int,
            val status: String,
                    val data: GrimoireData
}

data class GrimoireData {
    val seasonalInfo: SeasonalInfo
    val resultsCommunityNews: List<CommunityPosts>
    val resultsOfficialNews: List<OfficalPosts>

}

data class CommunityPosts {
    val id: Int,
            val title: String,
                    val description: String,
                            val profileThumbnail: Thumbnail
                            val username: String,
}

data class SeasonalInfo {
    val id: Int,
            val title: String,
                    val thumbnial: Thumbnail
}

data class OfficalPosts {
    val id: Int,
         val title: String,
         val thumbnail: Thumbnail
}

data class Thumbnail {
    val path: String,
            val extension: String
}
