package com.albatros.notable.domain

import android.graphics.Color

fun isEntryValid(vararg args: String?) = args.all { !it.isNullOrEmpty() }

fun String.toColor() = Color.parseColor(this)