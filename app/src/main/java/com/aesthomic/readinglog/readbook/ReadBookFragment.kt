package com.aesthomic.readinglog.readbook


import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.aesthomic.readinglog.R
import com.aesthomic.readinglog.app.di.Scopes
import com.aesthomic.readinglog.databinding.FragmentReadBookBinding
import com.aesthomic.readinglog.util.createPictureFile
import com.aesthomic.readinglog.util.decodeUriBitmap
import com.aesthomic.readinglog.util.hideKeyboard
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.getKoin
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import java.io.File
import java.io.IOException

class ReadBookFragment : Fragment() {

    companion object {
        private const val REQUEST_CAPTURE_IMAGE = 100
        private const val REQUEST_PICK_IMAGE = 101
        private const val IMAGE_WIDTH = 750
        private const val IMAGE_HEIGHT = 750
        private const val READ_EXTERNAL_STORAGE_REQUEST = 102
    }

    private lateinit var binding: FragmentReadBookBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var callback: OnBackPressedCallback

    private lateinit var scope: Scope
    private lateinit var viewModel: ReadBookViewModel

    private var lastClickTime = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_read_book, container, false)

        initViewModel()
        initBookTitle()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupBottomSheetBehavior()

        viewModel.selectedBook.observe(this, Observer {
            if (it == null) {
                binding.etReadbookTitle.hint = getString(R.string.no_book)
            }
        })

        viewModel.eventSubmit.observe(this, Observer {
            if (it && SystemClock.elapsedRealtime() - lastClickTime > 1000) {
                hideKeyboard(requireContext(), requireView())

                val dialog = MaterialAlertDialogBuilder(activity)
                dialog.apply {
                    setTitle("Congratulations!")
                    setMessage("Record successfully saved!")
                    setCancelable(false)
                }
                dialog.setNeutralButton("Ok") {_,_ ->
                    this.findNavController().navigate(
                        ReadBookFragmentDirections.actionReadBookDestinationToReadDestination())
                }
                dialog.show()
                viewModel.onSubmitDone()
                lastClickTime = SystemClock.elapsedRealtime()
            }
        })

        viewModel.eventSubmitBook.observe(this, Observer {
            if (it) {
                hideKeyboard(requireContext(), requireView())
                viewModel.clearBookField()
                viewModel.onSubmitBookDone()
                binding.bottomSheet.ivBotsheetReadBookPhoto
                    .setImageResource(R.drawable.ic_photo)
                Toast.makeText(requireContext(), "Book Successfully added!", Toast.LENGTH_SHORT).show()

                Handler().postDelayed({
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }, 250)
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
                if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        READ_EXTERNAL_STORAGE_REQUEST)
                } else {
                    openAlbumIntent()
                }
                viewModel.onGalleryDone()
            }
        })

        viewModel.eventBook.observe(this, Observer {
            if (it) {
                hideKeyboard(requireContext(), requireView())
                this.findNavController().navigate(
                    ReadBookFragmentDirections.actionReadBookDestinationToBookListDestination()
                )
                viewModel.onBookDone()
            }
        })

        viewModel.pictureFile.observe(this, Observer {
            it?.let {
                val imgUri = Uri.fromFile(it)
                binding.bottomSheet.ivBotsheetReadBookPhoto
                    .setImageURI(imgUri)
                viewModel.photoUri.value = imgUri.toString()
            }
        })

        return binding.root
    }

    private fun initViewModel() {
        scope = if (getKoin().getScopeOrNull(Scopes.READ_BOOK) == null) {
            getKoin().createScope(Scopes.READ_BOOK, named(Scopes.READ_BOOK))
        } else {
            getKoin().getScope(Scopes.READ_BOOK)
        }

        viewModel = scope.get()

        if (viewModel.readKey == -1L) {
            viewModel.readKey = ReadBookFragmentArgs.fromBundle(requireArguments()).readKey
        }
    }

    private fun initBookTitle() {
        val bookKey = ReadBookFragmentArgs.fromBundle(requireArguments()).bookKey
        if (bookKey != -1L) {
            viewModel.setSelectedBook(bookKey)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                } else {
                    findNavController().popBackStack(R.id.read_destination, false)
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openAlbumIntent()
            } else {
                val showRationale = shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)

                if (showRationale) {
                    Toast.makeText(requireContext(), getString(R.string.grant_permission),
                        Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), getString(R.string.ungrant_permission),
                        Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun setupBottomSheetBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.botsheetReadBook)
    }

    private fun openCameraIntent() {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (pictureIntent.resolveActivity(requireActivity().packageManager) != null) {

            viewModel.newPicture = getCreatedPicture()

            viewModel.newPicture?.let {
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

            viewModel.newPicture = getCreatedPicture()

            viewModel.newPicture?.let {
                albumIntent.type = "image/*"
                startActivityForResult(albumIntent, REQUEST_PICK_IMAGE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CAPTURE_IMAGE) {
            if (resultCode == RESULT_OK) {
                val imgUri = Uri.fromFile(viewModel.newPicture)
                val bitmapSource = decodeUriBitmap(
                    requireContext(), imgUri, IMAGE_WIDTH, IMAGE_HEIGHT)
                viewModel.inputBitmapFile(bitmapSource)
            } else {
                viewModel.newPicture?.delete()
                return
            }
        }

        else if (requestCode == REQUEST_PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                data?.data?.let {
                    val bitmapSource = decodeUriBitmap(
                        requireContext(), it, IMAGE_WIDTH, IMAGE_HEIGHT)
                    viewModel.inputBitmapFile(bitmapSource)
                }
            } else {
                viewModel.newPicture?.delete()
                return
            }
        }
    }

    private fun getCreatedPicture(): File? {
        return try {
            createPictureFile(requireActivity())
        } catch (ex: IOException) {
            Toast.makeText(requireContext(),
                getString(R.string.fail_create_file_text),
                Toast.LENGTH_SHORT).show()
            null
        }
    }

    override fun onPause() {
        super.onPause()
        callback.isEnabled = false
    }

    override fun onResume() {
        super.onResume()
        callback.isEnabled = true
    }


}
