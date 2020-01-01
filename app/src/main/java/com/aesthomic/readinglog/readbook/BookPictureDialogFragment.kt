package com.aesthomic.readinglog.readbook


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment

import com.aesthomic.readinglog.R
import com.aesthomic.readinglog.app.di.Scopes
import com.aesthomic.readinglog.databinding.FragmentBookPictureDialogBinding
import org.koin.android.ext.android.getKoin
import org.koin.core.scope.Scope

class BookPictureDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentBookPictureDialogBinding
    private lateinit var scope: Scope
    private lateinit var viewModel: ReadBookViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_book_picture_dialog, container, false)

        initViewModel()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBookpicDialogCamera.setOnClickListener {
            viewModel.onEventCamera()
            dismiss()
        }

        binding.btnBookpicDialogAlbum.setOnClickListener {
            viewModel.onEventGallery()
            dismiss()
        }
    }

    private fun initViewModel() {
        scope = getKoin().getScope(Scopes.READ_BOOK)
        viewModel = scope.get()
    }
}
