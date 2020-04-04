package cz.cizlmazna.schowl.ui.questions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import cz.cizlmazna.schowl.MainActivity
import cz.cizlmazna.schowl.R
import cz.cizlmazna.schowl.databinding.FragmentEditQuestionBinding
import cz.cizlmazna.schowl.ui.questions.EditQuestionFragmentArgs

class EditQuestionFragment : Fragment() {

    private lateinit var binding: FragmentEditQuestionBinding

    private lateinit var viewModel: EditQuestionViewModel
    private var darkMode = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_edit_question, container, false)
        darkMode = (activity as MainActivity).getDarkMode()

        val application = requireNotNull(this.activity).application

        val arguments =
            EditQuestionFragmentArgs.fromBundle(
                arguments!!
            )

        val viewModelFactory =
            EditQuestionViewModelFactory(
                application,
                arguments.categoryId,
                arguments.questionId
            )

        viewModel = ViewModelProvider(this, viewModelFactory).get(EditQuestionViewModel::class.java)

        binding.editQuestionViewModel = viewModel
        binding.lifecycleOwner = this
        setSeekBarListener(binding.LblDifficulty, binding.SbrDifficulty)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.edit_question)

//        binding.TxtQuestion.setOn

        binding.btnSent.setOnClickListener { view: View ->
            viewModel.confirm(
                binding.TxtQuestion.text.toString(),
                binding.TxtAnswer.text.toString(),
                binding.SbrDifficulty.progress.toByte()
            )
            Navigation.findNavController(view).popBackStack()
        }

        if (!darkMode) {
            setLayoutToLightMode()
        }
        return binding.root
    }

    private fun setSeekBarListener(label: TextView, seekBar: SeekBar) {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                (activity as MainActivity).hideKeyboard(binding.TxtAnswer)
                (activity as MainActivity).hideKeyboard(binding.TxtQuestion)
                label.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }

    private fun setLayoutToLightMode() {
        binding.LytBackground.background = ContextCompat.getDrawable(context!!, R.color.white)
        binding.LblDifficulty.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
        binding.LblDiff.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
        binding.LblQueston.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
        binding.LblSolution.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
        binding.TxtAnswer.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
        binding.TxtQuestion.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveQuestion(
            binding.TxtQuestion.text.toString(),
            binding.TxtAnswer.text.toString(),
            binding.SbrDifficulty.progress.toByte()
        )
        (activity as MainActivity).hideKeyboard(binding.TxtQuestion)
        (activity as MainActivity).hideKeyboard(binding.TxtAnswer)
    }
}
