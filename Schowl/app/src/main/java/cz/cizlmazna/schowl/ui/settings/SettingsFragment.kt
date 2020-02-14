package cz.cizlmazna.schowl.ui.settings


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import cz.cizlmazna.schowl.MainActivity
import cz.cizlmazna.schowl.R
import cz.cizlmazna.schowl.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentSettingsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        val darkMode = (activity as MainActivity).getDarkMode()
        binding.SwitchDarkMode.isChecked = darkMode
        if(!darkMode)
            setLayoutToLightMode(binding)
        else
            setLayoutToDarkMode(binding)
        binding.SwitchDarkMode.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if(!isChecked){
                (activity as MainActivity).setDarkMode(false)
                setLayoutToLightMode(binding)
            }
            else{
                (activity as MainActivity).setDarkMode(true)
                setLayoutToDarkMode(binding)
            }
        })
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.SETTINGS)
        return binding.root
    }


    private fun setLayoutToLightMode(binding:FragmentSettingsBinding){
        binding.SwitchDarkMode.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
        binding.SwitchDarkMode.setText(getString(R.string.light_mode))
      binding.image.setImageResource(R.drawable.ic_settings_background_light)

    }
    private fun setLayoutToDarkMode(binding:FragmentSettingsBinding){
        binding.image.setImageResource(R.drawable.ic_settings_background_dark_3)
        binding.SwitchDarkMode.setTextColor(ContextCompat.getColor(context!!, R.color.ivoryYellow))
        binding.SwitchDarkMode.setText(getString(R.string.dark_mode))
    }


}
