package com.codeop.recap.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.codeop.recap.R
import com.codeop.recap.adapter.ComicAdapter
import com.codeop.recap.databinding.FragmentFavoritesBinding
import com.codeop.recap.viewmodel.FavoritesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private lateinit var binding: FragmentFavoritesBinding
    private val favoritesViewModel: FavoritesViewModel by viewModel()

    private val adapter: ComicAdapter
        get() = binding.comicList.adapter as ComicAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Favorites"

        binding = FragmentFavoritesBinding.bind(view)

        binding.comicList.adapter = ComicAdapter {
            favoritesViewModel.removeComicAsFavorite(it)
        }

        favoritesViewModel.favorites.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        favoritesViewModel.getFavorites()
    }
}