package com.example.d3grimoire.posts.news

import com.example.d3grimoire.R
import com.example.d3grimoire.posts.NewsData

//Ideally, a separate API could be used for news.
// Unfortunately, Blizzard doesn't share their news through an API.
val newsButtonData: List<NewsData> = listOf(
    NewsData(
        R.id.news_post_1,
        "Season 35: Eternal Conflict – Has Concluded",
        "Diablo III Season 35 begins on June 6. Eradicate Hell’s emissaries using powerful boons granted by the season theme, and more. ",
        "https://bnetcmsus-a.akamaihd.net/cms/blog_header/2m/2M8QRV55TNGM1744065821014.png",
        "https://news.blizzard.com/en-us/article/24191146/season-35-eternal-conflict-has-concluded",
        "Admin"
    ),
    NewsData(
        R.id.news_post_2,
        "Play Diablo III on Game Pass now!",
        "The legendary action-RPG is now available on Game Pass, including the original game and the Reaper of Souls expansion.",
        "https://bnetcmsus-a.akamaihd.net/cms/blog_header/5h/5HYUB8O4XUYQ1744130028829.jpg",
        "https://news.blizzard.com/en-us/article/24186408/play-diablo-iii-on-game-pass-now",
        "Admin"
    ),
    NewsData(
        R.id.news_post_3,
        "BlizzCon Returns In 2026",
        "Our celebration of our universes, connection, and community is coming back to the Anaheim Convention Center next year.",
        "https://bnetcmsus-a.akamaihd.net/cms/blog_header/lf/LFFILFFOMFKV1741374538375.jpg",
        "https://news.blizzard.com/en-us/article/24178650/blizzcon-returns-in-2026",
        "Admin"
    )
);