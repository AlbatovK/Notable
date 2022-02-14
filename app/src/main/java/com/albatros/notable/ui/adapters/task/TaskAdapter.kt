package com.albatros.notable.ui.adapters.task

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.albatros.notable.databinding.TaskLayoutBinding
import com.albatros.notable.model.data.SubTask

class TaskAdapter(
    private val tasks: List<SubTask>,
    private val listener: TaskAdapterListener
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.task = tasks[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    inner class TaskViewHolder(private val binding: TaskLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var task: SubTask? = null
            get() = field!!
            set(value) {
                field = value
                bind(value)
            }

        private fun bind(task: SubTask?) {
            task?.let {
                with(binding) {
                    listener.onTaskStateSet(task, binding.finishedState)
                    description.text = task.title
                    finishedState.setOnClickListener {
                        listener.onTaskFinished(task, binding.finishedState)
                    }
                }
            }
        }
    }
}