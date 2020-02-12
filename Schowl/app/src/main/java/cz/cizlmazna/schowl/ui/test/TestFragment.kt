package cz.cizlmazna.schowl.ui.test


import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import cz.cizlmazna.schowl.R
import cz.cizlmazna.schowl.database.SchowlDatabase
import cz.cizlmazna.schowl.databinding.FragmentTestBinding
import kotlinx.android.synthetic.main.test_completed_dialog.view.*

class TestFragment : Fragment() {

    private lateinit var binding: FragmentTestBinding

    private lateinit var viewModel: TestViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_test, container, false)

        val application = requireNotNull(this.activity).application

        val arguments = TestFragmentArgs.fromBundle(arguments!!)

        val databaseDao = SchowlDatabase.getInstance(application).schowlDatabaseDao

        val viewModelFactory = TestViewModelFactory(databaseDao, arguments.categoryIds, arguments.minDifficulty, arguments.maxDifficulty)

        viewModel = ViewModelProvider(this, viewModelFactory).get(TestViewModel::class.java)

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.test)

        binding.BtnShowSolution.setOnClickListener{
            binding.BtnShowSolution.visibility = View.GONE
            binding.LblSolution.visibility = View.VISIBLE
            binding.LytEditNext.visibility = View.VISIBLE
        }
        //setLayout(true)
        binding.BtnNext.setOnClickListener {
                view: View ->
//            Navigation.findNavController(view).navigate(TestFragmentDirections.actionTestFragmentSelf())
        }

        return binding.root
    }

    private fun setLayout(darkMode: Boolean){
//        TODO("Add darkMode functionality")
        binding.LblMyAnswer.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
        binding.LblQuestion.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
        binding.LblNumberOfQuestion.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
        binding.LblSolution.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
        binding.LytMain.background=ContextCompat.getDrawable(context!!, R.color.white)
        binding.TxtAnswer.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
    }

    //TODO use this function at the end of the test
    private fun showTestCompletedDialog(){
        val mDialogView =
            LayoutInflater.from(activity).inflate(R.layout.test_completed_dialog, null)
        val mBuilder =
            AlertDialog.Builder(activity).setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mDialogView.btnSetup.setOnClickListener {
            //TODO navigation to tes setup
        }
    }


}
