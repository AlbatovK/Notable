package com.albatros.notable.domain

import android.content.Context
import com.albatros.notable.R
import java.util.Random

val note_colors = listOf(
    R.color.light_blue,
    R.color.red_orange,
    R.color.red_pink,
    R.color.baby_blue,
    R.color.neon_green,
    R.color.violet,
    R.color.light_green,
)

fun getRandomColor(context: Context): Int {
    val index = Random().nextInt(note_colors.size)
    return note_colors.map { context.resources.getColor(it, context.theme) }[index]
}
