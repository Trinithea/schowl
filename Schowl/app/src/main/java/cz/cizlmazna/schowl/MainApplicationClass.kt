package cz.cizlmazna.schowl

import android.app.Application
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class MainApplicationClass : Application() {
    private var darkMode :Boolean = true
    override fun onCreate() {
        super.onCreate()
        // TODO Put your application initialization code here.
    }
    fun setDarkMode(darkmode:Boolean){
        darkMode = darkmode
    }
    fun getDarkMode():Boolean{
        return darkMode
    }
}