package cz.cizlmazna.schowl.ui.subjects


import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.add_subject_dialog.view.*
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.navArgs
import cz.cizlmazna.schowl.R
import cz.cizlmazna.schowl.database.SchowlDatabase
import cz.cizlmazna.schowl.database.Subject
import cz.cizlmazna.schowl.databinding.FragmentSubjectsBinding


class SubjectsFragment : Fragment() {

    private lateinit var binding: FragmentSubjectsBinding

    private lateinit var viewModel: SubjectsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_subjects, container, false)

        val application = requireNotNull(this.activity).application

        val databaseDao = SchowlDatabase.getInstance(application).schowlDatabaseDao

        val viewModelFactory = SubjectsViewModelFactory(databaseDao)

        viewModel = ViewModelProvider(this, viewModelFactory).get(SubjectsViewModel::class.java)

        (activity as AppCompatActivity).supportActionBar?.title = "SUBJECTS" // TODO hardcoded string

        binding.btnAddSubject.setOnClickListener {
            val mDialogView = LayoutInflater.from(activity).inflate(R.layout.add_subject_dialog, null)
            val mBuilder = AlertDialog.Builder(activity).setView(mDialogView).setTitle("ADD SUBJECT")
            val mAlertDialog = mBuilder.show()

            mDialogView.BtnAdd.setOnClickListener {
                mAlertDialog.dismiss()
                val name = mDialogView.TxtName.text.toString()
                viewModel.addSubject(name)
            }
        }

        binding.lifecycleOwner = this

        viewModel.subjects.observe(viewLifecycleOwner, Observer {
            generateSubjectsList(it)
        })

        return binding.root
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return NavigationUI.onNavDestinationSelected(item, view!!.findNavController()) || super.onOptionsItemSelected(item)
//    }

    private fun generateSubjectsList(subjects: List<Subject>) {
        binding.LytItems.removeAllViews()
        binding.LytOptions.removeAllViews()
        for (subject in subjects) {
            addSubject(subject)
        }
    }

    private fun addSubject(subject: Subject) {

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

        btnNewSubject.background = ContextCompat.getDrawable(context!!, R.drawable.transparent)
        btnNewSubject.setTextColor(ContextCompat.getColor(context!!, R.color.white))
        btnNewSubject.text = subject.name

        btnNewSubject.gravity = Gravity.START
        btnNewSubject.setOnClickListener { view: View ->
            Navigation.findNavController(view).navigate(SubjectsFragmentDirections.actionSubjectsFragmentToCategoriesFragment(subject.id))
        }


        btnEdit.layoutParams = params
        btnEdit.setImageResource(R.drawable.ic_icon_edit)
        btnEdit.background = ContextCompat.getDrawable(context!!, R.drawable.transparent)
        btnEdit.setOnClickListener {
            val mDialogView = LayoutInflater.from(activity).inflate(R.layout.add_subject_dialog, null)
            val mBuilder = AlertDialog.Builder(activity).setView(mDialogView).setTitle("EDIT SUBJECT") // TODO Hardcoded string
            val mAlertDialog = mBuilder.show()
            mDialogView.TxtName.setText(subject.name)
            mDialogView.BtnAdd.text = "EDIT"
            mDialogView.BtnAdd.setOnClickListener {
                mAlertDialog.dismiss()
                viewModel.editSubject(subject, mDialogView.TxtName.text.toString())
//                btnNewSubject.text = mDialogView.TxtName.text.toString()
            }
        }

        btnRemove.layoutParams = params
        btnRemove.setImageResource(R.drawable.ic_icon_remove)
        btnRemove.background = ContextCompat.getDrawable(context!!, R.drawable.transparent)
        btnRemove.setOnClickListener {
//            binding.LytItems.removeView(btnNewSubject)
//            binding.LytOptions.removeView(newLyt)
            viewModel.removeSubject(subject)
        }

        btnTest.layoutParams = params
        btnTest.setImageResource(R.drawable.ic_test)
        btnTest.background = ContextCompat.getDrawable(context!!, R.drawable.transparent)
        //TODO setOnClickListener (test)

        binding.LytItems.addView(btnNewSubject)
        newLyt.addView(btnEdit)
        newLyt.addView(btnRemove)
        newLyt.addView(btnTest)

        // newLyt.addView(binding.LytOptions)
        binding.LytOptions.addView(newLyt)

        //Toast.makeText(activity,"Subject added.",Toast.LENGTH_SHORT).show()
    }
}
