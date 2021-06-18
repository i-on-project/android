package org.ionproject.android.main

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.SystemClock
import android.provider.SearchRecentSuggestions
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.widget.SearchView
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
import kotlinx.coroutines.runBlocking
import org.ionproject.android.ExceptionHandlingActivity
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel
import org.ionproject.android.SharedViewModelProvider
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.addGradientBackground
import org.ionproject.android.common.model.Root
import org.ionproject.android.common.repositories.FavoriteRepository
import org.ionproject.android.loading.LoadingActivity
import org.ionproject.android.search.SearchSuggestionsProvider
import org.ionproject.android.userAPI.AlarmReceiver
import java.net.URI

const val MAIN_ACTIVITY_ROOT_EXTRA = "MainActivity.Root.Extra"
const val MAIN_ACTIVITY_STALE_TOKEN_EXTRA = "MainActivity.Token.Extra"

class MainActivity : ExceptionHandlingActivity(),
    DeleteSuggestionsDialogFragment.OnDeleteSuggestionsDialogListener {

    private lateinit var alarmIntent: Intent

    private lateinit var pendingIntent: PendingIntent

    private var searchViewItem: MenuItem? = null

    private lateinit var alarmManager: AlarmManager

    /**
    This flag is used to let the Main Activity know if the user had to go through the
    authentication process or if they were already logged in.

    True: token was stale (arleady logged in)
    False: fresh token (had to login)
     */
    private var staleTokenFlag: Boolean = true

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

    /**
     * If the root intent is null, it means the app is outdated
     *
     * Creates instances of the alarmIntent and pendingIntent required for the AlarmManager
     * that refreshes the AccessToken every 20 minutes
     *
     * Also syncs the remote favorites list with the local favorites list when the app is starting up
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main_activity.addGradientBackground()
        val root = intent.getParcelableExtra<Root>(MAIN_ACTIVITY_ROOT_EXTRA)

        staleTokenFlag = intent.getBooleanExtra(MAIN_ACTIVITY_STALE_TOKEN_EXTRA, true)

        if (root != null) {
            sharedViewModel.root = root
            setupTopBarBehaviour()
            setupBottomBarBehaviour()
            setupBackButton()

            alarmIntent = Intent(this, AlarmReceiver::class.java)

            pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0)

            viewModel.syncLocalFavoritesWithRemoteFavorites()
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
            .setTitle(resources.getString(R.string.title_warning_all))
            .setMessage(resources.getString(R.string.label_no_connectivity_main))
            .setPositiveButton(android.R.string.ok) { _, _ -> }
            .create()

        viewModel.observeConnectivity {
            connectivityLostDialog.show()
        }
    }

    /**
     * Start observing the connection and setup the alarm for access token refresh
     */
    override fun onStart() {
        super.onStart()

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        /**
         * If the user didn't go through the authentication process,
         * refresh the access token stored locally
         */
        if (staleTokenFlag) {
            runBlocking {
                AlarmReceiver().refreshAccessToken(applicationContext)
            }
        }

        /**
         * Set up an alarm to refresh the token every 20 minutes
         */
        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + (30 * 60 * 1000),
            (30 * 60 * 1000),
            pendingIntent
        )

        observeConnectivity()
    }

    /**
     * Stops observing the device connectivity
     *
     * Cancel the alarm manager to stop the refresh attempts
     */
    override fun onStop() {
        super.onStop()
        IonApplication.connectivityObservable.stopObserving()

        alarmManager.cancel(pendingIntent)
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
     * Instantiates the menu XML file @menu/top_bar_menu.xml into a Menu object.
     * Configures the search view by enabling assisted search and adding a search
     * submit button
     *
     * This method is called by the framework after OnCreate but before it finishes
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_bar_menu, menu)

        val searchViewItem = menu.findItem(R.id.action_search)
        val deleteSuggestionsItem = menu.findItem(R.id.action_delete_suggestions)
        val logoutItem = menu.findItem(R.id.action_logout)

        // If Root did not bring the searchUri we don't add the functionality
        // and we hide all the buttons
        if (sharedViewModel.root?.searchUri == null) {
            searchViewItem.isVisible = false
            deleteSuggestionsItem.isVisible = false
        } else {
            // Get the SearchView and set the searchable configuration.
            val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
            this.searchViewItem = searchViewItem
            val searchView = searchViewItem?.actionView as? SearchView

            // Set search menu item behaviour
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

            // Set deleteSuggestions menu item behaviour
            deleteSuggestionsItem.setOnMenuItemClickListener {
                deleteSuggestionsDialogFragment.show(supportFragmentManager, "Delete Suggestions")
                true
            }

            //remove the user credentials and redirect to the loading activity
            logoutItem.setOnMenuItemClickListener {
                AlertDialog.Builder(this).setMessage(R.string.are_you_sure_logout)
                    .setPositiveButton("Ok") { _, _ ->
                        IonApplication.preferences.saveAccessToken("")
                        IonApplication.preferences.saveRefreshToken("")
                        val intent = Intent(this, LoadingActivity::class.java)
                        this.startActivity(intent)
                    }.show()
                true
            }
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
        setSupportActionBar(toolbar_main)

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
        Toast.makeText(
            this,
            resources.getString(R.string.toast_search_suggestions_deleted_message_main),
            Toast.LENGTH_SHORT
        )
            .show()
    }

}
