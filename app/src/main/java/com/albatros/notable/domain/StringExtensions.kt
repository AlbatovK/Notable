package com.albatros.notable.domain

fun isEntryValid(vararg args: String?) = args.all { !it.isNullOrEmpty()  }