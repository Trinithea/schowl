package com.example.experiment_databin_nav_api24


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.experiment_databin_nav_api24.databinding.FragmentCategoriesBinding
import com.example.experiment_databin_nav_api24.databinding.FragmentSettingstestBinding


class SettingsTestFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentSettingstestBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_settingstest,container,false)
        setHasOptionsMenu(true)

        (activity as AppCompatActivity).supportActionBar?.title = "CREATE YOUR TEST"
        return binding.root



    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.confirm_menu,menu)
    }

}
