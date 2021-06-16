package org.ionproject.android.userAPI

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_user_credentials.*
import kotlinx.android.synthetic.main.activity_user_credentials.view.*
import org.ionproject.android.ExceptionHandlingActivity
import org.ionproject.android.R
import org.ionproject.android.common.FetchFailure
import org.ionproject.android.common.FetchSuccess
import org.ionproject.android.common.IonApplication.Companion.preferences
import org.ionproject.android.common.addGradientBackground
import org.ionproject.android.common.model.Root
import org.ionproject.android.main.MAIN_ACTIVITY_ROOT_EXTRA
import org.ionproject.android.main.MainActivity
import org.ionproject.android.userAPI.models.AuthMethod
import org.ionproject.android.userAPI.models.PollResponse
import org.ionproject.android.userAPI.models.SelectedMethodResponse

const val USER_CREDENTIALS_ACTIVITY_ROOT_EXTRA = "UserCredentialsActivity.Root.Extra"

var AUTH_REQ_ID = ""

class UserCredentialsActivity : ExceptionHandlingActivity() {

    private val authOptionsArray: MutableList<AuthMethod> = mutableListOf()

    private val authTypesArray: MutableList<String> =
        mutableListOf() //idk why but it needs a list of strings for the spinner to work

    lateinit var pollResponse: PollResponse

    lateinit var adapter: ArrayAdapter<String>

    val handler = Handler()

    private val runnable: Runnable = object : Runnable {
        override fun run() {

            userAPIViewModel.pollCoreForEmailAuth()

            handler.postDelayed(this, 5000)
        }
    }

    private val userAPIViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, UserAPIViewModelProvider())[UserCredentialsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_credentials)
        activity_user_credentials.addGradientBackground()

        emailAuthTriggerButton.setOnClickListener {
            emailLoginButtonActions()
        }

        adapter =
            ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, authTypesArray)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        AuthChoicesSpinner.adapter = adapter

        AuthChoicesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent != null) {
                    if (parent.selectedItem.toString() == "email")
                        setupEmailAuth(position)
                    else
                        Toast.makeText(
                            applicationContext,
                            "No other solutions implemented yet",
                            Toast.LENGTH_SHORT
                        ).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        /**
         * Since this activity is going to be in between loading and main, we need to carry the
         * root object from one to the other
         */
        /**
         * Since this activity is going to be in between loading and main, we need to carry the
         * root object from one to the other
         */
        val root = intent.getParcelableExtra<Root>(USER_CREDENTIALS_ACTIVITY_ROOT_EXTRA)
            ?: throw IllegalArgumentException("Root is missing! Cannot load main activity without root.")

        /**
         * Response from the first request, where we get the available auth methods
         *
         * when we get the response, we oppulate the options arrays and create the View
         * for the chosen method
         */
        userAPIViewModel.observeAvailableAuthMethods(this) {
            when (it) {
                is FetchSuccess<List<AuthMethod>> -> {
                    authOptionsArray.addAll(it.value as MutableList<AuthMethod>)
                    authTypesArray.addAll(it.value.map { it.type })
                    adapter.notifyDataSetChanged()
                }
                is FetchFailure<List<AuthMethod>> -> { //unsuccessful request
                    Log.d("API", "Failure $it")
                    //go to catalog in case of failure?
                }
            }
        }

        /**
         * After the user presses the login button, a request is made to the Core.
         *
         * This observer waits for that response and saves the request ID for the POLL
         * request that follows
         *
         * Since the user has to open an email client and grant permissions to the API,
         * we run a handler that checks for updates every 5 seconds
         */
        userAPIViewModel.observeLoginResponse(this) {
            when (it) {
                is FetchSuccess<SelectedMethodResponse> -> {
                    AUTH_REQ_ID = it.value.auth_req_id
                    runnable.run() //start polling the server
                }
                is FetchFailure<SelectedMethodResponse> -> { //unsuccessful request
                    AlertDialog.Builder(this)
                        .setMessage("There was a problem in the login response")
                        .setPositiveButton("Ok", null).show()
                }
            }
        }

        /**
         * When the POLL response arrives, we either go to the Main Activity and resume normal
         * usage or we go to the catalog (?) navigation in case there is a problem with the
         * authentication system
         */
        userAPIViewModel.observePollResponse(this) {
            when (it) {
                is FetchSuccess<PollResponse> -> {
                    pollResponse = it.value
                    preferences.saveAccessToken(it.value.access_token) //save the access token in the shared preferences
                    preferences.saveRefreshToken(it.value.refresh_token)//setting the refresh token in the shared preferences
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra(MAIN_ACTIVITY_ROOT_EXTRA, root)
                    handler.removeCallbacks(runnable) //stop the handler from polling the server after a successful login
                    this.startActivity(intent)
                }
            }
        }
    }

    /**
     * Display all the views relevant to this type of authentication
     */
    private fun setupEmailAuth(position: Int) {
        ionLogoView.visibility = View.VISIBLE
        emailInputEditText.visibility = View.VISIBLE
        allowedDomainsContentTextView.visibility = View.VISIBLE
        allowedDomainsTextView.visibility = View.VISIBLE
        emailAuthTriggerButton.visibility = View.VISIBLE

        //display the allowed domains from the Core response
        activity_user_credentials.allowedDomainsContentTextView.text =
            authOptionsArray[position].allowed_domains.toString().replace("[", "")
                .replace("]", "")
    }

    /**
     * Input verification and login trigger
     */
    private fun emailLoginButtonActions() {
        val input = emailInputEditText.text.toString()

        if (input == "")
            AlertDialog.Builder(this).setMessage(R.string.email_input)
                .setPositiveButton("Ok", null).show()

        val domain = input.split("@alunos")[1]

        if (authOptionsArray.find { it.type == "email" }?.allowed_domains?.contains("*$domain") == true) {
            userAPIViewModel.loginWithEmail(emailInputEditText.text.toString())
            AlertDialog.Builder(this).setMessage(R.string.check_email)
                .setPositiveButton("Ok", null).show()
        } else
            AlertDialog.Builder(this).setMessage(R.string.invalid_email)
                .setPositiveButton("Ok", null).show()
    }
}