package com.albatros.notable.domain

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator

fun NavController.safeNavigate(direction: NavDirections, extras: FragmentNavigator.Extras) {
    currentDestination?.getAction(direction.actionId)?.run { navigate(direction, extras) }
}

@Suppress("unused")
fun NavController.safeNavigate(@IdRes currentId: Int, @IdRes id: Int, args: Bundle? = null) {
    if (currentId == currentDestination?.id) { navigate(id, args) }
}