package com.hemdan.jsonplaceholder.presentation.postList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hemdan.jsonplaceholder.repository.PostRepository
import com.hemdan.jsonplaceholder.repository.database.getPostsDatabase
import com.hemdan.jsonplaceholder.repository.database.model.Post
import kotlinx.coroutines.launch

class PostListViewModel(application: Application) : AndroidViewModel(application) {
    private var postRepository: PostRepository = PostRepository(getPostsDatabase(application))
    var posts: LiveData<List<Post>> = postRepository.posts

    init {
        fetchPosts()
    }

    private fun fetchPosts() {
        viewModelScope.launch {
            postRepository.fetchPostsAndInsert()
        }
    }
}