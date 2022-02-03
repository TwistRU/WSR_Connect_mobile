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
import ru.fefu.wsr_connect_mobile.databinding.ItemTasksUserDeleteBinding
import ru.fefu.wsr_connect_mobile.remote.models.User


class DeleteUsersListAdapter(
    private val myId: Int,
    private val clickListenerUserInfo: (User) -> Unit,
    private val clickListenerUserDelete: (User) -> Unit,
    private val clickListenerQuit: (User) -> Unit,
) : ListAdapter<User, RecyclerView.ViewHolder>(ItemCallback()) {

    var activeDelete = false

    inner class UserListHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var binding = ItemTasksUserDeleteBinding.bind(item)

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
                deleteUserBtn.setOnClickListener {
                    val position = adapterPosition
                    if (position in currentList.indices) {
                        clickListenerUserDelete.invoke(currentList[position])
                    }
                }
                quitBtn.setOnClickListener {
                    val position = adapterPosition
                    if (position in currentList.indices) {
                        clickListenerQuit.invoke(currentList[position])
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
                if (activeDelete) {
                    deleteUserBtn.visibility = View.VISIBLE
                }
                if (item.userId == myId) {
                    deleteUserBtn.visibility = View.GONE
                    quitBtn.visibility = View.VISIBLE
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
            .inflate(R.layout.item_tasks_user_delete, parent, false)
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
