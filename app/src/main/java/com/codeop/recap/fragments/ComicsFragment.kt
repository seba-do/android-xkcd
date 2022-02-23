package com.codeop.recap.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.codeop.recap.R
import com.codeop.recap.data.ComicResponse
import com.codeop.recap.databinding.FragmentComicsBinding
import com.codeop.recap.repositories.ComicRepository
import com.codeop.recap.repositories.FavoritesRepository
import com.codeop.recap.viewmodel.ComicViewModel
import com.codeop.recap.viewmodel.ComicViewModelFactory

class ComicsFragment : Fragment(R.layout.fragment_comics) {
    private lateinit var binding: FragmentComicsBinding
    private lateinit var viewModel: ComicViewModel

    private var menu: Menu? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = ViewModelProvider(
            requireActivity(),
            ComicViewModelFactory(
                ComicRepository.getInstance(requireContext()),
                FavoritesRepository.getInstance(requireContext())
            )
        ).get(ComicViewModel::class.java)

        binding = FragmentComicsBinding.bind(view)

        viewModel.currentComic.observe(viewLifecycleOwner) {
            it?.let { setComic(it) }
        }

        viewModel.isNextActive.observe(viewLifecycleOwner) {
            binding.btnNext.isEnabled = it
        }

        binding.btnNext.setOnClickListener { viewModel.getNextComic() }
        binding.btnPrevious.setOnClickListener { viewModel.getPreviousComic() }
        binding.btnRandom.setOnClickListener { viewModel.getRandomComic() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        inflater.inflate(R.menu.menu, menu)

        viewModel.isCurrentFavorite.observe(viewLifecycleOwner) {
            menu.findItem(R.id.like_icon)?.setIcon(
                if (it)
                    R.drawable.ic_favorite
                else
                    R.drawable.ic_favorite_border
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.like_icon) {
            viewModel.switchFavoriteState()
        }

        return true
    }

    private fun setComic(comic: ComicResponse) {
        activity?.title = comic.title
        binding.comic.load(comic.img)
    }
}