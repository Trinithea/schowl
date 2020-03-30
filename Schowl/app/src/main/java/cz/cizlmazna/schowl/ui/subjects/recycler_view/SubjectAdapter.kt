package cz.cizlmazna.schowl.ui.subjects.recycler_view

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cz.cizlmazna.schowl.R
import cz.cizlmazna.schowl.database.Subject
import kotlinx.android.synthetic.main.fragment_subjects.view.*

class SubjectAdapter : RecyclerView.Adapter<>(){
    var data = listOf<Subject>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: , position :Int){
        val item = data[position]
        if(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view  = layoutInflater.inflate(R.layout.fragment_subjects, parent, false) as
                return
    }
}