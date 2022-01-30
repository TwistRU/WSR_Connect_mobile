package ru.fefu.wsr_connect_mobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.*
import ru.fefu.wsr_connect_mobile.extensions.formatTo
import ru.fefu.wsr_connect_mobile.extensions.toDate
import ru.fefu.wsr_connect_mobile.remote.models.Card


class CardListAdapter(
    private val clickListenerCardCreator: (Card) -> Unit,
    private val clickListenerCardDetail: (Card) -> Unit,
) : ListAdapter<Card, RecyclerView.ViewHolder>(ItemCallback()) {

    inner class CardListHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var binding = ItemTasksCardBinding.bind(item)

        init {
            binding.cardCreator.setOnClickListener {
                val position = adapterPosition
                if (position in currentList.indices) {
                    clickListenerCardCreator.invoke(currentList[position])
                }
            }

            binding.item.setOnClickListener {
                val position = adapterPosition
                if (position in currentList.indices) {
                    clickListenerCardDetail.invoke(currentList[position])
                }
            }
        }

        fun bind(item: Card) {
            binding.apply {
                cardTitle.text = item.cardTitle
                cardCreator.text = item.cardCreator
                cardCreateDate.text = item.createDate.toDate().formatTo("dd MMM yyyy")
                cardShortDesc.text = item.cardShortDesc
                cardDeadline.text =
                    item.deadline?.let { s: String -> s.toDate().formatTo("dd MMM yyyy") }
            }
        }
    }

    private class ItemCallback : DiffUtil.ItemCallback<Card>() {
        override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean =
            oldItem.cardId == newItem.cardId

        override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tasks_card, parent, false)
        return CardListHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CardListHolder -> {
                val item = currentList[position]
                holder.bind(item)
            }
        }
    }
}