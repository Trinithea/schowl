package cz.cizlmazna.schowl.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import cz.cizlmazna.schowl.MainActivity
import cz.cizlmazna.schowl.R
import cz.cizlmazna.schowl.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSettingsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        val darkMode = (activity as MainActivity).getDarkMode()
        binding.SwitchDarkMode.isChecked = darkMode
        if (!darkMode)
            setLayoutToLightMode(binding)
        else
            setLayoutToDarkMode(binding)
        binding.SwitchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                (activity as MainActivity).setDarkMode(false)
                setLayoutToLightMode(binding)
            } else {
                (activity as MainActivity).setDarkMode(true)
                setLayoutToDarkMode(binding)
            }
        }
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.SETTINGS)
        return binding.root
    }


    private fun setLayoutToLightMode(binding: FragmentSettingsBinding) {
        binding.SwitchDarkMode.setTextColor(ContextCompat.getColor(binding.SwitchDarkMode.context, R.color.navyBlue))
        binding.SwitchDarkMode.text = getString(R.string.light_mode)
        binding.image.setImageResource(R.drawable.ic_settings_background_light)

    }

    private fun setLayoutToDarkMode(binding: FragmentSettingsBinding) {
        binding.image.setImageResource(R.drawable.ic_settings_background_dark_3)
        binding.SwitchDarkMode.setTextColor(ContextCompat.getColor(binding.SwitchDarkMode.context, R.color.ivoryYellow))
        binding.SwitchDarkMode.text = getString(R.string.dark_mode)
    }


}
