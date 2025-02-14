package com.potaninpm.finaltour.navigation

sealed class RootNavDestinations(val route: String) {

    object Welcome : RootNavDestinations("welcome")

    object Home : RootNavDestinations("home")

    object Finances : RootNavDestinations("finances")

    object News : RootNavDestinations("news")
}