package com.albatros.notable.ui.fragments.creator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albatros.notable.domain.isEntryValid
import com.albatros.notable.model.data.Note
import com.albatros.notable.model.repo.MainRepository
import kotlinx.coroutines.launch

class CreatorViewModel(private val repo: MainRepository) : ViewModel() {

    private val _inputValid: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    val inputValid: LiveData<Boolean> = _inputValid

    fun processNote(title: String?, description: String?) {
        val valid = isEntryValid(title, description)
        _inputValid.value = if (valid) {
            val item = Note(title!!, description!!)
            viewModelScope.launch { repo.insertNote(item) }
            true
        } else false
    }
}

