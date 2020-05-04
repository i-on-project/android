package org.ionproject.android

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_main.toolbar_main

class MainActivity : AppCompatActivity() {

    /**
     * lazy initialization with ThreadSafetyMode to NONE because we are sure that
     * they will only be accessed via the UI thread, therefore we don't require a double-checked lock,
     * which is the default
     */
    private val topBar: Toolbar by lazy(LazyThreadSafetyMode.NONE) {
        toolbar_main
    }

    private val navController: NavController by lazy(LazyThreadSafetyMode.NONE) {
        findNavController(R.id.fragment_main_navhost)
    }

    private val sharedViewModel: SharedViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(
            this,
            SharedViewModelProvider()
        )[SharedViewModel::class.java]
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
    private fun setupBackButton() {
        onBackPressedDispatcher.addCallback(this) {
            navController.navigateUp()
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
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_bar_menu, menu)

        // Get the SearchView and set the searchable configuration.
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val searchView = menu.findItem(R.id.action_search)?.actionView as? SearchView

        searchView?.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))

            // Add search submit button
            isSubmitButtonEnabled = true

            /**
             * We must redefine the [setOnQueryTextListener] in order to get query introduced
             * by the user and pass it to the [SearchResultsFragment]. This is a fragment
             * responsible to present information depending on the query.
             */
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    queryTextSubmitBehaviour(query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })

            /**
             * We must redefine the [setOnSuggestionListener] in order to get the suggestion
             * query from the search view cursor's adapter to deliver it to the
             * [SearchResultsFragment].
             */
            setOnSuggestionListener(object : SearchView.OnSuggestionListener {
                override fun onSuggestionSelect(position: Int): Boolean {
                    return true
                }

                override fun onSuggestionClick(position: Int): Boolean {
                    val cursor = searchView.suggestionsAdapter.cursor
                    cursor.moveToPosition(position)
                    val suggestion: String = cursor.getString(2)
                    searchView.setQuery(suggestion,true)
                    return true
                }
            })
        }
        return true
    }

    /**
     * Called when user submits a search.
     * This method exists because otherwise onCreateOptionsMenu
     * becomes too large and loses legibility.
     *
     * @param query The query to search
     */
    private fun queryTextSubmitBehaviour(query: String) {
        val currDestination = navController.currentDestination?.id
        if (currDestination != null) {
            if (currDestination != R.id.navigation_search_results) {
                /**
                 *  Navigating to the SearchResultsFragment and ensuring
                 * that when the user presses the back button it returns to
                 * the destination from which search was called.
                 */
                navController.navigate(
                    R.id.navigation_search_results,
                    null,
                    NavOptions.Builder().setPopUpTo(
                        currDestination,
                        false
                    ).build()
                )
            }
            // Passing query text to [SearchResultsFragment]
            sharedViewModel.setQueryText(query)

            return
        }

        /**
         * When this method is called the backstack is never empty because we always have a fragment on display
         * within the nav_host. In the worst case only the home fragment is in the stack. This means that if this exception
         * is thrown something really bad must have happened.
         */
        throw IllegalStateException("Navigation back stack is empty!")
    }

    /**
     * Sets the Top bar as a custom toolbar especified in the res/layout/toolbar layout file
     * Enables the back button on the top bar
     */
    private fun setupTopBarBehaviour() {
        // Sets up the top action bar as a custom toolbar
        setSupportActionBar(topBar)

        // Add back button support
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

    private fun setupBottomBarBehaviour() {
        /**
         * Passing each menu ID as a set of Ids because each
         * menu should be considered as top level destinations.
         */

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_favorites,
                R.id.navigation_home,
                R.id.navigation_schedule
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
        bottomnavview_main.setupWithNavController(navController)
    }

}
