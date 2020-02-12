package cz.cizlmazna.schowl.ui.settings


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cz.cizlmazna.schowl.R
import cz.cizlmazna.schowl.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentSettingsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
//        binding.SwitchDarkMode.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
//            if(isChecked == false){
//
//            }
//        })
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.SETTINGS)
        return binding.root
    }


}
