package com.albatros.notable.ui.fragments.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albatros.notable.model.data.Note
import com.albatros.notable.model.repo.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListViewModel(private val repo: MainRepository) : ViewModel() {

    private val _notes = MutableLiveData<List<Note>?>().apply {
        viewModelScope.launch(Dispatchers.Main) {
            value = repo.getNotes()
        }
    }

    val notes: LiveData<List<Note>?> = _notes
}