package com.albatros.notable.ui.fragments.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albatros.notable.model.data.Note
import com.albatros.notable.model.data.SubTask
import com.albatros.notable.model.repo.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(private val repo: MainRepository) : ViewModel() {

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.Main) {
            repo.deleteTasksByNoteId(note.id)
            repo.deleteNote(note)
        }
    }

    fun insertSubTask(task: SubTask) {
        _doneCount.value = Pair(_doneCount.value?.first ?: 0, (_doneCount.value?.second ?: 0) + 1)
        viewModelScope.launch(Dispatchers.Main) {
            repo.insertSubTask(task)
            loadSubTasks()
        }
    }

    fun finishNote(note: Note): Note {
        return note.copy().apply {
            this.finished = true
        }.let {
            viewModelScope.launch(Dispatchers.Main) { repo.updateNote(it) }
            it
        }
    }

    fun deleteTask(task: SubTask) {
        viewModelScope.launch(Dispatchers.Main) {
            repo.deleteSubTask(task)
            loadSubTasks()
            _percentCount.value = countPercentage()
            val tasks = repo.getTasksByNoteId(noteId)
            _doneCount.value = Pair(tasks.count { task -> task.finished }, tasks.size)
            if (_percentCount.value == 100.0) {
                val parent = repo.getNoteById(noteId).firstOrNull()
                parent?.let { note -> finishNote(note) }
                _finished.value = true
            }
        }
    }

    private val _percentCount: MutableLiveData<Double> = MutableLiveData<Double>()

    val percentCount: LiveData<Double> = _percentCount

    private val _doneCount: MutableLiveData<Pair<Int, Int>> = MutableLiveData<Pair<Int, Int>>()

    val doneCount: LiveData<Pair<Int, Int>> = _doneCount

    private var noteId: Long = 0

    fun initViewModel(id: Long) {
        this.noteId = id
        viewModelScope.launch(Dispatchers.Main) {
            _percentCount.value = countPercentage()
            val tasks = repo.getTasksByNoteId(noteId)
            _doneCount.value = Pair(tasks.count { task -> task.finished }, tasks.size)
        }
    }

    fun finishTask(task: SubTask): SubTask {
        return task.copy().apply {
            this.finished = true
        }.let {
            viewModelScope.launch(Dispatchers.Main) {
                repo.updateSubTask(it)
                _percentCount.value = countPercentage()
                val tasks = repo.getTasksByNoteId(noteId)
                _doneCount.value = Pair(tasks.count { task -> task.finished }, tasks.size)
                if (_percentCount.value == 100.0) {
                    val parent = repo.getNoteById(noteId).firstOrNull()
                    parent?.let { note -> finishNote(note) }
                    _finished.value = true
                }
            }
            it
        }
    }

    private val _finished: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    val finished: LiveData<Boolean> = _finished

   private suspend fun countPercentage(): Double {
        val tasks = repo.getTasksByNoteId(noteId)
        return tasks.count { it.finished }.toDouble() / tasks.size.toDouble() * 100
    }

    private val _tasks: MutableLiveData<List<SubTask>> = MutableLiveData<List<SubTask>>()

    val tasks: LiveData<List<SubTask>> = _tasks

    fun loadSubTasks() {
        viewModelScope.launch(Dispatchers.Main) {
            _tasks.value = repo.getTasksByNoteId(noteId)
        }
    }
}