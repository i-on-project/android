package org.ionproject.android.class_section

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_label.view.*
import kotlinx.android.synthetic.main.list_item_todos.view.*
import org.ionproject.android.R
import org.ionproject.android.calendar.jdcalendar.*
import org.ionproject.android.common.model.Todo
import org.ionproject.android.common.model.fillWithZero

class TodoAdapter(
    private val model: ClassSectionViewModel
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_todos, parent, false)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int = model.workAssignments.size

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bindTo(model.workAssignments[position])
    }

    class TodoViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val todoTextView = view.textview_list_item_todos
        private val attachmentTextView = view.textview_list_item_todos_attachment

        fun bindTo(todo: Todo) {
            todoTextView.text = view.resources.getString(
                R.string.placeholder_todo,
                todo.summary,
                todo.due.day,
                todo.due.month,
                todo.due.year,
                todo.due.hour.fillWithZero(),
                todo.due.minute.fillWithZero()
            )
            attachmentTextView.text = todo.attachment.toString()
        }
    }
}