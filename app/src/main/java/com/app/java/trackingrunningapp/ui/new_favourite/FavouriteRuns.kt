package com.app.java.trackingrunningapp.ui.new_favourite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.java.trackingrunningapp.R

class FavouriteRuns : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favourite_runs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val runList = mutableListOf(
            RunItem("Normal Run", "12KM", "22/10/2024"),
            RunItem("Normal Run", "12KM", "2/9/2024"),
            RunItem("Morning Run", "8KM", "1/8/2024"),
            RunItem("Evening Sprint", "5KM", "15/7/2024"),
            RunItem("Marathon Prep", "20KM", "10/7/2024"),
            RunItem("Trail Adventure", "15KM", "5/6/2024"),
            RunItem("Casual Jog", "6KM", "1/5/2024"),
            RunItem("City Run", "10KM", "25/4/2024"),
            RunItem("Weekend Dash", "7KM", "20/4/2024"),
            RunItem("Quick Run", "3KM", "18/4/2024"),
            RunItem("Challenge Run", "13KM", "15/4/2024"),
            RunItem("Sunset Run", "9KM", "10/4/2024"),
            RunItem("Morning Run", "8KM", "1/8/2024"),
            RunItem("Trail Adventure", "14KM", "22/3/2024"),
            RunItem("Hill Run", "10KM", "10/3/2024"),
            RunItem("Fitness Run", "11KM", "1/3/2024"),
            RunItem("Fun Run", "5KM", "20/2/2024"),
            RunItem("Marathon Race", "42KM", "15/2/2024"),
            RunItem("Light Jog", "4KM", "5/2/2024"),
            RunItem("Intense Sprint", "2KM", "25/1/2024")
        )

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = FavouriteRunAdapter(runList)
    }
}