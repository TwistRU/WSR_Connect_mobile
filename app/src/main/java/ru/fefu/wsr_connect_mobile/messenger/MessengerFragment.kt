package ru.fefu.wsr_connect_mobile.messenger

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.adapters.ChatListAdapter
import ru.fefu.wsr_connect_mobile.databinding.FragmentMessengerBinding
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted
import ru.fefu.wsr_connect_mobile.messenger.view_models.MessengerViewModel


class MessengerFragment : BaseFragment<FragmentMessengerBinding>(R.layout.fragment_messenger) {

    private lateinit var adapter: ChatListAdapter

    private val viewModel by lazy {
        ViewModelProvider(this)[MessengerViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ChatListAdapter(
            {
                val bundle = Bundle()
                bundle.putInt("chat_id", it.chatId)
                Navigation.findNavController(requireActivity(), R.id.rootActivityContainer)
                    .navigate(R.id.action_navBottomFragment_to_nav_graph_in_chat, bundle)
            },
            { viewModel.muteChat(it.chatId, true) },
            { viewModel.pinChat(it.chatId, true) },
            {
                val bundle = Bundle()
                bundle.putInt("chat_id", it.chatId)
                findNavController().navigate(
                    R.id.action_messengerFragment_to_quitChatFragment,
                    bundle
                )
            }
        )
        binding.apply {
            recycler.layoutManager = LinearLayoutManager(requireActivity())
            recycler.adapter = adapter

            searchChatView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    if (query.isNotEmpty()) viewModel.getChats(query)
                    else viewModel.getChats(null)
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.isEmpty()) viewModel.getChats(null)
                    return false
                }
            })

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getChats(null)//TODO
            }
        }

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.chats
            .onEach {
                binding.apply {
                    if (it.isEmpty()) {
                        emptyShapeContainer.visibility = View.VISIBLE
                        adapter.submitList(it)
                    } else {
                        emptyShapeContainer.visibility = View.GONE
                        adapter.submitList(it)
                    }
                    swipeRefreshLayout.isRefreshing = false
                }
            }
            .launchWhenStarted(lifecycleScope)

        binding.searchUsersBtn.setOnClickListener {
            findNavController().navigate(R.id.action_messengerFragment_to_newChatSearchFragment)
        }

        viewModel.getChats(null)
    }
}