package ru.fefu.wsr_connect_mobile.messenger

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.fefu.wsr_connect_mobile.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentMessengerBinding
import ru.fefu.wsr_connect_mobile.messenger.chat_list.ChatListAdapter
import ru.fefu.wsr_connect_mobile.messenger.chat_list.ItemRepository


class MessengerFragment : BaseFragment<FragmentMessengerBinding>(R.layout.fragment_messenger){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            searchUsersBtn.setOnClickListener {
                findNavController().navigate(R.id.action_messengerFragment_to_newChatSearchFragment)
            }
            val list = ItemRepository().getChatList()

            if (list.isEmpty()){
                emptyShapeContainer.visibility = View.VISIBLE
            }
            else {
                recycler.layoutManager = LinearLayoutManager(requireActivity())
                recycler.adapter = ChatListAdapter({}, list)
            }
        }
    }
}