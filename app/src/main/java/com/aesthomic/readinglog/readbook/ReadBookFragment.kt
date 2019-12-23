package com.aesthomic.readinglog.readbook


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.aesthomic.readinglog.R
import com.aesthomic.readinglog.database.ReadingLogDatabase
import com.aesthomic.readinglog.databinding.FragmentReadBookBinding
import com.aesthomic.readinglog.getTime
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.io.File
import java.io.IOException

class ReadBookFragment : Fragment() {

    companion object {
        private const val REQUEST_CAPTURE_IMAGE = 100
    }

    private lateinit var binding: FragmentReadBookBinding
    private lateinit var viewModel: ReadBookViewModel
    private lateinit var picturePath: String
    private lateinit var imgFile: File
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_read_book, container, false)

        initViewModel()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupBottomSheetBehavior()

        val time = System.currentTimeMillis()

        viewModel.eventSubmit.observe(this, Observer {
            if (it) {
                viewModel.updateRead(time)
                viewModel.onSubmitDone()

                this.findNavController().navigate(
                    ReadBookFragmentDirections.actionReadBookDestinationToReadDestination())
            }
        })

        viewModel.eventCamera.observe(this, Observer {
            if (it) {
                openCameraIntent()
                viewModel.onCameraDone()
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    } else {
                        findNavController().popBackStack(R.id.read_destination, false)
                    }
                }
            })
    }

    private fun setupBottomSheetBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.botsheetReadBook)
    }

    private fun initViewModel() {
        val readKey = ReadBookFragmentArgs.fromBundle(requireArguments()).readKey
        val application = requireNotNull(this.activity).application

        val database = ReadingLogDatabase.getInstance(application)
        val dbRead = database.readDao
        val dbBook = database.bookDao

        val viewModelFactory = ReadBookViewModelFactory(
            readKey, dbRead, dbBook)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ReadBookViewModel::class.java)
    }

    private fun openCameraIntent() {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (pictureIntent.resolveActivity(requireActivity().packageManager) != null) {

            val pictureFile: File?
            try {
                pictureFile = createImageFile()
            } catch (ex: IOException) {
                Toast.makeText(requireContext(),
                    getString(R.string.fail_create_file_text),
                    Toast.LENGTH_SHORT).show()
                return
            }

            pictureFile.let {
                val photoUri = FileProvider.getUriForFile(requireContext(),
                    getString(R.string.application_id),
                    pictureFile)
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CAPTURE_IMAGE) {
            imgFile = File(picturePath)
            if (resultCode == RESULT_OK) {
                binding.bottomSheet.ivBotsheetReadBookPhoto.setImageURI(
                    Uri.fromFile(imgFile))
                viewModel.photoUri.value = Uri.fromFile(imgFile).toString()
            } else {
                imgFile.delete()
                return
            }
        }
    }

    private fun createImageFile(): File {
        val timeStamp = getTime()
        val pictureName = getString(R.string.picture_file_format, timeStamp)

        val storageDir = requireActivity().getExternalFilesDir(
            Environment.DIRECTORY_PICTURES)

        val image = File.createTempFile(
            pictureName,
            "jpg",
            storageDir)

        picturePath = image.absolutePath
        return image
    }
}
