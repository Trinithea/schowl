package com.example.experiment_databin_nav_api24


import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.experiment_databin_nav_api24.databinding.FragmentCategoriesBinding
import com.example.experiment_databin_nav_api24.databinding.TestBinding

class TestFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: TestBinding = DataBindingUtil.inflate(inflater, R.layout.test,container,false)
        (activity as AppCompatActivity).supportActionBar?.title = "TEST"
        binding.BtnShowSolution.setOnClickListener{
            binding.LblSolution.visibility = View.VISIBLE
            binding.LytEditNext.visibility = View.VISIBLE
        }
        val darkmode:Boolean = true
        if(darkmode == false){
            setLayout(binding)
        }

        return binding.root

    }

    fun setLayout(binding: TestBinding){
        binding.LblMyAnswer.setTextColor(getResources().getColor(R.color.navyBlue))
        binding.LblQuestion.setTextColor(getResources().getColor(R.color.navyBlue))
        binding.LblNumberOfQuestion.setTextColor(getResources().getColor(R.color.navyBlue))
        binding.LblSolution.setTextColor(getResources().getColor(R.color.navyBlue))
        binding.LytMain.background=resources.getDrawable(R.color.white)
        binding.TxtAnswer.setTextColor(getResources().getColor(R.color.navyBlue))
    }


}
