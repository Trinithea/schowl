package cz.cizlmazna.schowl.ui.questions

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import cz.cizlmazna.schowl.MainActivity
import cz.cizlmazna.schowl.R
import cz.cizlmazna.schowl.database.Question
import cz.cizlmazna.schowl.databinding.FragmentQuestionsBinding
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

        val arguments: QuestionsFragmentArgs by navArgs()

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
        val adapter = QuestionAdapter(this)
        binding.questionsList.adapter = adapter

        binding.lifecycleOwner = this

        viewModel.questions.observe(viewLifecycleOwner, Observer {
            it?.let {
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

    fun removeButtonClicked(question: Question) {
        val dialogView =
            View.inflate(activity, R.layout.remove_dialog, null)
        val builder =
            AlertDialog.Builder(activity).setView(dialogView)
        val alertDialog = builder.show()
        dialogView.txtMessage.text = getString(R.string.remove_question_dialog)
        setDialog(dialogView.txtMessage, dialogView.LlMainRemove)
        dialogView.btnRemove.setOnClickListener {
            alertDialog.dismiss()
            viewModel.removeQuestion(question)
        }
        dialogView.btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }
    }


    private fun setLayoutToLightMode() {
        binding.LytMain.background = ContextCompat.getDrawable(context!!, R.color.white)
    }
}
