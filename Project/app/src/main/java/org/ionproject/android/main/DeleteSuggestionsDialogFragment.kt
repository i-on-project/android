package org.ionproject.android.main

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import org.ionproject.android.R

class DeleteSuggestionsDialogFragment : DialogFragment() {
    // Instance of the interface to deliver action events
    private lateinit var listener: OnDeleteSuggestionsDialogListener

    /**
     * [MainActivity]  must implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the [MainActivity] needs to query it.
     */
    interface OnDeleteSuggestionsDialogListener {
        fun onConfirmListener(dialog: DialogFragment)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.dialog_main)
                .setPositiveButton(
                    R.string.dialog_button_yes,
                    DialogInterface.OnClickListener { _, _ ->
                        listener.onConfirmListener(this)
                    })
                .setNegativeButton(
                    R.string.dialog_button_cancel,
                    DialogInterface.OnClickListener { _, _ ->

                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    // Override the Fragment.onAttach() method to instantiate the OnDeleteSuggestionsDialogListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify if MainActivity implements the callback interface
        try {
            // Instantiate the OnDeleteSuggestionsDialogListener so we can send events to the host
            listener = context as OnDeleteSuggestionsDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(
                (context.toString() +
                        " must implement OnDeleteSuggestionsDialogListener")
            )
        }
    }
}