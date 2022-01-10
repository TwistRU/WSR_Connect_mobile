package ru.fefu.wsr_connect_mobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.ItemTasksUserBinding
import ru.fefu.wsr_connect_mobile.remote.models.CompanyUser


class CompanyUsersListAdapter(
    private val clickListener: (CompanyUser) -> Unit,
    private val list: List<CompanyUser>
) : ListAdapter<CompanyUser, RecyclerView.ViewHolder>(ItemCallback()) {


    inner class UserListHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var binding = ItemTasksUserBinding.bind(item)

        init {
            binding.deleteUserBtn.setOnClickListener {
                val position = adapterPosition
                if (position in list.indices) {
                    clickListener.invoke(list[position])
                }
            }
        }

        fun bind(item: CompanyUser) {
            binding.apply {
                val name = "${item.userFirstName} ${item.userLastName}"
                userFirstAndLastName.text = name
                userEmail.text = item.userEmail
            }
        }
    }

    private class ItemCallback : DiffUtil.ItemCallback<CompanyUser>() {
        override fun areItemsTheSame(oldItem: CompanyUser, newItem: CompanyUser): Boolean =
            oldItem.userId == newItem.userId

        override fun areContentsTheSame(oldItem: CompanyUser, newItem: CompanyUser): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tasks_user, parent, false)
        return UserListHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UserListHolder -> {
                val item = list[position]
                holder.bind(item)
            }
        }
    }

    override fun getItemCount(): Int = list.size
}
