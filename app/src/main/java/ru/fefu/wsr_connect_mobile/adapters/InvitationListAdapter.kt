package ru.fefu.wsr_connect_mobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.*
import ru.fefu.wsr_connect_mobile.remote.models.Invitation


class InvitationListAdapter(
    private val list: List<Invitation>
) : ListAdapter<Invitation, RecyclerView.ViewHolder>(ItemCallback()) {

    var selectedItemPos = -1
    var lastItemSelectedPos = -1


    inner class InvitationListHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var binding = ItemTasksInvitationBinding.bind(item)

        fun default() {
            binding.radioBtn.isChecked = false
        }

        fun selected() {
            binding.radioBtn.isChecked = true
        }

        init {
            binding.radioBtn.setOnClickListener {
                selectedItemPos = adapterPosition
                if (lastItemSelectedPos == -1)
                    lastItemSelectedPos = selectedItemPos
                else {
                    notifyItemChanged(lastItemSelectedPos)
                    lastItemSelectedPos = selectedItemPos
                }
                notifyItemChanged(selectedItemPos)
            }
        }

        fun bind(item: Invitation) {
            binding.apply {
                companyName.text = item.companyName
                invitationBody.text = item.inviteBody
            }
        }
    }

    private class ItemCallback : DiffUtil.ItemCallback<Invitation>() {
        override fun areItemsTheSame(oldItem: Invitation, newItem: Invitation): Boolean =
            true

        override fun areContentsTheSame(oldItem: Invitation, newItem: Invitation): Boolean =
            true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tasks_invitation, parent, false)
        return InvitationListHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is InvitationListHolder -> {
                if (position == selectedItemPos) {
                    holder.selected()
                } else holder.default()
                holder.bind(list[position])
            }
        }
    }

    override fun getItemCount(): Int = list.size
}