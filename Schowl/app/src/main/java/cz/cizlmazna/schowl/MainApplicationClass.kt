package cz.cizlmazna.schowl

import android.app.Application

class MainApplicationClass : Application() {
    private var darkMode: Boolean = true

    fun setDarkMode(darkMode: Boolean) {
        this.darkMode = darkMode
    }

    fun getDarkMode(): Boolean {
        return darkMode
    }
}