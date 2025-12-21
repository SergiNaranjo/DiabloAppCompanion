package com.example.d3grimoire.posts

class NewsData {
    var id: Int;
    var name: String?;
    var description: String?;
    var imgId: String?;
    var url: String?;

    public constructor(
        id: Int,
        name: String?, description: String?,
        imgId: String?,
        url: String?
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imgId = imgId;
        this.url = url;
    }
}