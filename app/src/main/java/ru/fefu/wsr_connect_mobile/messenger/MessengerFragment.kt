package ru.fefu.wsr_connect_mobile.messenger

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.adapters.ChatListAdapter
import ru.fefu.wsr_connect_mobile.adapters.SearchChatListAdapter
import ru.fefu.wsr_connect_mobile.common.BaseFragment
import ru.fefu.wsr_connect_mobile.databinding.FragmentMessengerBinding
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted
import ru.fefu.wsr_connect_mobile.messenger.view_models.MessengerViewModel


class MessengerFragment : BaseFragment<FragmentMessengerBinding>(R.layout.fragment_messenger) {

    private lateinit var adapter: ChatListAdapter
    private lateinit var adapterSearch: SearchChatListAdapter

    private val viewModel by lazy {
        ViewModelProvider(this)[MessengerViewModel::class.java]
    }

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(this.context, R.anim.rotate_open_anim)
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(this.context, R.anim.rotate_close_anim)
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(this.context, R.anim.from_bottom_anim)
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(this.context, R.anim.to_bottom_anim)
    }
    private val fromRight: Animation by lazy {
        AnimationUtils.loadAnimation(this.context, R.anim.from_right_anim)
    }
    private val toRight: Animation by lazy {
        AnimationUtils.loadAnimation(this.context, R.anim.to_right_anim)
    }
    private var fabClicked = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener("resultDialog") { requestKey, bundle ->
            if (bundle.getBoolean("successfulDialog")) viewModel.getChats()
        }

        adapter = ChatListAdapter(
            {
                Navigation.findNavController(requireActivity(), R.id.rootActivityContainer)
                    .navigate(
                        R.id.action_navBottomFragment_to_nav_graph_in_chat,
                        bundleOf(
                            "chat_id" to it.chatId,
                            "group" to it.group,
                            "mine" to it.mine
                        )
                    )
            },
            {
                findNavController().navigate(
                    R.id.action_messengerFragment_to_editChatFragment,
                    bundleOf("chat_id" to it.chatId, "chat_name" to it.chatName, "img_url" to it.imgUrl)
                )
            },
            { viewModel.muteChat(it.chatId, !it.mute) },
            { viewModel.pinChat(it.chatId, !it.pin) },
            {
                if (it.group && it.mine) {
                    findNavController().navigate(
                        R.id.action_messengerFragment_to_deleteGroupChatFragment,
                        bundleOf("chat_id" to it.chatId)
                    )
                } else {
                    findNavController().navigate(
                        R.id.action_messengerFragment_to_quitChatFragment,
                        bundleOf("chat_id" to it.chatId)
                    )
                }
            }
        )
        adapterSearch = SearchChatListAdapter {
            Navigation.findNavController(requireActivity(), R.id.rootActivityContainer)
                .navigate(
                    R.id.action_navBottomFragment_to_nav_graph_in_chat,
                    bundleOf("chat_id" to it.chatId, "group" to it.group, "mine" to it.mine)
                )
        }
        binding.apply {
            recycler.layoutManager = LinearLayoutManager(requireActivity())
            recycler.adapter = adapter

            recyclerSearch.layoutManager = LinearLayoutManager(requireActivity())
            recyclerSearch.adapter = adapterSearch

            searchChatView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    if (query.isNotEmpty()) {
                        viewModel.searchChat(query)
                        binding.recyclerSearch.visibility = View.VISIBLE
                        binding.recycler.visibility = View.GONE
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    adapterSearch.search = newText
                    if (newText.isEmpty()) {
                        adapterSearch.search = null
                        binding.recyclerSearch.visibility = View.GONE
                        binding.recycler.visibility = View.VISIBLE
                    }
                    return false
                }
            })

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getChats()
                searchChatView.query.none()
            }

            fabBtn.setOnClickListener { onFabButtonClicked() }

            createGroupChatBtn.setOnClickListener {
                onFabButtonClicked()
                findNavController().navigate(R.id.action_messengerFragment_to_createGroupChatFragment)
            }
            searchUsersBtn.setOnClickListener {
                onFabButtonClicked()
                findNavController().navigate(R.id.action_messengerFragment_to_newChatSearchFragment)
            }
        }

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.success
            .onEach { viewModel.getChats() }
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

        viewModel.searchResult
            .onEach {
                adapterSearch.submitList(it)
            }
            .launchWhenStarted(lifecycleScope)

        viewModel.getChats()
    }

    private fun onFabButtonClicked() {
        setVisibility(fabClicked)
        setAnimation(fabClicked)
        setClickable(fabClicked)
        fabClicked = !fabClicked
    }

    private fun setVisibility(fabClicked: Boolean) {
        binding.apply {
            if (!fabClicked) {
                createGroupChatBtn.visibility = View.VISIBLE
                createGroupChatText.visibility = View.VISIBLE
                searchUsersBtn.visibility = View.VISIBLE
                searchUsersText.visibility = View.VISIBLE
            } else {
                createGroupChatBtn.visibility = View.INVISIBLE
                createGroupChatText.visibility = View.INVISIBLE
                searchUsersBtn.visibility = View.INVISIBLE
                searchUsersText.visibility = View.INVISIBLE
            }
        }
    }

    private fun setAnimation(fabClicked: Boolean) {
        binding.apply {
            if (!fabClicked) {
                createGroupChatBtn.startAnimation(fromBottom)
                createGroupChatText.startAnimation(fromRight)
                searchUsersBtn.startAnimation(fromBottom)
                searchUsersText.startAnimation(fromRight)
                fabBtn.startAnimation(rotateOpen)
            } else {
                createGroupChatBtn.startAnimation(toBottom)
                createGroupChatText.startAnimation(toRight)
                searchUsersBtn.startAnimation(toBottom)
                searchUsersText.startAnimation(toRight)
                fabBtn.startAnimation(rotateClose)
            }
        }
    }

    private fun setClickable(fabClicked: Boolean) {
        binding.apply {
            if (!fabClicked) {
                createGroupChatBtn.isClickable = true
                searchUsersBtn.isClickable = true
            } else {
                createGroupChatBtn.isClickable = false
                searchUsersBtn.isClickable = false
            }
        }
    }
}