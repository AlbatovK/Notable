package com.albatros.notable.ui.adapters.note

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.albatros.notable.R
import com.albatros.notable.databinding.NoteLayoutBinding
import com.albatros.notable.model.data.Note

class NoteAdapter(
    private val notes: MutableList<Note>,
    private val listener: NoteAdapterListener
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    override fun getItemCount(): Int = notes.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) { holder.note = notes[position] }

    inner class NoteViewHolder(private val binding: NoteLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        var note: Note? = null
            get() = field!!
            set(value) {
                field = value
                bind(value)
            }

        private fun bind(note: Note?) {
            note?.let {
                with(binding) {
                    ViewCompat.setTransitionName(cardView, note.id.toString())
                    cardView.setCardBackgroundColor(note.color)
                    title.text = note.title
                    description.text = note.data

                    cardView.startAnimation(AnimationUtils.loadAnimation(binding.root.context, R.anim.alpha_animation))
                    cardView.visibility = View.VISIBLE
                    if (note.finished)
                        doneImg.setImageResource(R.drawable.ic_check)

                    root.setOnClickListener {
                        /* Safe navigation check */
                        try { listener.onNoteSelected(note, it) } catch (e: IllegalArgumentException) {}
                    }
                }
            }
        }
    }
}