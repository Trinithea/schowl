package cz.cizlmazna.schowl.ui.questions

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import cz.cizlmazna.schowl.MainActivity
import cz.cizlmazna.schowl.R
import cz.cizlmazna.schowl.database.Question
import cz.cizlmazna.schowl.databinding.FragmentQuestionsBinding
import cz.cizlmazna.schowl.ui.questions.QuestionsFragmentArgs
import cz.cizlmazna.schowl.ui.questions.QuestionsFragmentDirections
import kotlinx.android.synthetic.main.remove_dialog.view.*

class QuestionsFragment : Fragment() {

    private lateinit var binding: FragmentQuestionsBinding

    private lateinit var viewModel: QuestionsViewModel
    private var darkMode = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_questions, container, false)
        darkMode = (activity as MainActivity).getDarkMode()

        val application = requireNotNull(this.activity).application

        val arguments =
            QuestionsFragmentArgs.fromBundle(
                arguments!!
            )

        viewModel = ViewModelProvider(this).get(QuestionsViewModel::class.java)
        viewModel.loadData(arguments.categoryId)

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.questions)

        binding.btnAddQuestion.setOnClickListener { view: View ->
            Navigation.findNavController(view).navigate(
                QuestionsFragmentDirections.actionQuestionsFragmentToEditQuestionFragment(
                    arguments.categoryId,
                    -1
                )
            )
        }
        val adapter = QuestionAdapter()
        binding.questionsList.adapter = adapter

        binding.lifecycleOwner = this

        viewModel.questions.observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.submitList(it)
            }
        })

        if (!darkMode) {
            setLayoutToLightMode()
        }
        return binding.root

    }

    private fun setDialog(textView: TextView, ll: LinearLayout) {
        if (!darkMode) {
            ll.background = ContextCompat.getDrawable(context!!, R.color.white)
            textView.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
        } else {
            ll.background = ContextCompat.getDrawable(context!!, R.color.navyBlue)
            textView.setTextColor(ContextCompat.getColor(context!!, R.color.ivoryYellow))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.question_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            view!!.findNavController()
        ) || super.onOptionsItemSelected(item)
    }

    private fun addQuestion(question: Question) {
        val btnQuestion = Button(activity)
        val btnRemove = ImageButton(activity)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        params.weight = 1f
        val mainLyt = ConstraintLayout(activity)
        btnQuestion.layoutParams = params

        if (!darkMode) {
            btnQuestion.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
            btnRemove.setImageResource(R.drawable.ic_remove_blue)
        } else {
            btnQuestion.setTextColor(ContextCompat.getColor(context!!, R.color.ivoryYellow))
            btnRemove.setImageResource(R.drawable.ic_remove_yellow)
        }
        btnQuestion.background = ContextCompat.getDrawable(context!!, R.drawable.transparent)
        btnQuestion.translationZ = 3f
        btnQuestion.gravity = Gravity.START
        btnQuestion.setOnClickListener { view: View ->
            Navigation.findNavController(view).navigate(
                QuestionsFragmentDirections.actionQuestionsFragmentToEditQuestionFragment(
                    question.categoryId,
                    question.id
                )
            )
        }

        btnQuestion.text = question.questionText
        if (btnQuestion.text.length > 27) {
            val displayMetrics = DisplayMetrics()
            (context as Activity).windowManager
                .defaultDisplay
                .getMetrics(displayMetrics)
            val param = LinearLayout.LayoutParams(
                (activity as MainActivity).dpToPx(260),
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            btnQuestion.layoutParams = param
        }
        btnRemove.layoutParams = params
        btnRemove.background = ContextCompat.getDrawable(context!!, R.drawable.transparent)
        btnRemove.setOnClickListener {
            val mDialogView =
                View.inflate(activity, R.layout.remove_dialog, null)
            val mBuilder =
                AlertDialog.Builder(activity).setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            mDialogView.txtMessage.text = getString(R.string.remove_question_dialog)
            setDialog(mDialogView.txtMessage, mDialogView.LlMainRemove)
            mDialogView.btnRemove.setOnClickListener {
                mAlertDialog.dismiss()
                viewModel.removeQuestion(question)
            }
            mDialogView.btnCancel.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }

        mainLyt.id = View.generateViewId()
        btnQuestion.id = View.generateViewId()
        btnRemove.id = View.generateViewId()

        mainLyt.addView(btnQuestion)
        mainLyt.addView(btnRemove)
        val set = ConstraintSet()
        set.clone(mainLyt)
        set.connect(btnQuestion.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        set.connect(btnQuestion.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        set.connect(btnRemove.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
        set.connect(btnRemove.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        set.connect(
            btnRemove.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )
        set.applyTo(mainLyt)
        //binding.llMain.addView(mainLyt)
    }

    private fun setLayoutToLightMode() {
        binding.LytMain.background = ContextCompat.getDrawable(context!!, R.color.white)
    }
}
