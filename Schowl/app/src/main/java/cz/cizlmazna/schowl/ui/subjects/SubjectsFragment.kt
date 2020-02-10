package cz.cizlmazna.schowl.ui.subjects


import android.app.AlertDialog
import android.os.Bundle
import android.text.Layout
import android.text.style.AlignmentSpan
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginLeft
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.add_subject_dialog.view.*
import kotlinx.coroutines.newFixedThreadPoolContext
import android.R.attr.button
import android.widget.LinearLayout
import androidx.navigation.Navigation
import cz.cizlmazna.schowl.R
import cz.cizlmazna.schowl.databinding.FragmentSubjectsBinding


class SubjectsFragment : Fragment() {

    private lateinit var binding: FragmentSubjectsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_subjects, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "SUBJECTS" // TODO hardcoded string
        //setHasOptionsMenu(true)
        binding.btnAddSubject.setOnClickListener {
            val mDialogView =
                LayoutInflater.from(activity).inflate(R.layout.add_subject_dialog, null)
            val mBuilder =
                AlertDialog.Builder(activity).setView(mDialogView).setTitle("ADD SUBJECT")
            val mAlertDialog = mBuilder.show()

            mDialogView.BtnAdd.setOnClickListener {
                mAlertDialog.dismiss()
                val name = mDialogView.TxtName.text.toString()
                addSubject(name, binding)
            }

        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.category_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, view!!.findNavController()) || super.onOptionsItemSelected(item)
    }

    private fun addSubject(name: String, binding: FragmentSubjectsBinding) {

        val newLyt = LinearLayout(activity)
        val btnNewSubject = Button(activity)
        val btnEdit = ImageButton(activity)
        val btnRemove = ImageButton(activity)
        val btnTest = ImageButton(activity)
        val ll: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        newLyt.layoutParams = ll
        newLyt.orientation = LinearLayout.HORIZONTAL

        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        params.weight = 1f

        btnNewSubject.layoutParams = params

        btnNewSubject.background = resources.getDrawable(R.drawable.transparent)
        btnNewSubject.setTextColor(getResources().getColor(R.color.white))
        btnNewSubject.setText(name)

        btnNewSubject.gravity = Gravity.START
        btnNewSubject.setOnClickListener { view: View ->
            Navigation.findNavController(view)
                .navigate(R.id.action_subjectsFragment_to_categoriesFragment)
        }


        btnEdit.layoutParams = params
        btnEdit.setImageResource(R.drawable.ic_icon_edit)
        btnEdit.background = resources.getDrawable(R.drawable.transparent)
        btnEdit.setOnClickListener {
            val mDialogView =
                LayoutInflater.from(activity).inflate(R.layout.add_subject_dialog, null)
            val mBuilder =
                AlertDialog.Builder(activity).setView(mDialogView).setTitle("EDIT SUBJECT") // TODO Hardcoded string
            val mAlertDialog = mBuilder.show()
            mDialogView.TxtName.setText(name)
            mDialogView.BtnAdd.setText("EDIT")

            mDialogView.BtnAdd.setOnClickListener {
                mAlertDialog.dismiss()
                btnNewSubject.setText(mDialogView.TxtName.text.toString())
            }
        }

        btnRemove.layoutParams = params
        btnRemove.setImageResource(R.drawable.ic_icon_remove)
        btnRemove.background = resources.getDrawable(R.drawable.transparent)
        btnRemove.setOnClickListener {
            binding.LytItems.removeView(btnNewSubject)
            binding.LytOptions.removeView(newLyt)
        }

        btnTest.layoutParams = params
        btnTest.setImageResource(R.drawable.ic_test)
        btnTest.background = resources.getDrawable(R.drawable.transparent)
        //TODO setOnClickListener

        binding.LytItems.addView(btnNewSubject)
        newLyt.addView(btnEdit)
        newLyt.addView(btnRemove)
        newLyt.addView(btnTest)

        // newLyt.addView(binding.LytOptions)
        binding.LytOptions.addView(newLyt)


        //Toast.makeText(activity,"Subject added.",Toast.LENGTH_SHORT).show()
    }
}
