package edu.isel.ion.android

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupTopBarBehaviour()

        setupBottomBarBehaviour()
    }

    /**
     * Instantiates the menu XML file @menu/top_bar_menu.xml into a Menu object.
     * Configures the search view by enabling assisted search and adding a search
     * submit button
     *
     * This method is called by the framework after OnCreate but before it finishes
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_bar_menu, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu?.findItem(R.id.action_search)?.actionView as SearchView).apply {
            // Assumes current activity is the searchable activity
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            isSubmitButtonEnabled = true //Add search submit button
        }
        return true
    }

    /**
     * Sets the Top bar as a custom toolbar especified in the res/layout/toolbar layout file
     * Enables the back button on the top bar
     */
    fun setupTopBarBehaviour() {
        //Sets up the top action bar as a custom toolbar
        setSupportActionBar(findViewById(R.id.toolbar_main))

        //Add back button support
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     *  Ensures that the buttons in the bottom navigation view work with the navigation controller
     *  so that when an item is selected the fragment container is updated with the according
     *  destination from the navigation graph. It also guarantees that the title in the action
     *  bar is updated with the name of the selected bottom bar item.
     *
     *  Note : The code inside this method came with the template activity with bottom navigation
     */
    fun setupBottomBarBehaviour() {
        val navView: BottomNavigationView = findViewById(R.id.bottomnavview_main)
        val navController = findNavController(R.id.fragment_main_navhost)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_favorites, R.id.navigation_home, R.id.navigation_schedule
            )
        )
        /**
         * Ensures the title in the action bar is updated according to the selected item
         * in the bottom navigation bar
         */
        setupActionBarWithNavController(navController, appBarConfiguration)
        /**
         * Sets up an {@link OnDestinationChangedListener} on the {@link BottomNavigationView} for
         * use with a {@link NavController}. This will call
         * {@link #onNavDestinationSelected(MenuItem, NavController)} when a menu item is selected.
         * The selected item in the BottomNavigationView will automatically be updated when the
         * destination changes.
         */
        navView.setupWithNavController(navController)
    }
}
