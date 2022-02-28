package com.codeop.recap.di

import com.codeop.recap.db.DBConnection
import com.codeop.recap.repositories.ComicRepository
import com.codeop.recap.repositories.FavoritesRepository
import com.codeop.recap.viewmodel.ComicViewModel
import com.codeop.recap.viewmodel.FavoritesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object KoinGraph {
    val mainModule = module {
        single { DBConnection(get()) }
        single { ComicRepository() }
        single { FavoritesRepository() }

        viewModel { ComicViewModel(get(), get()) }
        viewModel { FavoritesViewModel(get()) }
    }
}