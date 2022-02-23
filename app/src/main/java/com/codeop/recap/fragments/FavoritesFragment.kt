package com.codeop.recap.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.codeop.recap.R
import com.codeop.recap.adapter.ComicAdapter
import com.codeop.recap.databinding.FragmentFavoritesBinding
import com.codeop.recap.repositories.FavoritesRepository
import com.codeop.recap.viewmodel.FavoritesViewModel
import com.codeop.recap.viewmodel.FavoritesViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var viewModel: FavoritesViewModel
//    private lateinit var favoritesRepository: FavoritesRepository

    private val adapter: ComicAdapter
        get() = binding.comicList.adapter as ComicAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Favorites"

        viewModel = ViewModelProvider(
            requireActivity(),
            FavoritesViewModelFactory(
                FavoritesRepository.getInstance(requireContext())
            )
        ).get(FavoritesViewModel::class.java)

        binding = FragmentFavoritesBinding.bind(view)
//        favoritesRepository = FavoritesRepository.getInstance(requireContext())

        binding.comicList.adapter = ComicAdapter {
            viewModel.removeComicAsFavorite(it)
//            CoroutineScope(Dispatchers.IO).launch {
//                favoritesRepository.removeComicAsFavorite(it)
//            }
//            updateListItems()
        }

        viewModel.favorites.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.getFavorites()

//        updateListItems()
    }

//    private fun updateListItems() {
//        CoroutineScope(Dispatchers.IO).launch {
//            favoritesRepository.getAllFavorites().let {
//                withContext(Dispatchers.Main) {
//                    adapter.submitList(it)
//                }
//            }
//        }
//    }
}