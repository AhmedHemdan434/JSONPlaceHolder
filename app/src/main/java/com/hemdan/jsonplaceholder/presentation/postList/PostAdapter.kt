package com.hemdan.jsonplaceholder.presentation.postList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hemdan.jsonplaceholder.databinding.ItemPostBinding
import com.hemdan.jsonplaceholder.repository.database.model.Post

class PostAdapter(private var posts: List<Post>, private val listener: OnPostClickListener) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentPost = posts[position]
        holder.binding.post = currentPost // Set the post object for Data Binding
        holder.binding.executePendingBindings() // Force immediate binding

        holder.binding.root.setOnClickListener {
            listener.onPostClick(currentPost)

        }
    }

    override fun getItemCount(): Int = posts.size

    fun updatePosts(newPosts: List<Post>) {
        posts = newPosts
        notifyDataSetChanged()
    }

    interface OnPostClickListener {
        fun onPostClick(post: Post)
    }
}