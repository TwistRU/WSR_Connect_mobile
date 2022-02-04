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
import ru.fefu.wsr_connect_mobile.common.App
import ru.fefu.wsr_connect_mobile.databinding.ItemTasksUserAddBinding
import ru.fefu.wsr_connect_mobile.remote.models.User


class AddUsersListAdapter(
    private val clickListenerUserInfo: (User) -> Unit,
    private val clickListenerUserAdd: (User) -> Unit,
) : ListAdapter<User, RecyclerView.ViewHolder>(ItemCallback()) {

    var myId = App.sharedPreferences.getInt("my_id", -1)


    inner class UserListHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var binding = ItemTasksUserAddBinding.bind(item)

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
                addUserBtn.setOnClickListener {
                    val position = adapterPosition
                    if (position in currentList.indices) {
                        clickListenerUserAdd.invoke(currentList[position])
                    }
                }
            }
        }

        fun bind(item: User) {
            binding.apply {
                if (myId == item.userId) {
                    binding.item.visibility = View.GONE
                    return
                }
                val initials = "${item.firstName} ${item.lastName}"
                val contacts = "${item.email} @${item.username}"
                userFirstAndLastName.text = initials
                userEmailAndUsername.text = contacts
                val url = "$BASE_URL${item.imgUrl}"
                val imgView = binding.userImg
                Glide.with(itemView).load(url).error(R.drawable.ic_profile)
                    .into(imgView)
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
            .inflate(R.layout.item_tasks_user_add, parent, false)
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
