package com.albatros.notable.domain

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator

fun NavController.safeNavigate(direction: NavDirections, extras: FragmentNavigator.Extras) {
    currentDestination?.getAction(direction.actionId)?.run { navigate(direction, extras) }
}