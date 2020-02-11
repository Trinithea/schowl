package cz.cizlmazna.schowl.ui.subjects


import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import cz.cizlmazna.schowl.R
import cz.cizlmazna.schowl.databinding.FragmentQuestionsBinding


class QuestionsFragment : Fragment() {

    private lateinit var binding: FragmentQuestionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_questions,container,false)
        (activity as AppCompatActivity).supportActionBar?.title = "QUESTIONS"
        setHasOptionsMenu(true)
        binding.btnAddQuestion.setOnClickListener {
              //  view: View ->
       //     Navigation.findNavController(view).navigate(R.id.action_questionsFragment_to_editQuestionFragment)
            addQuestion(binding)
        }
        return binding.root

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.question_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, view!!.findNavController()) || super.onOptionsItemSelected(item)
    }

    private fun addQuestion( binding: FragmentQuestionsBinding){
        val btnQuestion = Button(activity)
        var btnRemove = ImageButton(activity)
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT)
        params.weight = 1f

        btnQuestion.layoutParams = params

        btnQuestion.background = resources.getDrawable(R.drawable.transparent)
        btnQuestion.setTextColor(getResources().getColor(R.color.white) )
        btnQuestion.translationZ = 3f
        btnQuestion.gravity = Gravity.START
        btnQuestion.setOnClickListener{
                view: View ->
            Navigation.findNavController(view).navigate(R.id.action_questionsFragment_to_editQuestionFragment)
            val question=Button(activity)
            question.setText("Question") // TODO hardcoded string

            binding.LytQuestions.addView(question)
        }
        btnQuestion.setText("Question")
        btnRemove.layoutParams = params
        btnRemove.setImageResource(R.drawable.ic_icon_remove)
        btnRemove.background= resources.getDrawable(R.drawable.transparent)
        btnRemove.setOnClickListener{
            binding.LytQuestions.removeView(btnQuestion)
            binding.LytRemoves.removeView(btnRemove)
        }
        binding.LytRemoves.addView(btnRemove)
        binding.LytQuestions.addView(btnQuestion)
    }
}
