package com.app.java.trackingrunningapp.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // hide icon setting
        val itemSetting = requireActivity()
            .findViewById<Toolbar>(R.id.toolbar_main).menu.findItem(R.id.item_toolbar_setting)
        itemSetting.isVisible = false
    }

    override fun onStop() {
        super.onStop()
        val itemSetting = requireActivity()
            .findViewById<Toolbar>(R.id.toolbar_main).menu.findItem(R.id.item_toolbar_setting)
        itemSetting.isVisible = true
    }
}