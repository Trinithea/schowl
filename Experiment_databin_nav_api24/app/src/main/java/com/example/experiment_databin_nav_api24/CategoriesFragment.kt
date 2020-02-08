package com.example.experiment_databin_nav_api24


import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.experiment_databin_nav_api24.databinding.FragmentCategoriesBinding
import kotlinx.android.synthetic.main.add_subject_dialog.view.*

class CategoriesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val binding: FragmentCategoriesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_categories,container,false)
        (activity as AppCompatActivity).supportActionBar?.title = "CATEGORIES"
        setHasOptionsMenu(true)
        var name = "subject"
        binding.btnAddCategory.setOnClickListener {
            val mDialogView = LayoutInflater.from(activity).inflate(R.layout.add_subject_dialog,null)
            val mBuilder = AlertDialog.Builder(activity).setView(mDialogView).setTitle("ADD CATEGORY")
            val mAlertDialog = mBuilder.show()
            mDialogView.BtnAdd.hint = "Add category"
            mDialogView.BtnAdd.setOnClickListener{
                mAlertDialog.dismiss()
                name = mDialogView.TxtName.text.toString()
                addCategory(name,binding)
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

    fun addCategory(name:String,binding: FragmentCategoriesBinding){

        var newLyt :LinearLayout = LinearLayout(activity)
        var btnNewCategory = Button(activity)
        var btnEdit=ImageButton(activity)
        var btnRemove=ImageButton(activity)
        var btnTest=ImageButton(activity)
        var ll :LinearLayout.LayoutParams=LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        newLyt.layoutParams = ll
        newLyt.orientation = LinearLayout.HORIZONTAL

        var params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT)
        params.weight = 1f

        btnNewCategory.layoutParams = params

        btnNewCategory.background = resources.getDrawable(R.drawable.trasparent)
        btnNewCategory.setTextColor(getResources().getColor(R.color.white) )
        btnNewCategory.setText(name)

        btnNewCategory.gravity = Gravity.LEFT
        btnNewCategory.setOnClickListener{
                view: View ->
            Navigation.findNavController(view).navigate(R.id.action_categoriesFragment_to_questionsFragment)
        }


        btnEdit.layoutParams = params
        btnEdit.setImageResource(R.drawable.ic_icon_edit)
        btnEdit.background= resources.getDrawable(R.drawable.trasparent)
        btnEdit.setOnClickListener{
            val mDialogView = LayoutInflater.from(activity).inflate(R.layout.add_subject_dialog,null)
            val mBuilder = AlertDialog.Builder(activity).setView(mDialogView).setTitle("EDIT CATEGORY")
            val mAlertDialog = mBuilder.show()
            mDialogView.TxtName.setText(name)
            mDialogView.BtnAdd.setText("EDIT")

            mDialogView.BtnAdd.setOnClickListener{
                mAlertDialog.dismiss()
                btnNewCategory.setText(mDialogView.TxtName.text.toString())
            }
        }

        btnRemove.layoutParams = params
        btnRemove.setImageResource(R.drawable.ic_icon_remove)
        btnRemove.background= resources.getDrawable(R.drawable.trasparent)
        btnRemove.setOnClickListener{
            binding.LytItems.removeView(btnNewCategory)
            binding.LytOptions.removeView(newLyt)
        }

        btnTest.layoutParams = params
        btnTest.setImageResource(R.drawable.ic_test)
        btnTest.background= resources.getDrawable(R.drawable.trasparent)
        //TODO setOnClickListener

        binding.LytItems.addView(btnNewCategory)
        newLyt.addView(btnEdit)
        newLyt.addView(btnRemove)
        newLyt.addView(btnTest)

        // newLyt.addView(binding.LytOptions)
        binding.LytOptions.addView(newLyt)


        //Toast.makeText(activity,"Subject added.",Toast.LENGTH_SHORT).show()
    }
}
