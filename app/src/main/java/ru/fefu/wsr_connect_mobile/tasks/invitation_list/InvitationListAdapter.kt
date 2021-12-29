package ru.fefu.wsr_connect_mobile.tasks.invitation_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.*


class InvitationListAdapter(
    private val list: List<InvitationItem>
) : ListAdapter<InvitationItem, RecyclerView.ViewHolder>(ItemCallback()) {

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

        fun bind(item: InvitationItem) {
            binding.apply {
                companyName.text = item.companyName
                invitationBody.text = item.invitationBody
            }
        }
    }

    private class ItemCallback : DiffUtil.ItemCallback<InvitationItem>() {
        override fun areItemsTheSame(oldItem: InvitationItem, newItem: InvitationItem): Boolean =
            true

        override fun areContentsTheSame(oldItem: InvitationItem, newItem: InvitationItem): Boolean =
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