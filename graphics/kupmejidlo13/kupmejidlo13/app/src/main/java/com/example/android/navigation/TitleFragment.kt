package com.example.android.navigation
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.android.navigation.databinding.FragmentTitleBinding

// Hlavní stránka

class TitleFragment : Fragment() {

    lateinit var jidlaDBHelper: JidlaDBHelper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentTitleBinding>(inflater, R.layout.fragment_title, container, false)

        jidlaDBHelper = JidlaDBHelper(requireContext())

        // Vytvoří nový jídelníček
        binding.btnNew.setOnClickListener { view: View ->
            jidlaDBHelper.smazVsechno()
            Navigation.findNavController(view).navigate(R.id.action_titleFragment_to_gameFragment)
        }

        // Nahraje již rozpracovaný jídelníček
        binding.btnOld.setOnClickListener { view :View ->
            Navigation.findNavController(view).navigate(R.id.action_titleFragment_to_gameFragment)
        }

        // Přejde rovnou na Poznámky
        binding.btnNotes.setOnClickListener { view: View ->
            Navigation.findNavController(view).navigate(R.id.action_titleFragment_to_rulesFragment)
        }
        return binding.root
    }


}
