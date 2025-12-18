package com.example.d3grimoire

data class CommunityPost(
    val title: String = "",
    val description: String = "",
    val username: String = "",
    val profileImageUrl: String = "",
    val timestamp: Long = System.currentTimeMillis()
)