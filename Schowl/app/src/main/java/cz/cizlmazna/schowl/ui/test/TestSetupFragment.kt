package cz.cizlmazna.schowl.ui.test

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import cz.cizlmazna.schowl.MainActivity
import cz.cizlmazna.schowl.R
import cz.cizlmazna.schowl.database.Category
import cz.cizlmazna.schowl.database.Subject
import cz.cizlmazna.schowl.databinding.FragmentTestSetupBinding


class TestSetupFragment : Fragment() {

    private lateinit var binding: FragmentTestSetupBinding

    private lateinit var viewModel: TestSetupViewModel
    private var darkMode = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_test_setup, container, false)

        val application = requireNotNull(this.activity).application
        darkMode = (activity as MainActivity).getDarkMode()
        val subjectId: Long
        val categoryId: Long
        if (this.arguments!!.isEmpty) {
            subjectId = -1L
            categoryId = -1L
        } else {
            val arguments = TestSetupFragmentArgs.fromBundle(arguments!!)
            subjectId = arguments.subjectId
            categoryId = arguments.categoryId
        }

        val viewModelFactory = TestSetupViewModelFactory(application, subjectId, categoryId)

        viewModel = ViewModelProvider(this, viewModelFactory).get(TestSetupViewModel::class.java)

        binding.lifecycleOwner = this
        binding.testSetupViewModel = viewModel
        if (!darkMode) {
            setLayoutToLightMode()
        }

        viewModel.getSubjects().observe(viewLifecycleOwner, Observer {
            var dataAdapter =
                ArrayAdapter<Subject>(context!!, android.R.layout.simple_spinner_item, it)
            if ((activity as MainActivity).getDarkMode()) {
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            } else {
                dataAdapter = ArrayAdapter(context!!, R.layout.spinner_item_dark, it)
                dataAdapter.setDropDownViewResource(R.layout.spinner_item_light)
            }
            binding.SpnSubject.adapter = dataAdapter
        })

        viewModel.getSelectedSubject().observe(viewLifecycleOwner, Observer {
            if (viewModel.getSubjects().value != null) {
                val dataAdapter = ArrayAdapter<Subject>(
                    context!!,
                    android.R.layout.simple_spinner_item,
                    viewModel.getSubjects().value!!
                )
                binding.SpnSubject.setSelection(dataAdapter.getPosition(it))
            }
        })

        binding.SpnSubject.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.onSubjectSelected(parent?.getItemAtPosition(position) as Subject)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                viewModel.onSubjectSelected(null)
            }
        }

        binding.SwitchAllCategories.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onAllCategoriesSelectedChange(isChecked)
        }

        viewModel.getSelectedSubjectCategories().observe(viewLifecycleOwner, Observer {
            generateCategoriesList(it)
        })

        setMinSeekBarListener(binding.SkbMinDif)
        setMaxSeekBarListener(binding.SkbMaxDif)

        viewModel.getErrorMessage().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val text = when (it) {
                    TestSetupViewModel.ErrorMessage.NO_SUBJECT_SELECTED -> getString(R.string.no_subject_selected)
                    TestSetupViewModel.ErrorMessage.NO_CATEGORIES_SELECTED -> getString(R.string.no_categories_selected)
                    TestSetupViewModel.ErrorMessage.NO_QUESTIONS_IN_CATEGORIES -> getString(R.string.no_questions_in_categories)
                }
                Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
                viewModel.doneShowingErrorMessage()
            }
        })

        viewModel.getNavigateToTest().observe(viewLifecycleOwner, Observer {
            if (it == true) {
                findNavController().navigate(
                    TestSetupFragmentDirections.actionTestSetupFragmentToTestFragment(
                        viewModel.getCategoryIds(),
                        viewModel.getMinDifficulty().value!!,
                        viewModel.getMaxDifficulty().value!!
                    )
                )
                viewModel.doneNavigatingToTest()
            }
        })

        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.create_your_test) // TODO it would be great to optimize the action bar a little

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.confirm_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            view!!.findNavController()
        ) || super.onOptionsItemSelected(item)
    }

    private fun setLayoutToLightMode() {

        binding.LblSubject.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
        binding.LblCategory.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
        binding.LblMinDif.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
        binding.LblMaxDif.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
        binding.LblCurrentMinDif.setTextColor(
            ContextCompat.getColor(
                context!!,
                R.color.navyBlue
            )
        )
        binding.LblCurrentMaxDif.setTextColor(
            ContextCompat.getColor(
                context!!,
                R.color.navyBlue
            )
        )
        binding.LytMain.background =
            ContextCompat.getDrawable(context!!, R.color.white)
        binding.SwitchAllCategories.setTextColor(
            ContextCompat.getColor(
                context!!,
                R.color.navyBlue
            )
        )

    }

    private fun setMinSeekBarListener(seekBar: SeekBar) {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.onSetMinDifficulty(seekBar!!.progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun setMaxSeekBarListener(seekBar: SeekBar) {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.onSetMaxDifficulty(seekBar!!.progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun generateCategoriesList(categories: List<Category>) {
        binding.LytCategories.removeAllViews()
        viewModel.getCategoriesChecked().removeObservers(viewLifecycleOwner)
        for (category in categories) {
            addCategory(category)
        }
    }

    private fun addCategory(category: Category) {
        val categoryCheckBox = CheckBox(activity)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        categoryCheckBox.layoutParams = params
        categoryCheckBox.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.peacockBlue))
        categoryCheckBox.buttonTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.peacockBlue))
        categoryCheckBox.text = category.name
        if (darkMode)
            categoryCheckBox.setTextColor(ContextCompat.getColor(context!!, R.color.ivoryYellow))
        else
            categoryCheckBox.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))

        categoryCheckBox.textSize = 18f
        categoryCheckBox.typeface = Typeface.MONOSPACE

        categoryCheckBox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onCategoryCheckedChange(category, isChecked)
        }

        viewModel.getCategoriesChecked().observe(viewLifecycleOwner, Observer {
            // TODO every checkbox reacts on one check change
            // observes only once
            if (it[category.id] != null)
                categoryCheckBox.isChecked = it[category.id]!!
        })

        binding.LytCategories.addView(categoryCheckBox)

    }

}
