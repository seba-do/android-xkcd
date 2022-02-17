package com.codeop.recap.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.codeop.recap.MainActivity
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
    private lateinit var binding: FragmentComicsBinding
    private lateinit var favoritesRepository: FavoritesRepository
    private var currentComic: ComicResponse? = null
    private var menu: Menu? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentComicsBinding.bind(view)
        favoritesRepository = FavoritesRepository.getInstance(requireContext())

        setHasOptionsMenu(true)

        CoroutineScope(Dispatchers.IO).launch {
            ComicRepository.getNewestComic()?.let {
                withContext(Dispatchers.Main) {
                    currentComic = it
                    setComic(it)
                    setLikeIcon(it)
                }
            }
        }

        binding.comic.setOnTouchListener { view, motionEvent ->
            val gestureDetector =
                GestureDetector(view.context, object : GestureDetector.SimpleOnGestureListener() {

                    override fun onDoubleTap(e: MotionEvent?): Boolean {
                        Toast.makeText(requireContext(), "Double Tap", Toast.LENGTH_SHORT).show()
                        return super.onDoubleTap(e)
                    }
                })

            gestureDetector.onTouchEvent(motionEvent)
        }

        binding.btnNext.isEnabled = ComicRepository.comicNumber != ComicRepository.comicLimit
        binding.btnNext.setOnClickListener { switchComic(Direction.NEXT) }
        binding.btnPrevious.setOnClickListener { switchComic(Direction.PREVIOUS) }
        binding.btnRandom.setOnClickListener { switchComic(Direction.RANDOM) }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.like_icon) {
            currentComic?.let {
                favoritesRepository.addComicAsFavorite(it)
                setLikeIcon(it)
            }
        }

        return true
    }

    private fun switchComic(direction: Direction) {
        CoroutineScope(Dispatchers.IO).launch {
            when (direction) {
                Direction.NEXT -> ComicRepository.getNextComic()
                Direction.PREVIOUS -> ComicRepository.getPreviousComic()
                Direction.RANDOM -> ComicRepository.getRandomComic()
            }?.let {
                withContext(Dispatchers.Main) {
                    setComic(it)
                    setLikeIcon(it)
                    currentComic = it
                    binding.btnNext.isEnabled =
                        ComicRepository.comicNumber != ComicRepository.comicLimit
                }
            }
        }
    }

    private fun setComic(comic: ComicResponse) {
        activity?.title = comic.title
        binding.comic.load(comic.img)
    }

    private fun setLikeIcon(comic: ComicResponse) {
        menu?.findItem(R.id.like_icon)?.setIcon(
            if (favoritesRepository.isComicFavorite(comic))
                R.drawable.ic_favorite
            else
                R.drawable.ic_favorite_border
        )
    }

    enum class Direction {
        NEXT, PREVIOUS, RANDOM
    }
}