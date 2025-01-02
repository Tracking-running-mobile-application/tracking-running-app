package com.app.java.trackingrunningapp.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.databinding.FragmentDetailRunBinding


class DetailRunFragment : Fragment() {
    private lateinit var binding: FragmentDetailRunBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailRunBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textDistance = view.findViewById<TextView>(R.id.text_distance_metric)
        textDistance.text = getString(R.string.text_distance_metric,3.38)

        val textCalo = view.findViewById<TextView>(R.id.text_calorie_metric)
        textCalo.text = getString(R.string.text_calorie_metric,353.4)
    }

    companion object{
        const val EXTRA_HISTORY_ID = "EXTRA_HISTORY_ID"
    }
}