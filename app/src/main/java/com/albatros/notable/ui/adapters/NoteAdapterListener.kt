package com.albatros.notable.ui.adapters

import android.view.View
import com.albatros.notable.model.data.Note

interface NoteAdapterListener {
    fun onNoteSelected(note: Note, view: View)
}