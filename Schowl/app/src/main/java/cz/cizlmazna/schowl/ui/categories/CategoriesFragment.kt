package cz.cizlmazna.schowl.ui.categories

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import cz.cizlmazna.schowl.MainActivity
import cz.cizlmazna.schowl.R
import cz.cizlmazna.schowl.database.Category
import cz.cizlmazna.schowl.databinding.FragmentCategoriesBinding
import kotlinx.android.synthetic.main.add_dialog.view.*
import kotlinx.android.synthetic.main.remove_dialog.view.*

class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding

    private lateinit var viewModel: CategoriesViewModel
    private var darkMode = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_categories, container, false)

        darkMode = (activity as MainActivity).getDarkMode()

        val application = requireNotNull(this.activity).application

        val arguments =
            CategoriesFragmentArgs.fromBundle(
                arguments!!
            )

        val viewModelFactory =
            CategoriesViewModelFactory(
                application,
                arguments.subjectId
            )

        viewModel = ViewModelProvider(this, viewModelFactory).get(CategoriesViewModel::class.java)

        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.categories) // TODO should include Subject name


        binding.btnAddCategory.setOnClickListener {
            val dialogView = View.inflate(activity, R.layout.add_dialog, null)
            val builder = AlertDialog.Builder(activity).setView(dialogView)
            val alertDialog = builder.show()
            dialogView.TxtName.hint = getString(R.string.name_of_category)
            setDialog(dialogView.TxtName, dialogView.llMain)
            dialogView.BtnAdd.setOnClickListener {
                alertDialog.dismiss()
                val name = dialogView.TxtName.text.toString()
                viewModel.addCategory(name)
            }

        }

        val adapter = CategoryAdapter(this)
        binding.categoriesList.adapter = adapter

        binding.lifecycleOwner = this

        viewModel.categories.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })



        if (!darkMode) {
            setLayoutToLightMode()
        }
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.category_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            view!!.findNavController()
        ) || super.onOptionsItemSelected(item)
    }

    private fun setLayoutToLightMode() {
        binding.LytMain.background = ContextCompat.getDrawable(context!!, R.color.white)
    }

    private fun setDialog(textView: TextView, ll: LinearLayout) {
        if (!darkMode) {
            ll.background = ContextCompat.getDrawable(context!!, R.color.white)
            textView.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
        } else {
            ll.background = ContextCompat.getDrawable(context!!, R.color.navyBlue)
            textView.setTextColor(ContextCompat.getColor(context!!, R.color.ivoryYellow))
        }
    }

    fun editButtonClicked(category: Category) {
        val dialogView = View.inflate(activity, R.layout.add_dialog, null)
        val builder = AlertDialog.Builder(activity).setView(dialogView)
        val alertDialog = builder.show()
        dialogView.TxtName.setText(category.name)
        dialogView.BtnAdd.text = getString(R.string.edit)

        setDialog(dialogView.TxtName, dialogView.llMain)
        dialogView.BtnAdd.setOnClickListener {
            alertDialog.dismiss()
            viewModel.editCategory(category, dialogView.TxtName.text.toString())
        }
    }

    fun removeButtonClicked(category: Category) {
        val dialogView =
            View.inflate(activity, R.layout.remove_dialog, null)
        val builder =
            AlertDialog.Builder(activity).setView(dialogView)
        val alertDialog = builder.show()
        dialogView.txtMessage.text = getString(R.string.remove_dialog, category.name)

        setDialog(dialogView.txtMessage, dialogView.LlMainRemove)
        dialogView.btnRemove.setOnClickListener {
            alertDialog.dismiss()
            viewModel.removeCategory(category)
        }
        dialogView.btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }
    }


}
