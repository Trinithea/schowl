package cz.cizlmazna.schowl.ui.subjects

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cz.cizlmazna.schowl.database.Subject
import cz.cizlmazna.schowl.databinding.SubjectItemBinding

//TODO darkMode -> fontColor, different drawable resource
//TODO setOnClickListeners
// TODO databinding
class SubjectAdapter(private val subjectsFragment: SubjectsFragment) : ListAdapter<Subject, SubjectAdapter.SubjectViewHolder>(
    SubjectDiffCallback()
){


    override fun onBindViewHolder(holder: SubjectViewHolder, position :Int){
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        return SubjectViewHolder.from(
            parent, subjectsFragment
        )
    }

    class SubjectViewHolder private constructor(private val binding: SubjectItemBinding, private val subjectsFragment: SubjectsFragment) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Subject){
            binding.subject = item
            binding.subjectsFragment = subjectsFragment
            binding.btnNewSubject.setOnClickListener { view: View ->
                Navigation.findNavController(view).navigate(
                    SubjectsFragmentDirections.actionSubjectsFragmentToCategoriesFragment(item.id)
                ) }
            binding.btnTest.setOnClickListener { view: View ->
                Navigation.findNavController(view)
                    .navigate(SubjectsFragmentDirections.actionNavSubjectsToNavTest(item.id, -1)) }

            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup, subjectsFragment: SubjectsFragment): SubjectViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SubjectItemBinding.inflate(layoutInflater,parent,false)
                return SubjectViewHolder(
                    binding, subjectsFragment
                )
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

