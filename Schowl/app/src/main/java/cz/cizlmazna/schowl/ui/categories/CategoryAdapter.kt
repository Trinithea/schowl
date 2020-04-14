package cz.cizlmazna.schowl.ui.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cz.cizlmazna.schowl.database.Category
import cz.cizlmazna.schowl.databinding.CategoryItemBinding

class CategoryAdapter(private val categoriesFragment: CategoriesFragment) :
    ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(
        CategoryDiffCallback()
    ) {


    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder.from(
            parent, categoriesFragment
        )
    }

    class CategoryViewHolder private constructor(
        val binding: CategoryItemBinding,
        private val categoriesFragment: CategoriesFragment
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Category) {
            binding.category = item
            binding.btnNewCategory.setOnClickListener { view: View ->
                Navigation.findNavController(view).navigate(
                    CategoriesFragmentDirections.actionCategoriesFragmentToQuestionsFragment(
                        item.id
                    )
                )
            }
            binding.categoriesFragment = categoriesFragment
            binding.btnTest.setOnClickListener { view: View ->
                Navigation.findNavController(view).navigate(
                    CategoriesFragmentDirections.actionCategoriesFragmentToNavTest(
                        item.subjectId,
                        item.id
                    )
                )
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(
                parent: ViewGroup,
                categoriesFragment: CategoriesFragment
            ): CategoryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CategoryItemBinding.inflate(layoutInflater, parent, false)
                return CategoryViewHolder(
                    binding, categoriesFragment
                )
            }
        }
    }
}

class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }

}