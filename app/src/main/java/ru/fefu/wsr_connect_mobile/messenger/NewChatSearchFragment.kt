package ru.fefu.wsr_connect_mobile.messenger

import android.os.Bundle
import android.view.View
import ru.fefu.wsr_connect_mobile.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentNewChatSearchBinding


class NewChatSearchFragment :
    BaseFragment<FragmentNewChatSearchBinding>(R.layout.fragment_new_chat_search) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.apply {
//            recycler.layoutManager = LinearLayoutManager(requireActivity())
//            recycler.adapter = ChatListAdapter(
//                { findNavController().navigate(R.id.action_newChatSearchFragment_to_inChatFragment) },
//                ItemRepository().getNewChatList()
//            )
//        }
    }
}