package cz.cizlmazna.schowl.ui.subjects.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cz.cizlmazna.schowl.database.Subject
import cz.cizlmazna.schowl.databinding.SubjectItemBinding

//TODO darkMode -> fontColor, different drawable resource
//TODO setOnClickListeners
// TODO databinding
class SubjectAdapter : ListAdapter<Subject, SubjectAdapter.SubjectViewHolder>(SubjectDiffCallback()){


    override fun onBindViewHolder(holder: SubjectViewHolder, position :Int){
        val item = getItem(position)
        val res = holder.itemView.context.resources
        //TODO do this in ViewHolder
       holder.btnSubject.text = item.name
       // holder.btnSubject.setOnClickListener { view: View ->
         //   Navigation.findNavController(view).navigate(
        //      SubjectsFragmentDirections.actionSubjectsFragmentToCategoriesFragment(item.id)
          //  )
        //}


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        return SubjectViewHolder.from(parent)
    }

    class SubjectViewHolder private constructor(val binding: SubjectItemBinding) : RecyclerView.ViewHolder(binding.root){
        val btnSubject : Button = binding.btnNewSubject
        val btnEdit : ImageButton = binding.btnEdit
        val btnRemove : ImageButton = binding.btnRemove
        val btnTest : ImageButton = binding.btnTest

        companion object {
            fun from(parent: ViewGroup): SubjectViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SubjectItemBinding.inflate(layoutInflater,parent,false)
                return SubjectViewHolder(binding)
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

