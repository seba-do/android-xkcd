package com.codeop.recap.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.codeop.recap.R
import com.codeop.recap.adapter.ComicAdapter
import com.codeop.recap.databinding.FragmentFavoritesBinding
import com.codeop.recap.repositories.FavoritesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var favoritesRepository: FavoritesRepository

    private val adapter: ComicAdapter
        get() = binding.comicList.adapter as ComicAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Favorites"

        binding = FragmentFavoritesBinding.bind(view)
        favoritesRepository = FavoritesRepository.getInstance(requireContext())

        binding.comicList.adapter = ComicAdapter {
            favoritesRepository.removeComicAsFavorite(it)
            updateListItems()
        }

        updateListItems()
    }

    private fun updateListItems() {
        CoroutineScope(Dispatchers.IO).launch {
            favoritesRepository.getAllFavorites().let {
                withContext(Dispatchers.Main) {
                    adapter.submitList(it)
                }
            }
        }
    }
}