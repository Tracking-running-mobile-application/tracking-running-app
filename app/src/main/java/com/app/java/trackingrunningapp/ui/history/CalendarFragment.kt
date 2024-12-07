package com.app.java.trackingrunningapp.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.app.java.trackingrunningapp.databinding.FragmentCalendarBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarFragment : Fragment() {

    private lateinit var calendarAdapter: CalendarAdapter
    private var startDate: LocalDate? = null
    private var endDate: LocalDate? = null

    // Keep track of the current month being displayed
    private var currentMonth: LocalDate = LocalDate.now()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Use view binding to inflate the fragment layout
        val binding = FragmentCalendarBinding.inflate(inflater, container, false)

        // Setup RecyclerView and Adapter
        calendarAdapter = CalendarAdapter(getDaysOfMonth(currentMonth)) { selectedDate ->
            onDateSelected(selectedDate)
        }

        // Set the LayoutManager for RecyclerView
        binding.rvCalendar.layoutManager = GridLayoutManager(requireContext(), 7) // 7 columns for a week
        binding.rvCalendar.adapter = calendarAdapter

        // Set initial month name
        updateMonthName(binding)

        // Setup previous and next month buttons
        binding.btnPreviousMonth.setOnClickListener {
            changeMonth(false)
            updateMonthName(binding)
        }

        binding.btnNextMonth.setOnClickListener {
            changeMonth(true)
            updateMonthName(binding)
        }

        // Setup Reset button to clear selected dates
        binding.btnReset.setOnClickListener {
            startDate = null
            endDate = null
            calendarAdapter.startDate = null
            calendarAdapter.endDate = null
            calendarAdapter.notifyDataSetChanged() // Update the adapter to reflect the changes
        }


        return binding.root
    }

    private fun getDaysOfMonth(month: LocalDate): List<LocalDate> {
        // Get the first day of the current month
        val firstDayOfMonth = month.withDayOfMonth(1)

        // Get the day of the week for the first day (0 = Sunday, 6 = Saturday)
        val startDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7

        // Get the number of days in the current month
        val daysInMonth = month.month.length(month.isLeapYear)

        // Calculate the days of the month, including leading empty days from the previous month
        val days = mutableListOf<LocalDate>()

        // Add empty days for the previous month (if necessary)
        for (i in 0 until startDayOfWeek) {
            val previousMonthDay = firstDayOfMonth.minusDays((startDayOfWeek - i).toLong())
            days.add(previousMonthDay)
        }

        // Add the actual days of the current month
        for (i in 0 until daysInMonth) {
            days.add(firstDayOfMonth.plusDays(i.toLong()))
        }

        // Calculate the number of empty days required after the last day of the current month to fill the last week
        val remainingDays = (7 - (days.size % 7)) % 7
        for (i in 0 until remainingDays) {
            val nextMonthDay = firstDayOfMonth.plusMonths(1).plusDays(i.toLong())
            days.add(nextMonthDay)
        }

        return days
    }

    private fun onDateSelected(date: LocalDate) {
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

        // Update the adapter with the new selected dates
        calendarAdapter.startDate = startDate
        calendarAdapter.endDate = endDate

        // Notify the adapter to update the UI immediately
        calendarAdapter.notifyDataSetChanged()
    }

    // Change the month (forward or backward)
    private fun changeMonth(isNext: Boolean) {
        currentMonth = if (isNext) {
            currentMonth.plusMonths(1)
        } else {
            currentMonth.minusMonths(1)
        }
        calendarAdapter.updateDays(getDaysOfMonth(currentMonth))
    }

    // Update the month name in the UI
    private fun updateMonthName(binding: FragmentCalendarBinding) {
        val monthName = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
        binding.tvMonthYear.text = monthName
    }
}
