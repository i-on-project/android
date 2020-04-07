package edu.isel.ion.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProviders
import edu.isel.ion.android.class_section.ClassSectionViewModel
import edu.isel.ion.android.class_section.ClassSectionViewModelProvider

/**
 * A simple [Fragment] subclass.
 */
class ClassSectionFragment : Fragment() {

    /*
        This view model is shared between fragments and the MainActivity
     */
    private val sharedViewModel : SharedViewModel by activityViewModels {
        SharedViewModelProvider()
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

        // Obtaining Class Section's View Model
        val viewModel = ViewModelProviders
            .of(this, ClassSectionViewModelProvider())[ClassSectionViewModel::class.java]

        //Class Section View Holder Setup
        val titleTextView = view.findViewById<TextView>(R.id.textView_class_section_title)
        val courseTextView = view.findViewById<TextView>(R.id.textView_class_section_course)
        val teacherTextView = view.findViewById<TextView>(R.id.textView_class_section_teacher)
        val classTermTextView = view.findViewById<TextView>(R.id.textView_class_section_class)
        val classIDTextView = view.findViewById<TextView>(R.id.textView_class_section_id)

        // Search for Class Section Details
        viewModel.getClassSectionDetails(sharedViewModel.classSummary)
        viewModel.observeForClassSectionData(this) {
            courseTextView.text = it.course
            classTermTextView.text = it.calendarTerm
            classIDTextView.text = it.id
        }
    }
}
