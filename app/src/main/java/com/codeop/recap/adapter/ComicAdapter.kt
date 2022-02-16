package com.codeop.recap.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.codeop.recap.R
import com.codeop.recap.data.ComicResponse
import com.codeop.recap.databinding.VhComicBinding

class ComicAdapter(private val onFavoriteClick: (ComicResponse) -> Unit) :
    ListAdapter<ComicResponse, ComicAdapter.ComicViewHolder>(ComicDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        return ComicViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        holder.bind(getItem(position), onFavoriteClick)
    }

    class ComicViewHolder private constructor(
        private val binding: VhComicBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(comic: ComicResponse, onFavoriteClick: (ComicResponse) -> Unit) = with(binding) {
            preview.load(comic.img)
            btnFavorite.apply {
                setImageResource(R.drawable.ic_favorite)
                setOnClickListener { onFavoriteClick(comic) }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ComicViewHolder {
                val binding = VhComicBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                return ComicViewHolder(binding)
            }
        }
    }

    class ComicDiffUtil : DiffUtil.ItemCallback<ComicResponse>() {
        override fun areItemsTheSame(oldItem: ComicResponse, newItem: ComicResponse): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ComicResponse, newItem: ComicResponse): Boolean {
            return oldItem.num == newItem.num
        }
    }
}