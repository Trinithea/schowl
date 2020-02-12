package cz.cizlmazna.schowl.ui.subjects.categories.questions


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import cz.cizlmazna.schowl.R
import cz.cizlmazna.schowl.database.SchowlDatabase
import cz.cizlmazna.schowl.databinding.FragmentEditQuestionBinding

class EditQuestionFragment : Fragment() {

    private lateinit var binding: FragmentEditQuestionBinding

    private lateinit var viewModel: EditQuestionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_question, container, false)

        val application = requireNotNull(this.activity).application

        val arguments = EditQuestionFragmentArgs.fromBundle(arguments!!)

        val databaseDao = SchowlDatabase.getInstance(application).schowlDatabaseDao

        val viewModelFactory = EditQuestionViewModelFactory(databaseDao, arguments.categoryId, arguments.questionId)

        viewModel = ViewModelProvider(this, viewModelFactory).get(EditQuestionViewModel::class.java)

        binding.editQuestionViewModel = viewModel
        binding.lifecycleOwner = this
        setSeekBarListener(binding.LblDifficulty,binding.SbrDifficulty)
        (activity as AppCompatActivity).supportActionBar?.title = "EDIT QUESTION" // TODO hardcoded string

        binding.btnSent.setOnClickListener {
            view: View ->
            viewModel.confirm(binding.TxtQuestion.text.toString(), binding.TxtAnswer.text.toString(), binding.SbrDifficulty.progress.toByte())
            Navigation.findNavController(view).navigate(EditQuestionFragmentDirections.actionEditQuestionFragmentToQuestionsFragment(viewModel.categoryId))
        }
        return binding.root
    }
    private fun setSeekBarListener(label: TextView, seekBar: SeekBar) {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                label.setText(progress.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }
}
