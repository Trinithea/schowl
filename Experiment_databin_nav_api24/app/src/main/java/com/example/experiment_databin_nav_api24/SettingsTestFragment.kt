package com.example.experiment_databin_nav_api24


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
import com.example.experiment_databin_nav_api24.databinding.FragmentCategoriesBinding
import com.example.experiment_databin_nav_api24.databinding.TestSetupBinding
import kotlinx.android.synthetic.main.fragment_categories.*


class SettingsTestFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: TestSetupBinding = DataBindingUtil.inflate(inflater, R.layout.test_setup,container,false)
        setHasOptionsMenu(true)

        var currentNightMode = Configuration.UI_MODE_NIGHT_MASK
        val darkmode:Boolean = currentNightMode == Configuration.UI_MODE_NIGHT_YES
        if(darkmode == true){
            setLayout(binding)
        }

        setSeekBarListener(binding.LblCurrentMinDif,binding.SkbMinDif)
        setSeekBarListener(binding.LblCurrentMaxDif,binding.SkbMaxDif)

        (activity as AppCompatActivity).supportActionBar?.title = "CREATE YOUR TEST"
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.confirm_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!,
            view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }

    fun setLayout(binding: TestSetupBinding){

            binding.LblSubject.setTextColor(getResources().getColor(R.color.navyBlue))
            binding.LblCategory.setTextColor(getResources().getColor(R.color.navyBlue))
            binding.LblMinDif.setTextColor(getResources().getColor(R.color.navyBlue))
            binding.LblMaxDif.setTextColor(getResources().getColor(R.color.navyBlue))
            binding.LblCurrentMinDif.setTextColor(getResources().getColor(R.color.navyBlue))
            binding.LblCurrentMaxDif.setTextColor(getResources().getColor(R.color.navyBlue))
            binding.LytMainConstraint.background = resources.getDrawable(R.color.white)
            binding.SwitchAllCategories.setTextColor(getResources().getColor(R.color.navyBlue))

    }

    fun setSeekBarListener(label:TextView,seekBar: SeekBar){
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

    fun addCategory(name:String,binding:TestSetupBinding){
        var category = CheckBox(activity)
        var params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)

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
