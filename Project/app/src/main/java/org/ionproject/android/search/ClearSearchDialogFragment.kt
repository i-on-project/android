package org.ionproject.android.search

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

/**
 * A simple [Fragment] subclass.
 */
class ClearSearchDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)

            builder.setMessage("Are you sure you want to delete all your recent search queries?")
                .setPositiveButton("Yes") { _, _ ->
                    SearchSuggestionProvider.clearHistory(context)
                    Toast.makeText(
                        context,
                        "Recent search history has been cleared",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .setNegativeButton("No") { _, _ -> dismiss() }
            builder.create()
        } ?: throw IllegalArgumentException("Activity cannot be null")
    }

}
