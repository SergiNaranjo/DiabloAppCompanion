package com.example.d3grimoire.posts

class NewsData {
    var id: Int;
    var name: String?;
    var description: String?;
    var imgUrl: String?;
    var url: String?;
    var author: String?;

    public constructor(
        id: Int,
        name: String?, description: String?,
        imgUrl: String?,
        url: String?,
        author: String?
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imgUrl = imgUrl;
        this.url = url;
        this.author = author;
    }
}