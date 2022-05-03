package com.cs.flickr.model

class Photo(
    val id: String,
    val secret: String,
    val server: String,
    val farm: String,
    val title: String
) {
    override fun toString(): String {
        return title
    }
}