package ru.fefu.wsr_connect_mobile.messenger

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.fefu.wsr_connect_mobile.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentInChatBinding
import ru.fefu.wsr_connect_mobile.adapters.MessagesListAdapter
import ru.fefu.wsr_connect_mobile.messenger.messages_list.MessagesRepository


class InChatFragment : BaseFragment<FragmentInChatBinding>(R.layout.fragment_in_chat) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
            val linearLayout = LinearLayoutManager(requireActivity())
            linearLayout.stackFromEnd = true
            recycler.layoutManager = linearLayout
            recycler.adapter = MessagesListAdapter(
                {}, MessagesRepository().getMessagesList()
            )
        }
    }
}
