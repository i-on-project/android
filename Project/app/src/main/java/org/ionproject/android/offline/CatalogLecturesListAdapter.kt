package org.ionproject.android.offline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_lectures.view.*
import org.ionproject.android.R
import org.ionproject.android.offline.models.ClassesDetails

/**
 * Wrong, correct later
 */
class CatalogLecturesListAdapter(
    private val classes: List<ClassesDetails>
) : RecyclerView.Adapter<CatalogLectureViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogLectureViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_lectures, parent, false)
        return CatalogLectureViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatalogLectureViewHolder, position: Int) {
        holder.bindTo(classes[position])
    }

    override fun getItemCount(): Int = classes.size
}

class CatalogLectureViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val lectureTextView = view.textview_list_item_lectures
    private val locationTextView = view.textview_list_item_lectures_location
    private val exportButton = view.button_list_item_lectures_export

    fun bindTo(classDetails: ClassesDetails) {

        /*val startMoment = Moment.fromCalendar(classesDetails)
        val endMoment = startMoment + lecture.duration

        lectureTextView.text = view.resources.getString(
            R.string.placeholder_lecture_all,
            lecture.summary,
            lecture.weekDay.getName(view.context),
            startMoment.hours.fillWithZero(),
            startMoment.minutes.fillWithZero(),
            endMoment.hours.fillWithZero(),
            endMoment.minutes.fillWithZero()
        )*/

        locationTextView.text =  ""

        exportButton.setOnClickListener {
           Toast.makeText(view.context, R.string.error_message_exception_resource_not_available_all, Toast.LENGTH_LONG).show()
        }
    }
}