package com.augistlk.irklavimas.presentation

interface Destinations{
    val route: String
}

object Settings: Destinations{
    override val route = "settings"
}

object Session: Destinations{
    override val route = "session"
}

object MainMenu: Destinations{
    override val route = "menu"
}