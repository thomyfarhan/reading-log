package com.aesthomic.readinglog.readbook


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import androidx.navigation.fragment.findNavController

import com.aesthomic.readinglog.R
import com.aesthomic.readinglog.databinding.FragmentReadBookBinding
import com.aesthomic.readinglog.util.createImageFile
import com.aesthomic.readinglog.util.decodeUriBitmap
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File
import java.io.IOException

class ReadBookFragment : Fragment() {

    companion object {
        private const val REQUEST_CAPTURE_IMAGE = 100
        private const val REQUEST_PICK_IMAGE = 101
        private const val IMAGE_WIDTH = 750
        private const val IMAGE_HEIGHT = 750
    }

    private lateinit var binding: FragmentReadBookBinding
    private lateinit var picturePath: String
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private var readKey = 0L
    private val viewModel: ReadBookViewModel by viewModel { parametersOf(readKey) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_read_book, container, false)

        readKey = ReadBookFragmentArgs.fromBundle(requireArguments()).readKey
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

        viewModel.eventImage.observe(this, Observer {
            if (it) {
                this.findNavController().navigate(R.id.book_picture_dialog)
                viewModel.onImageDone()
            }
        })

        viewModel.eventCamera.observe(this, Observer {
            if (it) {
                openCameraIntent()
                viewModel.onCameraDone()
            }
        })

        viewModel.eventGallery.observe(this, Observer {
            if (it) {
                openAlbumIntent()
                viewModel.onGalleryDone()
            }
        })

        viewModel.pictureFile.observe(this, Observer {
            it.let {
                val imgUri = Uri.fromFile(it)
                binding.bottomSheet.ivBotsheetReadBookPhoto
                    .setImageURI(imgUri)
                viewModel.photoUri.value = imgUri.toString()
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

    private fun openCameraIntent() {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (pictureIntent.resolveActivity(requireActivity().packageManager) != null) {

            val pictureFile = getCreatedImage()

            pictureFile?.let {
                picturePath = pictureFile.absolutePath
                val photoUri = FileProvider.getUriForFile(requireContext(),
                    getString(R.string.application_id), it)
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE)
            }
        }
    }

    private fun openAlbumIntent() {
        val albumIntent = Intent(Intent.ACTION_PICK)
        if (albumIntent.resolveActivity(requireActivity().packageManager) != null) {

            val pictureFile = getCreatedImage()

            pictureFile?.let {
                picturePath = it.absolutePath
                albumIntent.type = "image/*"
                startActivityForResult(albumIntent, REQUEST_PICK_IMAGE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CAPTURE_IMAGE) {
            val imgFile = File(picturePath)
            if (resultCode == RESULT_OK) {
                val imgUri = Uri.fromFile(imgFile)
                val bitmapSource = decodeUriBitmap(
                    requireContext(), imgUri, IMAGE_WIDTH, IMAGE_HEIGHT)
                viewModel.inputBitmapFile(bitmapSource, imgFile)
            } else {
                imgFile.delete()
                return
            }
        }

        else if (requestCode == REQUEST_PICK_IMAGE) {
            val imgFile = File(picturePath)
            if (resultCode == RESULT_OK) {
                data?.data?.let {
                    val bitmapSource = decodeUriBitmap(
                        requireContext(), it, IMAGE_WIDTH, IMAGE_HEIGHT)
                    viewModel.inputBitmapFile(bitmapSource, imgFile)
                }
            } else {
                imgFile.delete()
                return
            }
        }
    }

    private fun getCreatedImage(): File? {
        return try {
            createImageFile(requireActivity())
        } catch (ex: IOException) {
            Toast.makeText(requireContext(),
                getString(R.string.fail_create_file_text),
                Toast.LENGTH_SHORT).show()
            null
        }
    }

}
