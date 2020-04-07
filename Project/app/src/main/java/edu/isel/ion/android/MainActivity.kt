package edu.isel.ion.android

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    /*
    lazy initialization with ThreadSafetyMode to NONE because we are sure that
    they will only be accessed via the UI thread, therefore we don't require a double-checked lock,
    which is the default
     */
    private val topBar : Toolbar by lazy(LazyThreadSafetyMode.NONE) {
        findViewById<Toolbar>(R.id.toolbar_main)
    }
    private val navController : NavController by lazy(LazyThreadSafetyMode.NONE) {
        findNavController(R.id.fragment_main_navhost)
    }
    private val sharedViewModel : SharedViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this,SharedViewModelProvider())[SharedViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupTopBarBehaviour()

        setupBottomBarBehaviour()

        setupBackButton()
    }

    /**
     * Adds a callback to the onBackPressedDispacher which will be called
     * when the user clicks the back button. This callback guarantees that
     * when the back button is pressed the navigation controller navigates
     * to the previous destination.
     */
    fun setupBackButton() {
        onBackPressedDispatcher.addCallback(this) {
            navController.navigateUp()
            navController.popBackStack()
        }
    }

    /**
     * This method is called whenever the user chooses to navigate Up within your
     * application's activity hierarchy from the action bar.
     * We must override it because we removed the default action bar from the application
     * and added our own custom toolbar.
     * The method onBackPressed() calls the callbacks added to the onBackPressedDispacher
     */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
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
        val menuSearchItem = menu?.findItem(R.id.action_search)
        (menuSearchItem!!.actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            isSubmitButtonEnabled = true //Add search submit button
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    //topBar.collapseActionView() //Removing the top search area

                    if(navController.currentDestination!!.id != R.id.navigation_search_results) {
                        /* Navigating to the SearchResultsFragment and ensuring
                    that when the user presses the back button it returns to
                    the destination from which search was called. The query text
                    is passed as a bundle.

                    This approach is probably temporary, we are planning on having a shared viewmodel
                    between the fragments
                     */
                        navController.navigate(
                            R.id.navigation_search_results,
                            null,
                            NavOptions.Builder().setPopUpTo(navController.currentDestination!!.id,false)
                                .build()
                        )
                    }
                    /**Passing query text to [SearchResultsFragment]*/
                    sharedViewModel.searchText.value = query

                    return true;
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    //TODO While the code is changing
                    return true;
                }
            })
        }
        return true
    }

    /**
     * Sets the Top bar as a custom toolbar especified in the res/layout/toolbar layout file
     * Enables the back button on the top bar
     */
    fun setupTopBarBehaviour() {
        //Sets up the top action bar as a custom toolbar
        setSupportActionBar(topBar)

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
