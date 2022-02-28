package com.codeop.recap.fragments

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import coil.load
import com.codeop.recap.R
import com.codeop.recap.data.ComicResponse
import com.codeop.recap.databinding.FragmentComicsBinding
import com.codeop.recap.viewmodel.ComicViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ComicsFragment : Fragment(R.layout.fragment_comics) {
    private lateinit var binding: FragmentComicsBinding
    private val comicViewModel: ComicViewModel by viewModel()

    private var menu: Menu? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        binding = FragmentComicsBinding.bind(view)

        comicViewModel.currentComic.observe(viewLifecycleOwner) {
            it?.let { setComic(it) }
        }

        comicViewModel.isNextActive.observe(viewLifecycleOwner) {
            binding.btnNext.isEnabled = it
        }

        binding.btnNext.setOnClickListener { comicViewModel.getNextComic() }
        binding.btnPrevious.setOnClickListener { comicViewModel.getPreviousComic() }
        binding.btnRandom.setOnClickListener { comicViewModel.getRandomComic() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        inflater.inflate(R.menu.menu, menu)

        comicViewModel.isCurrentFavorite.observe(viewLifecycleOwner) {
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
            comicViewModel.switchFavoriteState()
        }

        return true
    }

    private fun setComic(comic: ComicResponse) {
        activity?.title = comic.title
        binding.comic.load(comic.img)
    }
}