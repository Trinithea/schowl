package cz.cizlmazna.schowl.ui.subjects

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        val adapter = SubjectAdapter()
        binding.subjectsList.adapter = adapter

        binding.lifecycleOwner = this

        viewModel.subjects.observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.submitList(it)
            }
        })

        if (!darkMode) {
            setLayoutToLightMode()
        }

        return binding.root
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return NavigationUI.onNavDestinationSelected(item, view!!.findNavController()) || super.onOptionsItemSelected(item)
//    }


    private fun setDialog(textView: TextView, ll: LinearLayout) {
        if (!darkMode) {
            ll.background = ContextCompat.getDrawable(context!!, R.color.white)
            textView.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
        } else {
            ll.background = ContextCompat.getDrawable(context!!, R.color.navyBlue)
            textView.setTextColor(ContextCompat.getColor(context!!, R.color.ivoryYellow))
        }
    }

    fun editButtonClicked(subject: Subject){
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


    private fun addSubject(subject: Subject) {

        val optionsLyt = LinearLayout(activity)
        val btnNewSubject = Button(activity)
        val btnEdit = ImageButton(activity)
        val btnRemove = ImageButton(activity)
        val btnTest = ImageButton(activity)
        val ll: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, // width
            ViewGroup.LayoutParams.MATCH_PARENT // height
        )
        val mainLyt = ConstraintLayout(activity)

        optionsLyt.layoutParams = ll
        optionsLyt.orientation = LinearLayout.HORIZONTAL

        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        params.weight = 1f

        btnNewSubject.layoutParams = params
        btnNewSubject.background = ContextCompat.getDrawable(context!!, R.drawable.transparent)

        btnNewSubject.text = subject.name

        btnNewSubject.gravity = Gravity.START
        btnNewSubject.setOnClickListener { view: View ->
            Navigation.findNavController(view).navigate(
                SubjectsFragmentDirections.actionSubjectsFragmentToCategoriesFragment(subject.id)
            )
        }
        if (!darkMode) {
            btnNewSubject.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
            btnEdit.setImageResource(R.drawable.ic_edit_blue)
            btnRemove.setImageResource(R.drawable.ic_remove_blue)
            btnTest.setImageResource(R.drawable.ic_test_blue)
        } else {
            btnNewSubject.setTextColor(ContextCompat.getColor(context!!, R.color.ivoryYellow))
            btnEdit.setImageResource(R.drawable.ic_edit_yellow)
            btnRemove.setImageResource(R.drawable.ic_remove_yellow)
            btnTest.setImageResource(R.drawable.ic_test_yellow)

        }


        btnEdit.layoutParams = params
        btnEdit.background = ContextCompat.getDrawable(context!!, R.drawable.transparent)
        btnEdit.setOnClickListener {
            val mDialogView =
                View.inflate(activity, R.layout.add_dialog, null)
            val mBuilder =
                AlertDialog.Builder(activity).setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            mDialogView.TxtName.setText(subject.name)
            mDialogView.BtnAdd.text = getString(R.string.edit)

            setDialog(mDialogView.TxtName, mDialogView.llMain)
            mDialogView.BtnAdd.setOnClickListener {
                mAlertDialog.dismiss()
                viewModel.editSubject(subject, mDialogView.TxtName.text.toString())
            }
        }

        btnRemove.layoutParams = params
        btnRemove.background = ContextCompat.getDrawable(context!!, R.drawable.transparent)
        btnRemove.setOnClickListener {
            val mDialogView =
                View.inflate(activity, R.layout.remove_dialog, null)
            val mBuilder =
                AlertDialog.Builder(activity).setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            mDialogView.txtMessage.text = getString(R.string.remove_dialog, subject.name)

            setDialog(mDialogView.txtMessage, mDialogView.LlMainRemove)
            mDialogView.btnRemove.setOnClickListener {
                mAlertDialog.dismiss()
                viewModel.removeSubject(subject)
            }
            mDialogView.btnCancel.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }

        btnTest.layoutParams = params
        btnTest.background = ContextCompat.getDrawable(context!!, R.drawable.transparent)
        btnTest.setOnClickListener { view: View ->
            Navigation.findNavController(view)
                .navigate(SubjectsFragmentDirections.actionNavSubjectsToNavTest(subject.id, -1))
        }

        optionsLyt.addView(btnEdit)
        optionsLyt.addView(btnRemove)
        optionsLyt.addView(btnTest)
        if (subject.name.length > 27) {
            val displayMetrics = DisplayMetrics()
            (context as Activity).windowManager
                .defaultDisplay
                .getMetrics(displayMetrics)
            val param = LinearLayout.LayoutParams(
                (activity as MainActivity).dpToPx(260),
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            btnNewSubject.layoutParams = param
        }

        mainLyt.id = View.generateViewId()
        btnNewSubject.id = View.generateViewId()
        optionsLyt.id = View.generateViewId()

        mainLyt.addView(btnNewSubject)
        mainLyt.addView(optionsLyt)
        val set = ConstraintSet()
        set.clone(mainLyt)
        set.connect(
            btnNewSubject.id,
            ConstraintSet.LEFT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.LEFT
        )
        set.connect(btnNewSubject.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        set.connect(
            optionsLyt.id,
            ConstraintSet.RIGHT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.RIGHT
        )
        set.connect(optionsLyt.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        set.connect(
            optionsLyt.id,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )
        set.applyTo(mainLyt)

      //  binding.llMain.addView(mainLyt)
    }


    private fun setLayoutToLightMode() {
        binding.LytMain.background = ContextCompat.getDrawable(context!!, R.color.white)
    }
}
