package cz.cizlmazna.schowl.ui.questions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import cz.cizlmazna.schowl.MainActivity
import cz.cizlmazna.schowl.R
import cz.cizlmazna.schowl.databinding.FragmentEditQuestionBinding

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

        val arguments: EditQuestionFragmentArgs by navArgs()

        viewModel = ViewModelProvider(this).get(EditQuestionViewModel::class.java)
        viewModel.loadData(arguments.categoryId, arguments.questionId)

        binding.editQuestionViewModel = viewModel
        binding.lifecycleOwner = this
        setSeekBarListener(binding.LblDifficulty, binding.SbrDifficulty)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.edit_question)

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
        binding.LytBackground.background = ContextCompat.getDrawable(binding.LytBackground.context, R.color.white)
        binding.LblDifficulty.setTextColor(ContextCompat.getColor(binding.LblDifficulty.context, R.color.navyBlue))
        binding.LblDiff.setTextColor(ContextCompat.getColor(binding.LblDiff.context, R.color.navyBlue))
        binding.LblQueston.setTextColor(ContextCompat.getColor(binding.LblQueston.context, R.color.navyBlue))
        binding.LblSolution.setTextColor(ContextCompat.getColor(binding.LblSolution.context, R.color.navyBlue))
        binding.TxtAnswer.setTextColor(ContextCompat.getColor(binding.TxtAnswer.context, R.color.navyBlue))
        binding.TxtQuestion.setTextColor(ContextCompat.getColor(binding.TxtQuestion.context, R.color.navyBlue))
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
