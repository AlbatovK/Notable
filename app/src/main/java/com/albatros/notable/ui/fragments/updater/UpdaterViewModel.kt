package com.albatros.notable.ui.fragments.updater

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albatros.notable.domain.isEntryValid
import com.albatros.notable.model.data.Note
import com.albatros.notable.model.repo.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdaterViewModel(private val repo: MainRepository) : ViewModel() {

    private val _inputValid: MutableLiveData<Note?> = MutableLiveData<Note?>()

    val inputValid: LiveData<Note?> = _inputValid

    fun processNote(note: Note, title: String?, description: String?) {
        val valid = isEntryValid(title, description)
        _inputValid.value = if (valid) {
            val new = note.copy().apply {
                this.title = title!!.trim()
                this.data = description!!.trim()
            }
            viewModelScope.launch(Dispatchers.Main) { repo.updateNote(new) }
            new
        } else null
    }
}