package cz.cizlmazna.schowl.ui.subjects.categories.questions


import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import cz.cizlmazna.schowl.R
import cz.cizlmazna.schowl.database.Question
import cz.cizlmazna.schowl.database.SchowlDatabase
import cz.cizlmazna.schowl.databinding.FragmentQuestionsBinding


class QuestionsFragment : Fragment() {

    private lateinit var binding: FragmentQuestionsBinding

    private lateinit var viewModel: QuestionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_questions,container,false)

        val application = requireNotNull(this.activity).application

        val arguments = QuestionsFragmentArgs.fromBundle(arguments!!)

        val databaseDao = SchowlDatabase.getInstance(application).schowlDatabaseDao

        val viewModelFactory = QuestionsViewModelFactory(databaseDao, arguments.categoryId)

        viewModel = ViewModelProvider(this, viewModelFactory).get(QuestionsViewModel::class.java)

        (activity as AppCompatActivity).supportActionBar?.title = "QUESTIONS"

        setHasOptionsMenu(true)
        binding.btnAddQuestion.setOnClickListener { view: View ->
            Navigation.findNavController(view).navigate(QuestionsFragmentDirections.actionQuestionsFragmentToEditQuestionFragment(viewModel.categoryId, -1))
        }

        binding.lifecycleOwner = this

        viewModel.questions.observe(viewLifecycleOwner, Observer {
            generateQuestionsList(it)
        })

        return binding.root

    }

    private fun generateQuestionsList(questions: List<Question>) {
        binding.LytQuestions.removeAllViews()
        binding.LytRemoves.removeAllViews()
        for (question in questions) {
            addQuestion(question)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.question_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, view!!.findNavController()) || super.onOptionsItemSelected(item)
    }

    private fun addQuestion(question: Question){
        val btnQuestion = Button(activity)
        val btnRemove = ImageButton(activity)
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT)
        params.weight = 1f

        btnQuestion.layoutParams = params

        btnQuestion.background = ContextCompat.getDrawable(context!!, R.drawable.transparent)
        btnQuestion.translationZ = 3f
        btnQuestion.gravity = Gravity.START
        btnQuestion.setOnClickListener{
                view: View ->
            Navigation.findNavController(view).navigate(QuestionsFragmentDirections.actionQuestionsFragmentToEditQuestionFragment(viewModel.categoryId, question.id))
//            val question=Button(activity)
//            question.text = "Question" // TODO hardcoded string
//
//            binding.LytQuestions.addView(question)
        }

        btnQuestion.text = question.questionText
        btnRemove.layoutParams = params
        btnRemove.setImageResource(R.drawable.ic_remove_yellow)
        btnRemove.background= ContextCompat.getDrawable(context!!, R.drawable.transparent)
        btnRemove.setOnClickListener{
            viewModel.removeQuestion(question)
//            binding.LytQuestions.removeView(btnQuestion)
//            binding.LytRemoves.removeView(btnRemove)
        }
        binding.LytQuestions.addView(btnQuestion)
        binding.LytRemoves.addView(btnRemove)
    }
}
