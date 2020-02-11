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
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import cz.cizlmazna.schowl.R
import cz.cizlmazna.schowl.databinding.FragmentTestSetupBinding
import kotlinx.android.synthetic.main.fragment_categories.*


class TestSetupFragment : Fragment() {

    private lateinit var binding: FragmentTestSetupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_test_setup, container, false)

       // setHasOptionsMenu(true)
        binding.btnSent.setOnClickListener {
                view: View ->
            Navigation.findNavController(view).navigate(R.id.action_testSetupFragment_to_testFragment)
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
        binding.LblSubject.setTextColor(getResources().getColor(R.color.navyBlue))
        binding.LblCategory.setTextColor(getResources().getColor(R.color.navyBlue))
        binding.LblMinDif.setTextColor(getResources().getColor(R.color.navyBlue))
        binding.LblMaxDif.setTextColor(getResources().getColor(R.color.navyBlue))
        binding.LblCurrentMinDif.setTextColor(getResources().getColor(R.color.navyBlue))
        binding.LblCurrentMaxDif.setTextColor(getResources().getColor(R.color.navyBlue))
        binding.LytMainConstraint.background = resources.getDrawable(R.color.white)
        binding.SwitchAllCategories.setTextColor(getResources().getColor(R.color.navyBlue))

    }

    private fun setSeekBarListener(label: TextView, seekBar: SeekBar) {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                label.setText(progress.toString())
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
        category.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.peacockBlue)))
        category.setButtonTintList(ColorStateList.valueOf(resources.getColor(R.color.peacockBlue)))
        category.setText(name)
        category.setTextColor(getResources().getColor(R.color.ivoryYellow))
        category.setTextSize(18f)
        category.setTypeface(Typeface.MONOSPACE)
        binding.LytCategories.addView(category)

    }

}
