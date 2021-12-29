package ru.fefu.wsr_connect_mobile.messenger

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.fefu.wsr_connect_mobile.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentNewChatSearchBinding
import ru.fefu.wsr_connect_mobile.messenger.chat_list.ChatListAdapter
import ru.fefu.wsr_connect_mobile.messenger.chat_list.ItemRepository


class NewChatSearchFragment :
    BaseFragment<FragmentNewChatSearchBinding>(R.layout.fragment_new_chat_search) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            recycler.layoutManager = LinearLayoutManager(requireActivity())
            recycler.adapter = ChatListAdapter(
                { findNavController().navigate(R.id.action_newChatSearchFragment_to_inChatFragment) },
                ItemRepository().getNewChatList()
            )
        }
    }
}