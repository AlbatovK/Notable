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

    fun loadNotesList() {
        viewModelScope.launch(Dispatchers.Main) {
            _notes.value = repo.getNotes()
        }
    }

    val notes: LiveData<List<Note>?> = _notes

    fun fetchByTopics(topics: String) {
        val topicsList = topics.lowercase().split(" ")
        viewModelScope.launch(Dispatchers.Main) {
            _notes.value = repo.getNotes().filter { note ->
                val noteWords = (note.data + " " + note.title).lowercase().trimIndent().split(" ")
                topicsList.any {
                    it.trim() in noteWords
                }
            }
        }
    }

}