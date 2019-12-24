package com.example.experiment_databin_nav_api24


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.experiment_databin_nav_api24.databinding.FragmentCategoriesBinding
import com.example.experiment_databin_nav_api24.databinding.FragmentSubjectsBinding


class SubjectsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSubjectsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_subjects,container,false)
        return binding.root

    }


}