package com.albatros.notable.domain

import java.util.*

val note_colors = listOf(
    "#ffD7E8DE" ,
    "#ffffab91" ,
    "#fff48fb1" ,
    "#ff81deea",
    "#b0d45d"   ,
    "#ffcf94da" ,
    "#ffe7ed9b" ,
)

fun getRandomColor(): Int {
    val index = Random().nextInt(note_colors.size)
    return note_colors.map(String::toColor)[index]
}
