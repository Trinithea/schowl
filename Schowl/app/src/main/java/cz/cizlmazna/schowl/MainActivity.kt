package cz.cizlmazna.schowl

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import cz.cizlmazna.schowl.databinding.ActivityMainBinding
import kotlin.math.roundToInt

public class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private var Darkmode:Boolean = true
    private val topLevelDestinations = setOf(
        R.id.nav_test, R.id.nav_subjects, R.id.nav_settings, R.id.nav_about
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            topLevelDestinations, drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    override fun onBackPressed() {

        val navController = findNavController(R.id.nav_host_fragment)
        if (topLevelDestinations.contains(navController.currentDestination?.id)) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    fun setDarkMode(darkmode:Boolean){
        (application as MainApplicationClass).setDarkMode(darkmode)
    }
    fun getDarkMode():Boolean{
        return (application as MainApplicationClass).getDarkMode()
    }
    fun dpToPx(dp: Int): Int {
        val displayMetrics = this.resources.displayMetrics
        return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }
}
