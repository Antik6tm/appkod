package fr.series.codemotard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import fr.series.codemotard.ui.etm.Prepopulate
import fr.series.codemotard.ui.etm.data.AppDatabase
import fr.series.codemotard.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Pré-remplissage de la base de données
        Prepopulate.prepopulateDatabase(this)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
    }
}
