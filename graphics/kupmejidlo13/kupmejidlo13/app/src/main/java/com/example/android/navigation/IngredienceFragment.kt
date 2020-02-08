/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.android.navigation.databinding.FragmentIngredienceBinding

class IngredienceFragment : Fragment() {
    lateinit var jidlaDBHelper: JidlaDBHelper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentIngredienceBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_ingredience, container, false)

        jidlaDBHelper = JidlaDBHelper(requireContext())
        var selectedItem = ""

        // vytvoreni vyctu vsech jidel do spinneru
        var jidla = jidlaDBHelper.nactiVsechnyJidla()
        var nazvyjidel = mutableListOf<String>()
        jidla.forEach {
            if(it.sn != "")
                nazvyjidel.add(it.sn)
            if(it.ob != "")
                nazvyjidel.add(it.ob)
            if(it.ve != "")
                nazvyjidel.add(it.ve)
        }
        if (binding.spinnerJidla != null) {
            val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nazvyjidel)
            binding.spinnerJidla.adapter = arrayAdapter

            binding.spinnerJidla.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    selectedItem = nazvyjidel[position]
                    binding.txtIngr.setText(jidlaDBHelper.nactiIngr(selectedItem))
                    //Toast.makeText(this@MainActivity, getString(R.string.selected_item) + " " + personNames[position], Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }
        }

        // vloží příslušné ingredience do databáze
        // pozn. špatný název tlačítka - lepší by bylo SUBMIT BUTTON
        binding.tryAgainButton.setOnClickListener {
            jidlaDBHelper.vlozIng(selectedItem,binding.txtIngr.text.toString())
        }

        // otevře Co bude k jídlu
        binding.btnJidelnicek3.setOnClickListener { view: View ->
            Navigation.findNavController(view).navigate(R.id.action_gameOverFragment_to_gameFragment)
        }

        // otevře Nákupní seznam
        binding.btnNakupak3.setOnClickListener { view: View ->
            Navigation.findNavController(view).navigate(R.id.action_gameOverFragment_to_gameWonFragment)
        }

        // otevře Poznámky
        binding.btnPozn3.setOnClickListener { view: View ->
            Navigation.findNavController(view).navigate(R.id.action_gameOverFragment_to_rulesFragment2)
        }

        return binding.root
    }
}
