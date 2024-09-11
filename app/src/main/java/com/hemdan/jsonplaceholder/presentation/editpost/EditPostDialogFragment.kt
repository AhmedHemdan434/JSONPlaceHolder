package com.hemdan.jsonplaceholder.presentation.editpost

import android.app.Application
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hemdan.jsonplaceholder.databinding.EditPostDialogBinding
import com.hemdan.jsonplaceholder.repository.database.model.Post

class EditPostDialogFragment(private val post: Post) : DialogFragment() {

    private val viewModel: EditPostViewModel by viewModels {
        EditPostViewModelFactory(requireActivity().application, post)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val binding = EditPostDialogBinding.inflate(layoutInflater)
            binding.viewModel = viewModel
            if(post.title.isEmpty() && post.body.isEmpty()){
                binding.deleteButton.visibility = View.GONE
                binding.saveButton.text = "Add"
                binding.saveButton.setOnClickListener(View.OnClickListener {
                    viewModel.onAddClick()
                    dismiss()
                })
            } else {
                binding.saveButton.setOnClickListener(View.OnClickListener {
                    viewModel.onSaveClick()
                    dismiss()
                })
            }

            binding.deleteButton.setOnClickListener(View.OnClickListener {
                viewModel.onDeleteClick()
                dismiss()
            })

            AlertDialog.Builder(it)
                .setView(binding.root)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

// Factory for creating EditPostViewModel with initial post data
class EditPostViewModelFactory(private val application: Application, private val initialPost: Post) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditPostViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditPostViewModel(application, initialPost) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}