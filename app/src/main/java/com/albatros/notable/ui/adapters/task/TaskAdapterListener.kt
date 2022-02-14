package com.albatros.notable.ui.adapters.task

import android.widget.ImageView
import com.albatros.notable.model.data.SubTask

interface TaskAdapterListener {
    fun onTaskStateSet(task: SubTask, view: ImageView)

    fun onTaskFinished(task: SubTask, view: ImageView)
}