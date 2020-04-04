package cz.cizlmazna.schowl.ui.questions

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cz.cizlmazna.schowl.database.Category
import cz.cizlmazna.schowl.database.Question
import cz.cizlmazna.schowl.databinding.QuestionItemBinding
import cz.cizlmazna.schowl.databinding.SubjectItemBinding

class QuestionAdapter : ListAdapter<Question, QuestionAdapter.QuestionViewHolder>(
    QuestionDiffCallback()
){


    override fun onBindViewHolder(holder: QuestionViewHolder, position :Int){
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        return QuestionViewHolder.from(
            parent
        )
    }

    class QuestionViewHolder private constructor(val binding: QuestionItemBinding) : RecyclerView.ViewHolder(binding.root){


        fun bind(item: Question){
            binding.question = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): QuestionViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = QuestionItemBinding.inflate(layoutInflater,parent,false)
                return QuestionViewHolder(
                    binding
                )
            }
        }
    }
}

class QuestionDiffCallback : DiffUtil.ItemCallback<Question>(){
    override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean {
        return oldItem == newItem
    }

}