package com.albatros.notable.model.module

import com.albatros.notable.ui.fragments.creator.CreatorViewModel
import com.albatros.notable.ui.fragments.detail.DetailViewModel
import com.albatros.notable.ui.fragments.list.ListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ListViewModel(get()) }
    viewModel { CreatorViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}