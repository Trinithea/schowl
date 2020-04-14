package cz.cizlmazna.schowl.ui.test

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import cz.cizlmazna.schowl.MainActivity
import cz.cizlmazna.schowl.R
import cz.cizlmazna.schowl.databinding.FragmentTestBinding
import kotlinx.android.synthetic.main.test_completed_dialog.view.*

class TestFragment : Fragment() {

    private lateinit var binding: FragmentTestBinding

    private lateinit var viewModel: TestViewModel
    private var darkMode = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_test, container, false)
        darkMode = (activity as MainActivity).getDarkMode()

        val arguments = TestFragmentArgs.fromBundle(arguments!!)

        viewModel = ViewModelProvider(this).get(TestViewModel::class.java)
        viewModel.loadData(arguments.categoryIds, arguments.minDifficulty, arguments.maxDifficulty)

        binding.testViewModel = viewModel

        binding.lifecycleOwner = this

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.test)

        if (!darkMode) {
            setLayoutToLightMode()
        }

        viewModel.getShowSolution().observe(viewLifecycleOwner, Observer {
            if (it) {
                (activity as MainActivity).hideKeyboard(binding.TxtAnswer)
            }
        })

        viewModel.getQuestionChange().observe(viewLifecycleOwner, Observer {
            if (it) {
                (activity as MainActivity).hideKeyboard(binding.TxtAnswer)
                binding.TxtAnswer.setText("")
                viewModel.doneChangingQuestion()
            }
        })

        viewModel.getNavigateToEditQuestion().observe(viewLifecycleOwner, Observer {
            if (it == true) {
                (activity as MainActivity).hideKeyboard(binding.TxtAnswer)
                findNavController().navigate(
                    TestFragmentDirections.actionTestFragmentToEditQuestionFragment(
                        viewModel.getCurrentQuestionCategoryId(),
                        viewModel.getCurrentQuestionId()
                    )
                )
            }
        })

        viewModel.getTestOver().observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val mDialogView = View.inflate(activity, R.layout.test_completed_dialog, null)
                val mBuilder = AlertDialog.Builder(activity).setView(mDialogView)
                val mAlertDialog = mBuilder.show()
                mDialogView.btnSetup.setOnClickListener {
                    // TODO make this as an event using the viewModel
                    findNavController().popBackStack()
                    mAlertDialog.dismiss()
                }
            }
        })

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.doneNavigatingToEditQuestion()
    }

    private fun setLayoutToLightMode() {
        binding.LblMyAnswer.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
        binding.LblQuestion.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
        binding.LblNumberOfQuestion.setTextColor(
            ContextCompat.getColor(
                context!!,
                R.color.navyBlue
            )
        )
        binding.LblSolution.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
        binding.LytMain.background = ContextCompat.getDrawable(context!!, R.color.white)
        binding.TxtAnswer.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).hideKeyboard(binding.TxtAnswer)
    }
}
