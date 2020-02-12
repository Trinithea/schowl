package cz.cizlmazna.schowl.ui.subjects.categories


import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
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
import kotlinx.android.synthetic.main.fragment_subjects.*
import kotlinx.android.synthetic.main.remove_dialog.view.*

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

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.categories) // TODO hardcoded string, also also should include Subject name



        binding.btnAddCategory.setOnClickListener {
            val mDialogView = LayoutInflater.from(activity).inflate(R.layout.add_subject_dialog,null)
            val mBuilder = AlertDialog.Builder(activity).setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            mDialogView.TxtName.hint = getString(R.string.name_of_category)
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
        llMain.removeAllViews()
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
        val optionsLyt = LinearLayout(activity)
        val btnNewCategory = Button(activity)
        val btnEdit=ImageButton(activity)
        val btnRemove=ImageButton(activity)
        val btnTest=ImageButton(activity)
        val ll: LinearLayout.LayoutParams=LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT)
        optionsLyt.layoutParams = ll
        optionsLyt.orientation = LinearLayout.HORIZONTAL
        val mainLyt = ConstraintLayout(activity)

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
            mDialogView.BtnAdd.text = getString(R.string.edit)

            mDialogView.BtnAdd.setOnClickListener{
                mAlertDialog.dismiss()
                viewModel.editCategory(category, mDialogView.TxtName.text.toString())
//                btnNewCategory.text = mDialogView.TxtName.text.toString()
            }
        }

        btnRemove.layoutParams = params
        btnRemove.setImageResource(R.drawable.ic_remove_yellow)
        btnRemove.background= ContextCompat.getDrawable(context!!, R.drawable.transparent)
        btnRemove.setOnClickListener {
            val mDialogView =
                LayoutInflater.from(activity).inflate(R.layout.remove_dialog, null)
            val mBuilder =
                AlertDialog.Builder(activity).setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            mDialogView.txtMessage.setText(getString(R.string.remove_dalog) + category.name + "?")
            mDialogView.btnRemove.setOnClickListener {
                mAlertDialog.dismiss()
                viewModel.removeCategory(category)
            }
            mDialogView.btnCancel.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }

        btnTest.layoutParams = params
        btnTest.setImageResource(R.drawable.ic_test_yellow)
        btnTest.background= ContextCompat.getDrawable(context!!, R.drawable.transparent)
        btnTest.setOnClickListener{
                view: View ->
            Navigation.findNavController(view).navigate(CategoriesFragmentDirections.actionCategoriesFragmentToNavTest(category.subjectId, category.id))
        }

        optionsLyt.addView(btnEdit)
        optionsLyt.addView(btnRemove)
        optionsLyt.addView(btnTest)
        if(category.name.length >27){
            val displayMetrics = DisplayMetrics()
            (context as Activity).windowManager
                .defaultDisplay
                .getMetrics(displayMetrics)
            val width = displayMetrics.widthPixels
            val param = LinearLayout.LayoutParams(dpToPx(260),
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            btnNewCategory.layoutParams = param
        }

        mainLyt.setId(View.generateViewId())
        btnNewCategory.setId(View.generateViewId())
        optionsLyt.setId(View.generateViewId())

        mainLyt.addView(btnNewCategory)
        mainLyt.addView(optionsLyt)
        val set = ConstraintSet()
        set.clone(mainLyt)
        set.connect(btnNewCategory.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        set.connect(btnNewCategory.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        set.connect(optionsLyt.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
        set.connect(optionsLyt.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        set.connect(optionsLyt.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        set.applyTo(mainLyt)
        // newLyt.addView(binding.LytOptions)
        binding.llMain.addView(mainLyt)

        //Toast.makeText(activity,"Subject added.",Toast.LENGTH_SHORT).show()
    }
    fun dpToPx(dp: Int): Int {
        val displayMetrics = context!!.resources.displayMetrics
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }
}
