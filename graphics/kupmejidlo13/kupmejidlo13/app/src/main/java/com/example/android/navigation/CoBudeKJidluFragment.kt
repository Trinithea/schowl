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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.android.navigation.databinding.FragmentCoBudeKJidluBinding

class CoBudeKJidluFragment : Fragment() {
    data class Question(
            val text: String,
            val answers: List<String>)

    // díky JidlaDbHelper můžu komunikovat s databází
    lateinit var jidlaDBHelper: JidlaDBHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        jidlaDBHelper = JidlaDBHelper(requireContext())
        var aktualniden = ""

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentCoBudeKJidluBinding>(
                inflater, R.layout.fragment_co_bude_k_jidlu, container, false)

        fun vlozDen() {
            var nazev = aktualniden
            var snidane = binding.editText.text.toString()
            var obed = binding.editText2.text.toString()
            var vecere = binding.editText3.text.toString()

            if (jidlaDBHelper.vlozDen(DenModel(den=aktualniden,sn=snidane, ob=obed, ve=vecere, ingr_sn ="", ingr_ob = "",ingr_ve = ""))==false)
            {
                jidlaDBHelper.smazDen(aktualniden)
                jidlaDBHelper.vlozDen(DenModel(den=aktualniden,sn=snidane, ob=obed, ve=vecere, ingr_sn ="", ingr_ob = "",ingr_ve = ""))
            }
        }

        // přepíše databázi a uloží tak nová tři jídla do zvoleného dne
        binding.submitButton.setOnClickListener {
            //view: View ->
            //Navigation.findNavController(view).navigate(R.id.action_gameFragment_to_gameOverFragment)
            vlozDen()
        }

        // nic neukládá, pouze přejde do Ingrediencí
        binding.btnIngr.setOnClickListener { view: View ->
            Navigation.findNavController(view).navigate(R.id.action_gameFragment_to_gameOverFragment)}

        // přejde na Nákupní seznam
        binding.btnNakupak.setOnClickListener { view: View ->
            Navigation.findNavController(view).navigate(R.id.action_gameFragment_to_gameWonFragment)}

        // přejde na Poznámky
        binding.btnPozn.setOnClickListener { view: View ->
            Navigation.findNavController(view).navigate(R.id.action_gameFragment_to_rulesFragment2)}


        fun NactiDen(den:DenModel) {
            binding.editText.setText(den.sn.toString())
            binding.editText2.setText(den.ob.toString())
            binding.editText3.setText(den.ve.toString())
        }

        // nasledujici tlacitka zmeni aktualni den a barvu svoji i ostatnich tlacitek
        binding.btnPo.setOnClickListener {
            //vlozDen()
            aktualniden = "pondeli"
            binding.btnPo.setBackgroundResource(R.drawable.circlebutton_red)
            binding.btnUt.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnSt.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnT.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnPa.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnSo.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnNe.setBackgroundResource(R.drawable.circlebutton_ice)

            val den :DenModel = jidlaDBHelper.nactiDen(aktualniden)
            NactiDen(den)
        }

        binding.btnUt.setOnClickListener {
            //vlozDen()
            aktualniden = "utery"
            binding.btnPo.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnUt.setBackgroundResource(R.drawable.circlebutton_red)
            binding.btnSt.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnT.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnPa.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnSo.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnNe.setBackgroundResource(R.drawable.circlebutton_ice)

            val den :DenModel = jidlaDBHelper.nactiDen(aktualniden)
            NactiDen(den)
        }

        binding.btnSt.setOnClickListener {
           // vlozDen()
            aktualniden = "streda"
            binding.btnPo.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnUt.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnSt.setBackgroundResource(R.drawable.circlebutton_red)
            binding.btnT.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnPa.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnSo.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnNe.setBackgroundResource(R.drawable.circlebutton_ice)

            val den :DenModel = jidlaDBHelper.nactiDen(aktualniden)
            NactiDen(den)
        }

        binding.btnT.setOnClickListener {
            //vlozDen()
            aktualniden = "ctvrtek"
            binding.btnPo.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnUt.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnSt.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnT.setBackgroundResource(R.drawable.circlebutton_red)
            binding.btnPa.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnSo.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnNe.setBackgroundResource(R.drawable.circlebutton_ice)

            val den :DenModel = jidlaDBHelper.nactiDen(aktualniden)
            NactiDen(den)
        }

        binding.btnPa.setOnClickListener {
            //vlozDen()
            aktualniden = "patek"
            binding.btnPo.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnUt.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnSt.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnT.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnPa.setBackgroundResource(R.drawable.circlebutton_red)
            binding.btnSo.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnNe.setBackgroundResource(R.drawable.circlebutton_ice)

            val den :DenModel = jidlaDBHelper.nactiDen(aktualniden)
            NactiDen(den)
        }

        binding.btnSo.setOnClickListener {
            //vlozDen()
            aktualniden = "sobota"
            binding.btnPo.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnUt.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnSt.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnT.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnPa.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnSo.setBackgroundResource(R.drawable.circlebutton_red)
            binding.btnNe.setBackgroundResource(R.drawable.circlebutton_ice)

            val den :DenModel = jidlaDBHelper.nactiDen(aktualniden)
            NactiDen(den)
        }

        binding.btnNe.setOnClickListener {
           // vlozDen()
            aktualniden = "nedele"
            binding.btnPo.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnUt.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnSt.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnT.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnPa.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnSo.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnNe.setBackgroundResource(R.drawable.circlebutton_red)

            val den :DenModel = jidlaDBHelper.nactiDen(aktualniden)
            NactiDen(den)
        }

        binding.btnPo.setOnClickListener {
            aktualniden = "pondeli"
            binding.btnPo.setBackgroundResource(R.drawable.circlebutton_red)
            binding.btnUt.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnSt.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnT.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnPa.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnSo.setBackgroundResource(R.drawable.circlebutton_ice)
            binding.btnNe.setBackgroundResource(R.drawable.circlebutton_ice)

            val den :DenModel = jidlaDBHelper.nactiDen(aktualniden)
            NactiDen(den)
        }

        return binding.root

    }

}
