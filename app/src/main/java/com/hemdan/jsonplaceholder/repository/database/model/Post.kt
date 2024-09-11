package com.hemdan.jsonplaceholder.repository.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity
class Post(
    var userId : Int = 1,
    @PrimaryKey(autoGenerate = true)
    var id     : Int = 0,
    var title  : String,
    var body   : String,
    var state  : PostState = PostState.SYNCED
)

enum class PostState {
    CREATED,
    DELETED,
    EDITED,
    SYNCED
}

class PostStateConverter {
    @TypeConverter
    fun fromPostState(state: PostState): String {
        return state.name // Convert enum to its string name
    }

    @TypeConverter
    fun toPostState(value: String): PostState {
        return PostState.valueOf(value) // Convert string back to enum
    }
}