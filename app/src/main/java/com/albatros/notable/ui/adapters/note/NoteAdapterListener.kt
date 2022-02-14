package com.albatros.notable.ui.adapters.note

import android.view.View
import com.albatros.notable.model.data.Note

interface NoteAdapterListener {
    fun onNoteSelected(note: Note, view: View)
}