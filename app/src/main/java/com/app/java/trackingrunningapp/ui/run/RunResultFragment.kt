package com.app.java.trackingrunningapp.ui.run

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.databinding.FragmentRunResultBinding


class RunResultFragment : Fragment() {
    private lateinit var binding:FragmentRunResultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRunResultBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbarTitle = requireActivity().findViewById<TextView>(R.id.tv_toolbar_title)
        toolbarTitle.text = "Run Result"
    }
}