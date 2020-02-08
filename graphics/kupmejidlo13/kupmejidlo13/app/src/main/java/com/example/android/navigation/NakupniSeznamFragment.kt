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
import android.widget.CheckBox
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.android.navigation.databinding.FragmentNakupniSeznamBinding


class NakupniSeznamFragment : Fragment() {
    lateinit var jidlaDBHelper: JidlaDBHelper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentNakupniSeznamBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_nakupni_seznam, container, false)

        jidlaDBHelper = JidlaDBHelper(requireContext())

        fun showNakupak() {
            var jidla = jidlaDBHelper.nactiVsechnyJidla()
            binding.llPolozky.removeAllViews()
            jidla.forEach {
                val ingrs_sn = it.ingr_sn.toString().split("\n")
                val ingrs_ob = it.ingr_ob.toString().split("\n")
                val ingrs_ve = it.ingr_ve.toString().split("\n")

                val finalList = ingrs_sn + ingrs_ob + ingrs_ve

                val unionList = finalList.distinct()

                for (ingr in unionList) {
                    if (ingr != "") {
                        var cb_ingr = CheckBox(requireContext())
                        cb_ingr.textSize = 20F
                        cb_ingr.text = ingr
                        // + " (" + finalList.count { it == ingr } + ")"
                        binding.llPolozky.addView(cb_ingr)
                    }
                }
            }
        }

        showNakupak()

        binding.btnJidelnicek4.setOnClickListener { view: View ->
            Navigation.findNavController(view).navigate(R.id.action_gameWonFragment_to_gameFragment)
        }

        binding.btnIngr4.setOnClickListener { view: View ->
            Navigation.findNavController(view).navigate(R.id.action_gameWonFragment_to_gameOverFragment)
        }

        binding.btnPozn4.setOnClickListener { view: View ->
            Navigation.findNavController(view).navigate(R.id.action_gameWonFragment_to_rulesFragment2)
        }

        return binding.root
    }
}
