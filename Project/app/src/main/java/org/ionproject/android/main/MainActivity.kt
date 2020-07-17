package org.ionproject.android.main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_main.toolbar_main
import org.ionproject.android.*
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.addGradientBackground
import org.ionproject.android.common.model.Root
import org.ionproject.android.loading.ROOT_KEY
import org.ionproject.android.search.SearchSuggestionsProvider

class MainActivity : ExceptionHandlingActivity(),
    DeleteSuggestionsDialogFragment.OnDeleteSuggestionsDialogListener {

    /**
     * lazy initialization with ThreadSafetyMode to NONE because we are sure that
     * they will only be accessed via the UI thread, therefore we don't require a double-checked lock,
     * which is the default
     */
    private val topBar: Toolbar by lazy(LazyThreadSafetyMode.NONE) {
        toolbar_main
    }

    private var searchViewItem: MenuItem? = null

    private val navController: NavController by lazy(LazyThreadSafetyMode.NONE) {
        findNavController(R.id.fragment_main_navhost).apply {
            addOnDestinationChangedListener { _, destination, _ ->
                // Collapse search view if fragment destination is not Search Results Fragment
                if (destination.id != R.id.navigation_search_results)
                    searchViewItem?.collapseActionView()
                IonApplication.globalExceptionHandler.unRegisterCurrExceptionHandler()
            }
        }
    }

    private val sharedViewModel: SharedViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(
            this,
            SharedViewModelProvider()
        )[SharedViewModel::class.java]
    }

    private val viewModel: MainViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(
            this,
            MainViewModelProvider()
        )[MainViewModel::class.java]
    }

    private val deleteSuggestionsDialogFragment: DeleteSuggestionsDialogFragment by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        DeleteSuggestionsDialogFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observeConnectivity()
        main_activity.addGradientBackground()
        val root = intent.getParcelableExtra<Root>(ROOT_KEY)
        if (root != null) {
            sharedViewModel.root = root
            setupTopBarBehaviour()
            setupBottomBarBehaviour()
            setupBackButton()
        } else {
            throw IllegalArgumentException("Root is missing! Cannot load main activity without root.")
        }
    }

    /**
     * Starts observing the device connectivity
     * and shows dialog when its lost
     */
    private fun observeConnectivity() {
        val connectivityLostDialog = AlertDialog.Builder(this)
            .setTitle(resources.getString(R.string.title_warning))
            .setMessage(resources.getString(R.string.label_no_connectivity_main_activity))
            .setPositiveButton(android.R.string.ok) { _, _ -> }
            .create()

        // When application has been started, we want to check if there is already a network connectivity
        if (!viewModel.hasConnectivityBeenChecked() && !viewModel.hasConnectivity())
            connectivityLostDialog.show()

        viewModel.observeConnectivity {
            connectivityLostDialog.show()
        }
    }

    /**
     * Stops observing the device connectivity
     */
    override fun onDestroy() {
        super.onDestroy()
        IonApplication.connectivityObservable.stopObserving()
    }

    /**
     * Adds a callback to the onBackPressedDispatcher which will be called
     * when the user clicks the back button. This callback guarantees that
     * when the back button is pressed the navigation controller navigates
     * to the previous destination.
     */
    @SuppressLint("SourceLockedOrientationActivity")
    private fun setupBackButton() {
        onBackPressedDispatcher.addCallback(this) {
            navController.navigateUp()

            /*
             This ensures that if the user clicks the back button from android instead of
             the back button inside the schedule fragment, the top bar and bottom bar
             are added again.
             */
            val actionBar = supportActionBar
            if (actionBar != null && !actionBar.isShowing) {
                actionBar.show()
                bottomnavview_main?.visibility = View.VISIBLE
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
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
     * Handle action bar items clicks here.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_delete_suggestions) {
            deleteSuggestionsDialogFragment.show(supportFragmentManager, "Delete Suggestions")
            return true
        }
        return super.onOptionsItemSelected(item)
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
        searchViewItem = menu.findItem(R.id.action_search)
        val searchView = searchViewItem?.actionView as? SearchView

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
                    searchView.setQuery(suggestion, true)
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
            sharedViewModel.setSearchText(query)

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

    /**
     * If user wants to delete all search suggestions, this method will be called.
     */
    override fun onConfirmListener(dialog: DialogFragment) {
        // Delete all recent suggestion queries
        SearchRecentSuggestions(
            this,
            SearchSuggestionsProvider.AUTHORITY,
            SearchSuggestionsProvider.MODE
        ).clearHistory()
        Toast.makeText(this, resources.getString(R.string.toast_message_main), Toast.LENGTH_SHORT)
            .show()
    }

}
