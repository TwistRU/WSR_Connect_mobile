package ru.fefu.wsr_connect_mobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.*
import ru.fefu.wsr_connect_mobile.remote.models.Card


class CardListAdapter(
    private val clickListener: (Card) -> Unit,
    private val optionsMenuClickListener: OptionsMenuClickListener,
    private val columnId: Int,
    private val list: List<Card>
) : ListAdapter<Card, RecyclerView.ViewHolder>(ItemCallback()) {

    interface OptionsMenuClickListener {
        fun onOptionsMenuClicked(
            columnId: Int,
            card: Card,
            view: View
        )
    }

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

        fun bind(item: Card) {
            binding.apply {
                cardTitle.text = item.cardTitle
                cardCreator.text = item.cardCreator
                cardCreateDate.text = item.cardCreateDate
                cardShortDesc.text = item.cardShortDesc
                cardLongDesc.text = item.cardLongDesc
            }

            binding.item.setOnLongClickListener {
                optionsMenuClickListener.onOptionsMenuClicked(
                    columnId, item, it
                )
                return@setOnLongClickListener true
            }
        }
    }

    private class ItemCallback : DiffUtil.ItemCallback<Card>() {
        override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean =
            true

        override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean =
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