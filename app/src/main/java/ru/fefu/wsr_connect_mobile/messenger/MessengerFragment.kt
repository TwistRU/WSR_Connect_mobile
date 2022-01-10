package ru.fefu.wsr_connect_mobile.messenger

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentMessengerBinding
import ru.fefu.wsr_connect_mobile.launchWhenStarted
import ru.fefu.wsr_connect_mobile.adapters.ChatListAdapter
import ru.fefu.wsr_connect_mobile.messenger.new_models.MessengerViewModel


class MessengerFragment : BaseFragment<FragmentMessengerBinding>(R.layout.fragment_messenger) {

    private val viewModel by lazy {
        ViewModelProvider(this)[MessengerViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.chats
            .onEach {
                binding.apply {
                    if (it.isEmpty()) {
                        emptyShapeContainer.visibility = View.VISIBLE
                    } else {
                        recycler.layoutManager = LinearLayoutManager(requireActivity())
                        recycler.adapter = ChatListAdapter(
                            { findNavController().navigate(R.id.action_messengerFragment_to_inChatFragment) },
                            it
                        )
                    }
                }
            }
            .launchWhenStarted(lifecycleScope)

        binding.searchUsersBtn.setOnClickListener {
            findNavController().navigate(R.id.action_messengerFragment_to_newChatSearchFragment)
        }

        viewModel.getChats()
    }
}