package ru.fefu.wsr_connect_mobile.tasks.column_list.card_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.*
import ru.fefu.wsr_connect_mobile.tasks.column_list.CardItem


class CardListAdapter(
    private val clickListener: (CardItem) -> Unit,
    private val list: List<CardItem>
) : ListAdapter<CardItem, RecyclerView.ViewHolder>(ItemCallback()) {


    inner class CardListHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var binding = ItemTasksCardBinding.bind(item)

        init {
            binding.item.setOnClickListener {
                val position = adapterPosition
                if (position in list.indices) {
                    clickListener.invoke(list[position])
                }
                binding.apply {
                    if (!cardLongDescContainer.isVisible) {
                        cardLongDescContainer.visibility = View.VISIBLE
                    } else {
                        cardLongDescContainer.visibility = View.INVISIBLE
                    }
                }
            }
        }

        fun bind(item: CardItem) {
            binding.apply {
                cardTitle.text = item.cardTitle
                cardCreator.text = item.cardCreator
                cardCreateDate.text = item.cardCreateDate
                cardShortDesc.text = item.cardShortDesc
                cardLongDesc.text = item.cardLongDesc
            }
        }
    }

    private class ItemCallback : DiffUtil.ItemCallback<CardItem>() {
        override fun areItemsTheSame(oldItem: CardItem, newItem: CardItem): Boolean =
            true

        override fun areContentsTheSame(oldItem: CardItem, newItem: CardItem): Boolean =
            true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tasks_card, parent, false)
        return CardListHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CardListHolder -> {
                val item = list[position]
                holder.bind(item)
            }
        }
    }

    override fun getItemCount(): Int = list.size
}