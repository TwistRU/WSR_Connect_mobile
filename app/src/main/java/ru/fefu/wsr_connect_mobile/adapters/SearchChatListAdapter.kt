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
import ru.fefu.wsr_connect_mobile.remote.models.Chat
import android.text.SpannableString
import android.widget.TextView
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import androidx.compose.ui.graphics.Color
import ru.fefu.wsr_connect_mobile.databinding.ItemMessengerChatSearchBinding


class SearchChatListAdapter(
    private val clickListener: (Chat) -> Unit,
) : ListAdapter<Chat, RecyclerView.ViewHolder>(ItemCallback()) {

    public var search: String? = null
    var searchLast: String? = null

    inner class ChatListHolder(item: View) : RecyclerView.ViewHolder(item) {
        private var binding = ItemMessengerChatSearchBinding.bind(item)

        init {
            binding.apply {
                binding.item.setOnClickListener {
                    val position = adapterPosition
                    if (position in currentList.indices) {
                        clickListener.invoke(currentList[position])
                    }
                }
            }
        }

        fun bind(item: Chat) {
            binding.apply {
                chatName.text = item.chatName
                lastMessage.text = item.lastMessage?.messageBody
                if (item.pin) pinned.visibility = View.VISIBLE
                if (item.mute) muted.visibility = View.VISIBLE

                val url = "$BASE_URL${item.imgUrl}"
                val imgView = binding.chatImg
                Glide.with(itemView).load(url).error(R.drawable.ic_profile).into(imgView)

                if (search != null) {
                    setHighLightedText(lastMessage, search!!)
                }
            }
        }
    }

    private class ItemCallback : DiffUtil.ItemCallback<Chat>() {
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean =
            oldItem.chatId == newItem.chatId

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_messenger_chat, parent, false)
        return ChatListHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChatListHolder -> {
                val item = currentList[position]
                holder.bind(item)
            }
        }
    }

    fun setHighLightedText(tv: TextView, textToHighlight: String) {
        val tvt = tv.text.toString()
        var ofe = tvt.indexOf(textToHighlight, 0)
        val wordToSpan: Spannable = SpannableString(tv.text)
        var ofs = 0
        while (ofs < tvt.length && ofe != -1) {
            ofe = tvt.indexOf(textToHighlight, ofs)
            if (ofe == -1) break else {
                // set color here
                wordToSpan.setSpan(
                    ForegroundColorSpan(Color.Red.hashCode()),
                    ofe,
                    ofe + textToHighlight.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                tv.setText(wordToSpan, TextView.BufferType.SPANNABLE)
            }
            ofs = ofe + 1
        }
    }
}
