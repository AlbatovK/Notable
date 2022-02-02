package com.albatros.notable.ui.fragments.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albatros.notable.model.data.Note
import com.albatros.notable.model.repo.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(private val repo: MainRepository) : ViewModel() {

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.Main) { repo.deleteNote(note) }
    }
}