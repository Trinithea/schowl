package cz.cizlmazna.schowl.ui.subjects.categories


import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import cz.cizlmazna.schowl.R
import cz.cizlmazna.schowl.database.Category
import cz.cizlmazna.schowl.database.SchowlDatabase
import cz.cizlmazna.schowl.databinding.FragmentCategoriesBinding
import kotlinx.android.synthetic.main.add_subject_dialog.view.*

class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding

    private lateinit var viewModel: CategoriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_categories,container,false)

        val application = requireNotNull(this.activity).application

        val arguments = CategoriesFragmentArgs.fromBundle(arguments!!)

        val databaseDao = SchowlDatabase.getInstance(application).schowlDatabaseDao

        val viewModelFactory = CategoriesViewModelFactory(databaseDao, arguments.subjectId)

        viewModel = ViewModelProvider(this, viewModelFactory).get(CategoriesViewModel::class.java)

        (activity as AppCompatActivity).supportActionBar?.title = "CATEGORIES" // TODO hardcoded string, also also should include Subject name

      

        binding.btnAddCategory.setOnClickListener {
            val mDialogView = LayoutInflater.from(activity).inflate(R.layout.add_subject_dialog,null)
            val mBuilder = AlertDialog.Builder(activity).setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            mDialogView.TxtName.hint = "Name of category"
            mDialogView.BtnAdd.setOnClickListener{
                mAlertDialog.dismiss()
                val name = mDialogView.TxtName.text.toString()
                viewModel.addCategory(name)
            }

        }

        binding.lifecycleOwner = this

        viewModel.categories.observe(viewLifecycleOwner, Observer {
            generateCategoriesList(it)
        })

        return binding.root
    }

    private fun generateCategoriesList(categories: List<Category>) {
        binding.LytItems.removeAllViews()
        binding.LytOptions.removeAllViews()
        for (category in categories) {
            addCategory(category)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.category_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, view!!.findNavController()) || super.onOptionsItemSelected(item)
    }

    private fun addCategory(category: Category){
        val newLyt = LinearLayout(activity)
        val btnNewCategory = Button(activity)
        val btnEdit=ImageButton(activity)
        val btnRemove=ImageButton(activity)
        val btnTest=ImageButton(activity)
        val ll: LinearLayout.LayoutParams=LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        newLyt.layoutParams = ll
        newLyt.orientation = LinearLayout.HORIZONTAL

        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT)
        params.weight = 1f

        btnNewCategory.layoutParams = params

        btnNewCategory.background = ContextCompat.getDrawable(context!!, R.drawable.transparent)
        btnNewCategory.text = category.name

        btnNewCategory.gravity = Gravity.START
        btnNewCategory.setOnClickListener{
                view: View ->
            Navigation.findNavController(view).navigate(CategoriesFragmentDirections.actionCategoriesFragmentToQuestionsFragment(category.id))
        }

        btnEdit.layoutParams = params
        btnEdit.setImageResource(R.drawable.ic_edit_yellow)
        btnEdit.background= ContextCompat.getDrawable(context!!, R.drawable.transparent)
        btnEdit.setOnClickListener{
            val mDialogView = LayoutInflater.from(activity).inflate(R.layout.add_subject_dialog,null)
            val mBuilder = AlertDialog.Builder(activity).setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            mDialogView.TxtName.setText(category.name)
            mDialogView.BtnAdd.text = "EDIT"

            mDialogView.BtnAdd.setOnClickListener{
                mAlertDialog.dismiss()
                viewModel.editCategory(category, mDialogView.TxtName.text.toString())
//                btnNewCategory.text = mDialogView.TxtName.text.toString()
            }
        }

        btnRemove.layoutParams = params
        btnRemove.setImageResource(R.drawable.ic_remove_yellow)
        btnRemove.background= ContextCompat.getDrawable(context!!, R.drawable.transparent)
        btnRemove.setOnClickListener{
            viewModel.removeCategory(category)
//            binding.LytItems.removeView(btnNewCategory)
//            binding.LytOptions.removeView(newLyt)
        }

        btnTest.layoutParams = params
        btnTest.setImageResource(R.drawable.ic_test_yellow)
        btnTest.background= ContextCompat.getDrawable(context!!, R.drawable.transparent)
        btnTest.setOnClickListener{
                view: View ->
            Navigation.findNavController(view).navigate(R.id.action_categoriesFragment_to_testFragment)
        }

        binding.LytItems.addView(btnNewCategory)
        newLyt.addView(btnEdit)
        newLyt.addView(btnRemove)
        newLyt.addView(btnTest)

        // newLyt.addView(binding.LytOptions)
        binding.LytOptions.addView(newLyt)

        //Toast.makeText(activity,"Subject added.",Toast.LENGTH_SHORT).show()
    }
}
