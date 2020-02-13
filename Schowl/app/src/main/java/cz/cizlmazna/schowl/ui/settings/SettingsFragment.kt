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
        val darkmode = (activity as MainActivity).getDarkMode()
        binding.SwitchDarkMode.isChecked = darkmode
        if(!darkmode)
            setLayoutToLightMode(binding)
        binding.SwitchDarkMode.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked == false){
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
        binding.llMain.background= ContextCompat.getDrawable(context!!, R.color.white)
        binding.SwitchDarkMode.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))

    }
    private fun setLayoutToDarkMode(binding:FragmentSettingsBinding){
        binding.llMain.background= ContextCompat.getDrawable(context!!, R.color.navyBlueDark)
        binding.image.setImageResource(R.drawable.ic_settings_background_dark)
        binding.SwitchDarkMode.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
    }
}
