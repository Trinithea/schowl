package cz.cizlmazna.schowl.ui.subjects.recycler_view

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cz.cizlmazna.schowl.R
import cz.cizlmazna.schowl.database.Subject
import cz.cizlmazna.schowl.ui.subjects.SubjectsFragmentDirections
import kotlinx.android.synthetic.main.fragment_subjects.view.*
//TODO darkMode -> fontColor, different drawable resource
//TODO setOnClickListeners
// TODO databinding
class SubjectAdapter : ListAdapter<Subject, SubjectAdapter.SubjectViewHolder>(SubjectDiffCallback()){


    override fun onBindViewHolder(holder: SubjectViewHolder, position :Int){
        val item = getItem(position)
        val res = holder.itemView.context.resources
        //TODO do this in ViewHolder
        holder.btnSubject.text = item.name
        holder.btnSubject.setOnClickListener { view: View ->
            Navigation.findNavController(view).navigate(
                SubjectsFragmentDirections.actionSubjectsFragmentToCategoriesFragment(item.id)
            )
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        return SubjectViewHolder.from(parent)
    }

    class SubjectViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        val btnSubject : Button
        val btnEdit : Button
        val btnRemove : Button
        val btnTest : Button

        companion object {
            fun from(parent: ViewGroup): SubjectViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.fragment_subjects, parent, false)
                return SubjectViewHolder(view)
            }
        }
    }
}

class SubjectDiffCallback : DiffUtil.ItemCallback<Subject>(){
    override fun areItemsTheSame(oldItem: Subject, newItem: Subject): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Subject, newItem: Subject): Boolean {
        return oldItem == newItem
    }

}

