package org.ionproject.android.offline

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_catalog_main.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.bottomnavview_main
import kotlinx.android.synthetic.main.activity_main.toolbar_main
import kotlinx.android.synthetic.main.toolbar_main.*
import org.ionproject.android.ExceptionHandlingActivity
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel
import org.ionproject.android.SharedViewModelProvider
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.addGradientBackground
import org.ionproject.android.common.model.Root
import org.ionproject.android.main.MAIN_ACTIVITY_ROOT_EXTRA

class CatalogMainActivity :  ExceptionHandlingActivity() {

    private var searchViewItem: MenuItem? = null

    private val sharedViewModel: CatalogSharedViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(
            this,
            CatalogSharedViewModelProvider()
        )[CatalogSharedViewModel::class.java]
    }

    private val navController: NavController by lazy(LazyThreadSafetyMode.NONE) {
        findNavController(R.id.catalog_fragment_main_navhost).apply {
            addOnDestinationChangedListener { _, destination, _ ->
                // Collapse search view if fragment destination is not Search Results Fragment
                if (destination.id != R.id.navigation_search_results)
                    searchViewItem?.collapseActionView()
                IonApplication.globalExceptionHandler.unRegisterCurrExceptionHandler()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog_main)
        activity_catalog_main.addGradientBackground()

        setupTopBarBehaviour()
        setupBottomBarBehaviour()
        setupBackButton()

        AlertDialog.Builder(this).setMessage(R.string.catalog_info_warning)
            .setPositiveButton("Ok", null).show()
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
                catalog_bottomnavview_main?.visibility = View.VISIBLE
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.catalog_top_bar, menu)

        val noConnectivity = menu.findItem(R.id.no_connection_action)

        this.searchViewItem = menu.findItem(R.id.catalog_action_search)

        val searchView = searchViewItem?.actionView as? SearchView

        searchView?.apply {
            isSubmitButtonEnabled = true

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                        sharedViewModel.setSearchText(newText ?: "")
                    return true
                }
            })
        }

        noConnectivity.setOnMenuItemClickListener {
            AlertDialog.Builder(this).setMessage(R.string.catalog_info_warning)
                .setPositiveButton("Ok", null).show()
            true
        }

        return true
    }

    /**
     * Sets the Top bar as a custom toolbar especified in the res/layout/toolbar layout file
     * Enables the back button on the top bar
     */
    private fun setupTopBarBehaviour() {
        // Sets up the top action bar as a custom toolbar
        setSupportActionBar(catalog_toolbar_main as Toolbar?)

        // Add back button support
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupBottomBarBehaviour() {
        /**
         * Passing each menu ID as a set of Ids because each
         * menu should be considered as top level destinations.
         */
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.catalog_programmes,
                R.id.catalog_Info
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
        catalog_bottomnavview_main.setupWithNavController(navController)
    }
}