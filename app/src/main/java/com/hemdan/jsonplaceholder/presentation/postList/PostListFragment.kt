package com.hemdan.jsonplaceholder.presentation.postList

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hemdan.jsonplaceholder.databinding.FragmentPostListBinding
import com.hemdan.jsonplaceholder.presentation.editpost.EditPostDialogFragment
import com.hemdan.jsonplaceholder.repository.database.model.Post


class PostListFragment : Fragment(), PostAdapter.OnPostClickListener{

    private val viewModel: PostListViewModel by lazy {
        PostListViewModelFactory(requireActivity().application).create(PostListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        val postAdapter = PostAdapter(emptyList(), this)
        binding.postRecycler.adapter = postAdapter
        binding.postRecycler.layoutManager = LinearLayoutManager(context)
        binding.floatingActionButton.setOnClickListener(View.OnClickListener {
            onPostClick(Post(title = "", body = ""))
        })

        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            postAdapter.updatePosts(posts)
        }
        return binding.root
    }

    override fun onPostClick(post: Post) {
        EditPostDialogFragment(post).show(childFragmentManager, "edit_post_dialog")
    }

    class PostListViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PostListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PostListViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}