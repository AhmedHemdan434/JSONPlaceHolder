package com.hemdan.jsonplaceholder.presentation.editpost

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hemdan.jsonplaceholder.repository.PostRepository
import com.hemdan.jsonplaceholder.repository.database.getPostsDatabase
import com.hemdan.jsonplaceholder.repository.database.model.Post
import kotlinx.coroutines.launch

class EditPostViewModel(application: Application, initialPost: Post) : ViewModel() {
    private var postRepository: PostRepository = PostRepository(getPostsDatabase(application))
    val title = MutableLiveData(initialPost.title)
    val content = MutableLiveData(initialPost.body)
    private var chosenPost = initialPost


    fun onSaveClick() {
        // Get the updated title and content from the LiveData objects
        val updatedTitle = title.value ?: ""
        val updatedContent = content.value ?: ""
        if(updatedTitle.isNotEmpty() && updatedContent.isNotEmpty()) {
            chosenPost.title = updatedTitle
            chosenPost.body = updatedContent
            viewModelScope.launch {
                postRepository.updatePost(chosenPost)
            }
        }
    }

    fun onDeleteClick() {
        viewModelScope.launch {
            postRepository.removePost(chosenPost)
        }
    }

    fun onAddClick() {
        val updatedTitle = title.value ?: ""
        val updatedContent = content.value ?: ""
        if(updatedTitle.isNotEmpty() && updatedContent.isNotEmpty()) {
            val newPost = Post(title = updatedTitle, body = updatedContent)
            viewModelScope.launch {
                postRepository.insertPost(newPost)
            }
        }
    }
}