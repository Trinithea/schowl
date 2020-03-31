package cz.cizlmazna.schowl

import android.app.Application

class MainApplicationClass : Application() {

    companion object{
        private var darkMode: Boolean = true
        fun getDarkMode() : Boolean {
            return darkMode
        }
        fun setDarkMode(mode:Boolean){
            darkMode = mode
        }
    }
}