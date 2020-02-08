package com.example.experiment_databin_nav_api24


import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.experiment_databin_nav_api24.databinding.SettingsBinding
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate


class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: SettingsBinding = DataBindingUtil.inflate(inflater, R.layout.settings,container,false)
        binding.SwitchDarkMode.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked == false){

            }
        });
        return binding.root
    }


}
