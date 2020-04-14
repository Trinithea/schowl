package cz.cizlmazna.schowl.ui.subjects

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cz.cizlmazna.schowl.MainActivity
import cz.cizlmazna.schowl.R
import cz.cizlmazna.schowl.database.Subject
import cz.cizlmazna.schowl.databinding.FragmentSubjectsBinding
import kotlinx.android.synthetic.main.add_dialog.view.*
import kotlinx.android.synthetic.main.remove_dialog.view.*

class SubjectsFragment : Fragment() {

    private lateinit var binding: FragmentSubjectsBinding

    private lateinit var viewModel: SubjectsViewModel
    private var darkMode = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_subjects, container, false)

        darkMode = (activity as MainActivity).getDarkMode()

        viewModel = ViewModelProvider(this).get(SubjectsViewModel::class.java)

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.subjects)

        binding.btnAddSubject.setOnClickListener {
            val dialogView =
                View.inflate(activity, R.layout.add_dialog, null)
            val builder =
                AlertDialog.Builder(activity).setView(dialogView)
            dialogView.TxtName.hint = getString(R.string.name_of_subject)
            setDialog(dialogView.TxtName, dialogView.llMain)
            val alertDialog = builder.show()

            dialogView.BtnAdd.setOnClickListener {
                alertDialog.dismiss()
                val name = dialogView.TxtName.text.toString()
                viewModel.addSubject(name)
            }
        }

        val adapter = SubjectAdapter(this)
        binding.subjectsList.adapter = adapter

        binding.lifecycleOwner = this

        viewModel.subjects.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        if (!darkMode) {
            setLayoutToLightMode()
        }

        return binding.root
    }

    private fun setDialog(textView: TextView, linearLayout: LinearLayout) {
        if (!darkMode) {
            linearLayout.background = ContextCompat.getDrawable(linearLayout.context, R.color.white)
            textView.setTextColor(ContextCompat.getColor(textView.context, R.color.navyBlue))
        } else {
            linearLayout.background = ContextCompat.getDrawable(linearLayout.context, R.color.navyBlue)
            textView.setTextColor(ContextCompat.getColor(textView.context, R.color.ivoryYellow))
        }
    }

    fun editButtonClicked(subject: Subject) {
        val dialogView =
            View.inflate(activity, R.layout.add_dialog, null)
        val builder =
            AlertDialog.Builder(activity).setView(dialogView)
        val alertDialog = builder.show()
        dialogView.TxtName.setText(subject.name)
        dialogView.BtnAdd.text = getString(R.string.edit)

        setDialog(dialogView.TxtName, dialogView.llMain)
        dialogView.BtnAdd.setOnClickListener {
            alertDialog.dismiss()
            viewModel.editSubject(subject, dialogView.TxtName.text.toString())
        }
    }

    fun removeButtonClicked(subject: Subject) {
        val dialogView =
            View.inflate(activity, R.layout.remove_dialog, null)
        val builder =
            AlertDialog.Builder(activity).setView(dialogView)
        val alertDialog = builder.show()
        dialogView.txtMessage.text = getString(R.string.remove_dialog, subject.name)

        setDialog(dialogView.txtMessage, dialogView.LlMainRemove)
        dialogView.btnRemove.setOnClickListener {
            alertDialog.dismiss()
            viewModel.removeSubject(subject)
        }
        dialogView.btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }
    }
    
    private fun setLayoutToLightMode() {
        binding.LytMain.background = ContextCompat.getDrawable(binding.LytMain.context, R.color.white)
    }
}
