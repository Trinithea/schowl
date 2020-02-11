package cz.cizlmazna.schowl.ui.test


import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColor
import androidx.core.graphics.toColorLong
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import cz.cizlmazna.schowl.R
import cz.cizlmazna.schowl.database.SchowlDatabase
import cz.cizlmazna.schowl.databinding.FragmentTestSetupBinding
import kotlinx.android.synthetic.main.fragment_categories.*


class TestSetupFragment : Fragment() {

    private lateinit var binding: FragmentTestSetupBinding

    private lateinit var viewModel: TestSetupViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_test_setup, container, false)

        val application = requireNotNull(this.activity).application

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

        val databaseDao = SchowlDatabase.getInstance(application).schowlDatabaseDao

        val viewModelFactory = TestSetupViewModelFactory(databaseDao, subjectId, categoryId)

        viewModel = ViewModelProvider(this, viewModelFactory).get(TestSetupViewModel::class.java)

       // setHasOptionsMenu(true)
        binding.btnSent.setOnClickListener {
                view: View ->
            Navigation.findNavController(view).navigate(TestSetupFragmentDirections.actionTestSetupFragmentToTestFragment(LongArray(1)))// TODO make this work, no hardcoded array
        }
        //setLayout(Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_NO)

        setSeekBarListener(binding.LblCurrentMinDif, binding.SkbMinDif)
        setSeekBarListener(binding.LblCurrentMaxDif, binding.SkbMaxDif)

        (activity as AppCompatActivity).supportActionBar?.title = "CREATE YOUR TEST" // TODO move text out of here, also it would be great to optimize the action bar a little
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.confirm_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, view!!.findNavController()) || super.onOptionsItemSelected(item)
    }

    private fun setLayout(darkMode: Boolean) {
        // TODO update to use ContextCompat
        // TODO use darkMode
        binding.LblSubject.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
        binding.LblCategory.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
        binding.LblMinDif.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
        binding.LblMaxDif.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
        binding.LblCurrentMinDif.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
        binding.LblCurrentMaxDif.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))
        binding.LytMainConstraint.background = ContextCompat.getDrawable(context!!, R.color.white)
        binding.SwitchAllCategories.setTextColor(ContextCompat.getColor(context!!, R.color.navyBlue))

    }

    private fun setSeekBarListener(label: TextView, seekBar: SeekBar) {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                label.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }

    fun addCategory(name: String) {
        val category = CheckBox(activity)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        category.layoutParams = params
        category.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.peacockBlue))
        category.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.peacockBlue))
        category.text = name
        category.setTextColor(ContextCompat.getColor(context!!, R.color.ivoryYellow))
        category.textSize = 18f
        category.typeface = Typeface.MONOSPACE
        binding.LytCategories.addView(category)

    }

}
