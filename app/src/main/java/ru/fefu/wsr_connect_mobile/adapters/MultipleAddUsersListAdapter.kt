package ru.fefu.wsr_connect_mobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.fefu.wsr_connect_mobile.common.BASE_URL
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.ItemTasksUserAddMultipleBinding
import ru.fefu.wsr_connect_mobile.remote.models.User


class MultipleAddUsersListAdapter(
    private val clickListenerUserInfo: (User) -> Unit,
    private val notifyCounter: (User) -> Unit
) : ListAdapter<User, RecyclerView.ViewHolder>(ItemCallback()) {

    val users = mutableSetOf<Int>()

    inner class UserListHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var binding = ItemTasksUserAddMultipleBinding.bind(item)

        init {
            binding.apply {
                userInfoContainer.setOnClickListener {
                    val position = adapterPosition
                    if (position in currentList.indices) {
                        clickListenerUserInfo.invoke(currentList[position])
                    }
                }
                userImg.setOnClickListener {
                    val position = adapterPosition
                    if (position in currentList.indices) {
                        clickListenerUserInfo.invoke(currentList[position])
                    }
                }
            }
        }

        fun bind(item: User) {
            binding.apply {
                val initials = "${item.firstName} ${item.lastName}"
                val contacts = "${item.email} @${item.username}"
                userFirstAndLastName.text = initials
                userEmailAndUsername.text = contacts
                val url = "$BASE_URL${item.imgUrl}"
                val imgView = binding.userImg
                Glide.with(itemView).load(url).error(R.drawable.ic_profile)
                    .into(imgView)

                binding.checkbox.isChecked = users.contains(item.userId)

                checkbox.setOnClickListener {
                    val position = adapterPosition
                    if (position in currentList.indices) {
                        val userId = currentList[position].userId
                        if (users.contains(userId)) {
                            users.remove(userId)
                        } else {
                            users.add(userId)
                        }
                        notifyCounter.invoke(currentList[position])
                    }
                }
            }
        }
    }

    private class ItemCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
            oldItem.userId == newItem.userId

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tasks_user_add_multiple, parent, false)
        return UserListHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UserListHolder -> {
                val item = currentList[position]
                holder.bind(item)
            }
        }
    }
}
