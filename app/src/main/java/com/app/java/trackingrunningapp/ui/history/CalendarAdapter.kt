package com.app.java.trackingrunningapp.ui.history

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.java.trackingrunningapp.R
import java.time.LocalDate

class CalendarAdapter(
    private var days: List<LocalDate>,
    private val onDateSelected: (LocalDate) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.DayViewHolder>() {

    var startDate: LocalDate? = null
    var endDate: LocalDate? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_day, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val date = days[position]
        holder.bind(date)
    }

    override fun getItemCount() = days.size

    // Update the list of days when the month changes
    fun updateDays(newDays: List<LocalDate>) {
        this.days = newDays
        notifyDataSetChanged() // Notify adapter to update the UI
    }

    inner class DayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val dayTextView: TextView = view.findViewById(R.id.tv_day)

        fun bind(date: LocalDate) {
            dayTextView.text = date.dayOfMonth.toString()

            // Update background color based on selected dates
            updateDaySelectionBackground(date)

            // Set click listener to change the selected date
            dayTextView.setOnClickListener {
                if (startDate == null || (endDate != null && startDate != null)) {
                    startDate = date
                    endDate = null
                } else {
                    if (date.isAfter(startDate!!)) {
                        endDate = date
                    } else {
                        startDate = date
                        endDate = null
                    }
                }

                // Notify parent fragment about the selected date (optional)
                onDateSelected(date)

                // Update UI immediately
                notifyItemChanged(adapterPosition)
            }
        }

        // Method to update background color based on selection
        private fun updateDaySelectionBackground(date: LocalDate) {
            when {
                date == startDate -> {
                    dayTextView.setBackgroundResource(R.drawable.item_calendar_day_circle_selected)
                    dayTextView.setTextColor(Color.BLACK)
                }
                date == endDate -> {
                    dayTextView.setBackgroundResource(R.drawable.item_calendar_day_circle_selected)
                    dayTextView.setTextColor(Color.BLACK)
                }
                startDate != null && endDate != null && date.isAfter(startDate) && date.isBefore(endDate) -> {
                    dayTextView.setBackgroundResource(R.drawable.item_calendar_day_circle_selected_range)
                    dayTextView.setTextColor(Color.BLACK)
                }
                else -> {
                    dayTextView.setBackgroundResource(R.drawable.item_calendar_day_circle)
                    dayTextView.setTextColor(Color.WHITE)
                }
            }
        }
    }
}