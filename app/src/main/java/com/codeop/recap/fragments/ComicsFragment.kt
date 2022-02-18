package com.codeop.recap.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import coil.load
import com.codeop.recap.R
import com.codeop.recap.data.ComicResponse
import com.codeop.recap.databinding.FragmentComicsBinding
import com.codeop.recap.repositories.ComicRepository
import com.codeop.recap.repositories.FavoritesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ComicsFragment : Fragment(R.layout.fragment_comics) {
    companion object {
        private const val CURRENT_COMIC_KEY = "current-comic"
    }

    private lateinit var binding: FragmentComicsBinding
    private lateinit var favoritesRepository: FavoritesRepository
    private lateinit var comicRepository: ComicRepository

    private var currentComic: ComicResponse? = null
    private var menu: Menu? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentComicsBinding.bind(view)
        favoritesRepository = FavoritesRepository.getInstance(requireContext())
        comicRepository = ComicRepository.getInstance(requireContext())

        setHasOptionsMenu(true)

        CoroutineScope(Dispatchers.IO).launch {
            (savedInstanceState?.getParcelable(CURRENT_COMIC_KEY)
                ?: comicRepository.restoreLastComic())
                ?.let {
                    withContext(Dispatchers.Main) {
                        currentComic = it
                        setComic(it)
                        setLikeIcon(it)
                    }
                }
        }

        binding.btnNext.isEnabled = comicRepository.comicNumber != comicRepository.comicLimit
        binding.btnNext.setOnClickListener { switchComic(Direction.NEXT) }
        binding.btnPrevious.setOnClickListener { switchComic(Direction.PREVIOUS) }
        binding.btnRandom.setOnClickListener { switchComic(Direction.RANDOM) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(CURRENT_COMIC_KEY, currentComic)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.like_icon) {
            currentComic?.let {
                CoroutineScope(Dispatchers.IO).launch {
                    if (favoritesRepository.isComicFavorite(it)) {
                        favoritesRepository.removeComicAsFavorite(it)
                    } else {
                        favoritesRepository.addComicAsFavorite(it)
                    }

                    withContext(Dispatchers.Main) {
                        setLikeIcon(it)
                    }
                }
            }
        }

        return true
    }

    private fun switchComic(direction: Direction) {
        CoroutineScope(Dispatchers.IO).launch {
            when (direction) {
                Direction.NEXT -> comicRepository.getNextComic()
                Direction.PREVIOUS -> comicRepository.getPreviousComic()
                Direction.RANDOM -> comicRepository.getRandomComic()
            }?.let {
                withContext(Dispatchers.Main) {
                    setComic(it)
                    setLikeIcon(it)
                    currentComic = it
                    binding.btnNext.isEnabled =
                        comicRepository.comicNumber != comicRepository.comicLimit
                }
            }
        }
    }

    private fun setComic(comic: ComicResponse) {
        activity?.title = comic.title
        binding.comic.load(comic.img)
    }

    private fun setLikeIcon(comic: ComicResponse) {
        CoroutineScope(Dispatchers.IO).launch {
            val isFavorite = favoritesRepository.isComicFavorite(comic)

            withContext(Dispatchers.Main) {
                menu?.findItem(R.id.like_icon)?.setIcon(
                    if (isFavorite)
                        R.drawable.ic_favorite
                    else
                        R.drawable.ic_favorite_border
                )
            }
        }
    }

    enum class Direction {
        NEXT, PREVIOUS, RANDOM
    }
}