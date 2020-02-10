package com.example.experiment_databin_nav_api24


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.experiment_databin_nav_api24.databinding.FragmentCategoriesBinding
import com.example.experiment_databin_nav_api24.databinding.FragmentEditquestionBinding

class EditQuestionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentEditquestionBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_editquestion,container,false)
        (activity as AppCompatActivity).supportActionBar?.title = "EDIT QUESTION"

        binding.btnSent.setOnClickListener {
            //TODO some Live data magic
                view: View ->
            Navigation.findNavController(view).navigate(R.id.action_action_edit_category_to_questionsFragment)
        }
        return binding.root



    }


}
