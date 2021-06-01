package org.ionproject.android.userAPI

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_user_credentials.*
import kotlinx.android.synthetic.main.activity_user_credentials.view.*
import org.ionproject.android.ExceptionHandlingActivity
import org.ionproject.android.R
import org.ionproject.android.common.FetchFailure
import org.ionproject.android.common.FetchSuccess
import org.ionproject.android.common.addGradientBackground
import org.ionproject.android.common.model.Root
import org.ionproject.android.userAPI.models.AuthMethod
import org.ionproject.android.userAPI.models.SelectedMethodResponse

const val USER_CREDENTIALS_ACTIVITY_ROOT_EXTRA = "UserCredentialsActivity.Root.Extra"

class UserCredentialsActivity : ExceptionHandlingActivity(){

    private val authOptionsArray: MutableList<AuthMethod> = mutableListOf()

    private val authTypesArray: MutableList<String> = mutableListOf() //idk why but it needs a list of strings for the spinner to work

    lateinit var adapter: ArrayAdapter<String>

    private val userAPIViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, UserAPIViewModelProvider())[UserCredentialsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_credentials)
        activity_user_credentials.addGradientBackground()

        emailAuthTriggerButton.setOnClickListener {

            val input = emailInputEditText.text.toString()

            if(input == "")
                AlertDialog.Builder(this).setMessage(R.string.email_input)
                    .setPositiveButton("Ok", null).show()

            val domain = input.split("@alunos")[1]

            Log.d("API", domain)

            if(authOptionsArray.find{ it.type == "email"}?.allowed_domains?.contains("*$domain") == true)
                userAPIViewModel.loginWithEmail(emailInputEditText.text.toString())
            else
                AlertDialog.Builder(this).setMessage(R.string.invalid_email)
                    .setPositiveButton("Ok", null).show()
        }

        adapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, authTypesArray)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        AuthChoicesSpinner.adapter = adapter

        AuthChoicesSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                setupEmailAuth()
                activity_user_credentials.allowedDomainsContentTextView.text =
                    authOptionsArray[position].allowed_domains.toString().replace("[", "")
                        .replace("]", "")
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

        userAPIViewModel.observeLoginResponse(this){
            when (it) {
                is FetchSuccess<SelectedMethodResponse> -> {

                }
                is FetchFailure<SelectedMethodResponse> -> { //unsuccessful request
                    Log.d("API", "Failure $it")
                    //go to catalog in case of failure?
                }
            }
        }
    }

    /**
     * Display all the views relevant to this type of authentication
     */
    private fun setupEmailAuth(){
        ionLogoView.visibility = View.VISIBLE
        emailInputEditText.visibility = View.VISIBLE
        allowedDomainsContentTextView.visibility = View.VISIBLE
        allowedDomainsTextView.visibility = View.VISIBLE
        emailAuthTriggerButton.visibility = View.VISIBLE
    }
}