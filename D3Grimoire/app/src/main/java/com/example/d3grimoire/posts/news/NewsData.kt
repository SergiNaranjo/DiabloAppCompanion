package com.example.d3grimoire.posts.news

import com.example.d3grimoire.R

//Ideally, a separate API could be used for news.
// Unfortunately, Blizzard doesn't share their news through an API.

val newsButtonData: List<NewsData> = listOf(
    NewsData(
        R.id.news_post_1,
        "Season 35: Eternal Conflict – Has Concluded",
        "Diablo III Season 35 begins on June 6. Eradicate Hell’s emissaries using powerful boons granted by the season theme, and more. ",
        R.drawable.diablonews1,
        "https://news.blizzard.com/en-us/article/24191146/season-35-eternal-conflict-has-concluded"
    ),
    NewsData(
        R.id.news_post_2,
        "Play Diablo III on Game Pass now!",
        "The legendary action-RPG is now available on Game Pass, including the original game and the Reaper of Souls expansion.",
        R.drawable.diablonews2,
        "https://news.blizzard.com/en-us/article/24186408/play-diablo-iii-on-game-pass-now"
    ),
    NewsData(
        R.id.news_post_3,
        "BlizzCon Returns In 2026",
        "Our celebration of our universes, connection, and community is coming back to the Anaheim Convention Center next year.",
        R.drawable.diablonews3,
        "https://news.blizzard.com/en-us/article/24178650/blizzcon-returns-in-2026"
    )
);

class NewsData {
    var id: Int;
    var name: String;
    var description: String;
    var imgId: Int;
    var url: String;

    public constructor(
        id: Int,
        name: String, description: String,
        imgId: Int,
        url: String
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imgId = imgId;
        this.url = url;
    }
}