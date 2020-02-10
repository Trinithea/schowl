package com.example.experiment_databin_nav_api24


import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.experiment_databin_nav_api24.databinding.FragmentCategoriesBinding
import com.example.experiment_databin_nav_api24.databinding.FragmentQuestionsBinding


class QuestionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentQuestionsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_questions,container,false)
        (activity as AppCompatActivity).supportActionBar?.title = "QUESTIONS"
        setHasOptionsMenu(true)
        binding.btnAddQuestion.setOnClickListener {
                view: View ->
            Navigation.findNavController(view).navigate(R.id.action_questionsFragment_to_editQuestionFragment)
        }
        return binding.root

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.question_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!,
            view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }

    fun addQuestion( binding: FragmentQuestionsBinding){
        var btnQuestion = Button(activity)
        var params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT)
        params.weight = 1f

        btnQuestion.layoutParams = params

        btnQuestion.background = resources.getDrawable(R.drawable.trasparent)
        btnQuestion.setTextColor(getResources().getColor(R.color.white) )

        btnQuestion.gravity = Gravity.LEFT
        btnQuestion.setOnClickListener{
                view: View ->
            Navigation.findNavController(view).navigate(R.id.action_questionsFragment_to_editQuestionFragment)
            var question=Button(activity)
            question.setText("Question")//TODO

            binding.LytQuestions.addView(question)
        }
        binding.LytQuestions.addView(btnQuestion)
    }
}
