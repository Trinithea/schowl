package cz.cizlmazna.schowl.ui.subjects


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import cz.cizlmazna.schowl.R
import cz.cizlmazna.schowl.databinding.FragmentEditQuestionBinding

class EditQuestionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentEditQuestionBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_edit_question, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "EDIT QUESTION" // TODO hardcoded string

        binding.btnSent.setOnClickListener {
            //TODO some Live data magic
                view: View ->
            Navigation.findNavController(view)
                .navigate(R.id.action_editQuestionFragment_to_questionsFragment)
        }
        return binding.root
    }
}
