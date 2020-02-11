package cz.cizlmazna.schowl.ui.test


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import cz.cizlmazna.schowl.R
import cz.cizlmazna.schowl.databinding.FragmentTestBinding

class TestFragment : Fragment() {

    private lateinit var binding: FragmentTestBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_test, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "TEST"

        binding.BtnShowSolution.setOnClickListener{
            binding.BtnShowSolution.visibility = View.GONE
            binding.LblSolution.visibility = View.VISIBLE
            binding.LytEditNext.visibility = View.VISIBLE
        }
        //setLayout(true)
        binding.BtnNext.setOnClickListener {
                view: View ->
            Navigation.findNavController(view).navigate(R.id.action_testFragment_self)
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


}
