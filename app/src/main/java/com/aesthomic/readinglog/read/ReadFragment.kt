package com.aesthomic.readinglog.read


import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.aesthomic.readinglog.R
import com.aesthomic.readinglog.app.di.Scopes
import com.aesthomic.readinglog.customview.SwipeToDeleteCallback
import com.aesthomic.readinglog.database.Read
import com.aesthomic.readinglog.database.ReadingLogDatabase
import com.aesthomic.readinglog.databinding.FragmentReadBinding
import com.aesthomic.readinglog.readbook.ReadBookViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.getKoin

class ReadFragment : Fragment() {

    private lateinit var binding: FragmentReadBinding
    private lateinit var viewModel: ReadViewModel
    private lateinit var adapter: ReadAdapter

    private var readKeyDelete = -1L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_read, container, false)

        initViewModel()
        initRecyclerView()
        enableSwipeToDelete()
        setFABVisibility()
        killReadBookScope()

        viewModel.navigateToReadBook.observe(this, Observer {
            it?.let {
                this.findNavController().navigate(
                    ReadFragmentDirections.
                        actionReadDestinationToReadBookDestination(it.id, -1L)
                )
                viewModel.onNavigateReadBookDone()
            }
        })

        viewModel.navigateToDetail.observe(this, Observer {
            it?.let {
                this.findNavController().navigate(
                    ReadFragmentDirections.
                        actionReadDestinationToReadDetailDestination(it)
                )
                viewModel.onNavigateDetailDone()
            }
        })

        viewModel.fabState.observe(this, Observer {
            if (it) {
                binding.fabRead.setImageResource(R.drawable.ic_play_arrow)
                binding.fabRead.imageTintList = ColorStateList.
                    valueOf(ContextCompat.getColor(requireContext(), R.color.colorStart))
            } else {
                binding.fabRead.setImageResource(R.drawable.ic_stop)
                binding.fabRead.imageTintList = ColorStateList.
                    valueOf(ContextCompat.getColor(requireContext(), R.color.colorStop))
            }
        })

        viewModel.deleteEnable.observe(this, Observer {
            if (it) {
                binding.fabDelete.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.colorFabDelete))
            } else {
                binding.fabDelete.backgroundTintList = ColorStateList.valueOf(Color.GRAY)
            }
        })

        viewModel.read.observe(this, Observer {
            if (it?.id == readKeyDelete) {
                viewModel.deleteReadById(readKeyDelete)

                setUndoSnackbar(it)
                viewModel.setReadKey(-1L)
            }
        })

        return binding.root
    }

    private fun setFABVisibility() {
        binding.nsvRead.setOnScrollChangeListener { v: NestedScrollView?, _,
                                                    scrollY: Int, _, oldScrollY: Int ->
            if (scrollY > oldScrollY) {
                binding.fabDelete.hide()
                binding.fabRead.hide()
            } else {
                binding.fabDelete.show()
                binding.fabRead.show()
            }
        }
    }

    private fun killReadBookScope() {
        val readBookScope = if (getKoin().getScopeOrNull(Scopes.READ_BOOK) != null) {
            getKoin().getScope(Scopes.READ_BOOK)
        } else {null}

        readBookScope?.let {
            val readBookViewModel: ReadBookViewModel = it.get()
            readBookViewModel.onClearState()
            it.close()
        }
    }

    private fun initRecyclerView() {
        binding.rvRead.layoutManager = LinearLayoutManager(requireContext())

        adapter = ReadAdapter(ReadListener {
            viewModel.onReadClicked(it)
        })

        binding.rvRead.adapter = adapter

        viewModel.readBook.observe(this, Observer {
            it?.let {
                adapter.addSubmitList(it)
            }
        })
    }

    private fun initViewModel() {
        val application = requireNotNull(this.activity).application
        val database = ReadingLogDatabase.getInstance(application).readDao

        val viewModelFactory = ReadViewModelFactory(database, application)
        viewModel = ViewModelProviders.of(
            this, viewModelFactory).get(ReadViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun enableSwipeToDelete() {
        val swipeToDeleteCallback = object: SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                readKeyDelete = adapter.getItemId(position)
                viewModel.setReadKey(readKeyDelete)

            }

        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvRead)

    }

    private fun setUndoSnackbar(read: Read) {
        val snackbar = Snackbar
            .make(binding.clRead, "Item was removed", Snackbar.LENGTH_LONG)
            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
        snackbar.setAction("UNDO") {
            viewModel.insertRead(read)
        }

        if (binding.fabDelete.isShown) {
            snackbar.anchorView = binding.fabDelete
        }

        snackbar.setActionTextColor(Color.YELLOW)
        snackbar.show()
    }


}
