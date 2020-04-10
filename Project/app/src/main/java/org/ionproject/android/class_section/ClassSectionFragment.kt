package org.ionproject.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_class_section.*
import org.ionproject.android.class_section.ClassSectionViewModel
import org.ionproject.android.class_section.ClassSectionViewModelProvider

class ClassSectionFragment : Fragment() {

    /**
     * This view model is shared between fragments and the MainActivity
     */
    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModelProvider()
    }

    /**
     * Obtaining Class Section's View Model
     */
    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(
            this,
            ClassSectionViewModelProvider()
        )[ClassSectionViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_class_section, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Class Section View Holder Setup
        val courseTextView = textView_class_section_course
        val classTermTextView = textView_class_section_class
        val classIDTextView = textView_class_section_id

        // Search for Class Section Details
        viewModel.getClassSectionDetails(sharedViewModel.classSummary)

        viewModel.observeForClassSectionData(this) {
            courseTextView.text = it.course
            classTermTextView.text = it.calendarTerm
            classIDTextView.text = it.id
            setupCheckboxBehaviour(checkbox_class_section_favorite)
        }
    }

    /**
     * Defines what happens when the checkbox is clicked
     */
    private fun setupCheckboxBehaviour(checkBox: CheckBox) {
        viewModel.isThisClassSectionFavorite {
            checkBox.isChecked = it
        }
        checkBox.setOnClickListener {
            val isChecked = checkBox.isChecked
            if (isChecked) {
                if (!viewModel.addClassSectionToFavorites())
                    checkBox.isChecked = false
            } else {
                if (!viewModel.removeClassSectionFromFavorites())
                    checkBox.isChecked = true
            }
        }
    }
}
