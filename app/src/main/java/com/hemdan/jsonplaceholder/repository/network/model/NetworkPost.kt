package com.hemdan.jsonplaceholder.repository.network.model

import com.google.gson.annotations.SerializedName
import com.hemdan.jsonplaceholder.repository.database.model.Post


data class NetworkPost (
    @SerializedName("userId" ) var userId : Int,
    @SerializedName("id"     ) var id     : Int,
    @SerializedName("title"  ) var title  : String,
    @SerializedName("body"   ) var body   : String
)

data class NetworkAddPost (
    @SerializedName("userId" ) var userId : Int,
    @SerializedName("title"  ) var title  : String,
    @SerializedName("body"   ) var body   : String
)

fun List<NetworkPost>.asDatabaseModelList(): List<Post> {
    return this.map {
        Post(
            userId = it.userId,
            id = it.id,
            title = it.title,
            body = it.body
        )
    }.toList()
}

fun NetworkPost.asNetworkAddPost(): NetworkAddPost {
    return NetworkAddPost(
            userId = this.userId,
            title = this.title,
            body = this.body
        )
}