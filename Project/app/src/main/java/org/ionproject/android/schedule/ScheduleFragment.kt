package org.ionproject.android.schedule

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.android.synthetic.main.fragment_schedule.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.ionproject.android.ExceptionHandlingFragment
import org.ionproject.android.R
import org.ionproject.android.common.model.Moment
import org.ionproject.android.main.MainActivity

const val NUMBER_OF_COLUMNS = 8

class ScheduleFragment : ExceptionHandlingFragment() {

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(
            this,
            ScheduleViewModelProvider()
        )[ScheduleViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    /**
     * Hide bottom navigation bar from activity
     * Hide top action bar from activity
     * Change screen orientation
     *
     * If user clicks back arrow undo this changes
     */
    @SuppressLint("SourceLockedOrientationActivity") // Required for locked orientation
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.bottomnavview_main?.visibility = View.GONE
        (activity as MainActivity).supportActionBar?.hide()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        // Get calendar term that was selected by the user on App Settings
        val selectedCalendarTerm = viewModel.getSelectedCalendarTerm()

        if (selectedCalendarTerm == null)
            viewModel.getLecturesFromFirstCalendarFromFavorites()
        else
            viewModel.getLecturesFromSelectedCalendarTerm(selectedCalendarTerm)

        // Back button behaviour
        view.cardview_schedule_back.setOnClickListener {
            activity?.bottomnavview_main?.visibility = View.VISIBLE
            (activity as MainActivity).supportActionBar?.show()
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            findNavController().navigateUp()
        }

        // Schedule grid behaviour
        recyclerview_schedule_hours.apply {
            layoutManager = GridLayoutManager(context, NUMBER_OF_COLUMNS)

            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.HORIZONTAL
                )
            )
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )

            // Create grid adapter which will contain half an hour blocks starting at 8 am
            val adapter = ScheduleGridAdapter(
                startHours = Moment(8, 0),
                endHours = Moment(23, 0),
                blockSize = Moment.ThirtyMinutes,
                model = viewModel
            )

            this.adapter = adapter

            viewModel.observerLecturesLiveData(viewLifecycleOwner) {
                adapter.notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("SourceLockedOrientationActivity") // Required for locked orientation
    override fun exceptionHandler(throwable: Throwable) {
        activity?.lifecycleScope?.launch(Dispatchers.Main) {
            activity?.bottomnavview_main?.visibility = View.VISIBLE
            (activity as MainActivity).supportActionBar?.show()
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            super.exceptionHandler(throwable)
        }
    }

}
