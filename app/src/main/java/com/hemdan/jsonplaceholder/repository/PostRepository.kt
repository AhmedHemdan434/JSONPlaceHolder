package com.hemdan.jsonplaceholder.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.hemdan.jsonplaceholder.repository.database.PostDatabase
import com.hemdan.jsonplaceholder.repository.database.model.Post
import com.hemdan.jsonplaceholder.repository.database.model.PostState
import com.hemdan.jsonplaceholder.repository.network.Network.jsonPlaceHolder
import com.hemdan.jsonplaceholder.repository.network.model.NetworkPost
import com.hemdan.jsonplaceholder.repository.network.model.asDatabaseModelList
import com.hemdan.jsonplaceholder.repository.network.model.asNetworkAddPost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private fun Post.asNetworkModel(): NetworkPost {
    return NetworkPost( userId = userId, id = id, title = title, body = body)
}

class PostRepository(private val database: PostDatabase) {

    val posts: LiveData<List<Post>> = database.postsDao.getPosts()

    suspend fun fetchPostsAndInsert() {
        try {
            val response = jsonPlaceHolder.getPosts()
            withContext(Dispatchers.IO) {
                database.postsDao.insertAll(response.asDatabaseModelList())
            }
        } catch (e: Exception) {
            Log.e("PostRepository", "Error fetching posts: ${e.message}")
        }
    }

    suspend fun updatePost(post: Post) {
        withContext(Dispatchers.IO) {
            post.state = PostState.EDITED
            database.postsDao.updatePost(post)
        }
    }

    suspend fun insertPost(post: Post) {
        withContext(Dispatchers.IO) {
            post.state = PostState.CREATED
            database.postsDao.insertAll(listOf(post))
        }
    }

    suspend fun removePost(post: Post) {
        withContext(Dispatchers.IO) {
            post.state = PostState.DELETED
            database.postsDao.updatePost(post)
        }
    }

    suspend fun createPost(post: Post) {
        jsonPlaceHolder.addPost(post.asNetworkModel().asNetworkAddPost())
    }

    suspend fun editPost(post: Post) {
        jsonPlaceHolder.updatePost(post.id, post.asNetworkModel())
    }

    suspend fun deletePost(post: Post) {
        jsonPlaceHolder.deletePost(post.id)
    }
}