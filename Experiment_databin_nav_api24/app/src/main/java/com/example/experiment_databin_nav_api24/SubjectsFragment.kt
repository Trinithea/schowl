package com.example.experiment_databin_nav_api24


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
import com.example.experiment_databin_nav_api24.databinding.FragmentCategoriesBinding
import com.example.experiment_databin_nav_api24.databinding.FragmentSubjectsBinding
import kotlinx.android.synthetic.main.add_subject_dialog.view.*
import kotlinx.coroutines.newFixedThreadPoolContext
import android.R.attr.button
import android.widget.LinearLayout
import androidx.navigation.Navigation


class SubjectsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSubjectsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_subjects,container,false)
        (activity as AppCompatActivity).supportActionBar?.title = "SUBJECTS"
        //setHasOptionsMenu(true)
        var name = "subject"
        binding.btnAddSubject.setOnClickListener {
            val mDialogView = LayoutInflater.from(activity).inflate(R.layout.add_subject_dialog,null)
            val mBuilder = AlertDialog.Builder(activity).setView(mDialogView).setTitle("ADD SUBJECT")
            val mAlertDialog = mBuilder.show()

            mDialogView.BtnAdd.setOnClickListener{
                mAlertDialog.dismiss()
                name = mDialogView.TxtName.text.toString()
                addSubject(name,binding)
            }

        }
        return binding.root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.category_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!,
            view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }

    fun addSubject(name:String,binding: FragmentSubjectsBinding){

        var newLyt :LinearLayout = LinearLayout(activity)
        var btnNewSubject = Button(activity)
        var btnEdit=ImageButton(activity)
        var btnRemove=ImageButton(activity)
        var btnTest=ImageButton(activity)
        var ll :LinearLayout.LayoutParams=LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        newLyt.layoutParams = ll
        newLyt.orientation = LinearLayout.HORIZONTAL

        var params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT)
        params.weight = 1f

        btnNewSubject.layoutParams = params

        btnNewSubject.background = resources.getDrawable(R.drawable.trasparent)
        btnNewSubject.setTextColor(getResources().getColor(R.color.white) )
        btnNewSubject.setText(name)

        btnNewSubject.gravity = Gravity.LEFT
        btnNewSubject.setOnClickListener{
                view: View ->
            Navigation.findNavController(view).navigate(R.id.action_subjectsFragment_to_categoriesFragment)
        }


        btnEdit.layoutParams = params
        btnEdit.setImageResource(R.drawable.ic_icon_edit)
        btnEdit.background= resources.getDrawable(R.drawable.trasparent)
        btnEdit.setOnClickListener{
            val mDialogView = LayoutInflater.from(activity).inflate(R.layout.add_subject_dialog,null)
            val mBuilder = AlertDialog.Builder(activity).setView(mDialogView).setTitle("EDIT SUBJECT")
            val mAlertDialog = mBuilder.show()
            mDialogView.TxtName.setText(name)
            mDialogView.BtnAdd.setText("EDIT")

            mDialogView.BtnAdd.setOnClickListener{
                mAlertDialog.dismiss()
                btnNewSubject.setText(mDialogView.TxtName.text.toString())
            }
        }

        btnRemove.layoutParams = params
        btnRemove.setImageResource(R.drawable.ic_icon_remove)
        btnRemove.background= resources.getDrawable(R.drawable.trasparent)
        btnRemove.setOnClickListener{
            binding.LytItems.removeView(btnNewSubject)
            binding.LytOptions.removeView(newLyt)
        }

        btnTest.layoutParams = params
        btnTest.setImageResource(R.drawable.ic_test)
        btnTest.background= resources.getDrawable(R.drawable.trasparent)
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
