package cz.cizlmazna.schowl.ui.questions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cz.cizlmazna.schowl.database.Question
import cz.cizlmazna.schowl.databinding.QuestionItemBinding

class QuestionAdapter(private val questionsFragment: QuestionsFragment) :
    ListAdapter<Question, QuestionAdapter.QuestionViewHolder>(
        QuestionDiffCallback()
    ) {


    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        return QuestionViewHolder.from(
            parent, questionsFragment
        )
    }

    class QuestionViewHolder private constructor(
        val binding: QuestionItemBinding,
        private val questionsFragment: QuestionsFragment
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Question) {
            binding.question = item
            binding.questionsFragment = questionsFragment
            binding.btnNewQuestion.setOnClickListener { view: View ->
                Navigation.findNavController(view).navigate(
                    QuestionsFragmentDirections.actionQuestionsFragmentToEditQuestionFragment(
                        item.categoryId,
                        item.id
                    )
                )
            }

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup, questionsFragment: QuestionsFragment): QuestionViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = QuestionItemBinding.inflate(layoutInflater, parent, false)
                return QuestionViewHolder(
                    binding, questionsFragment
                )
            }
        }
    }
}

class QuestionDiffCallback : DiffUtil.ItemCallback<Question>() {
    override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean {
        return oldItem == newItem
    }

}