package ru.fefu.wsr_connect_mobile.messenger

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.common.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.adapters.DeleteUsersListAdapter
import ru.fefu.wsr_connect_mobile.common.App
import ru.fefu.wsr_connect_mobile.databinding.FragmentChatUsersBinding
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted
import ru.fefu.wsr_connect_mobile.messenger.view_models.ChatUsersViewModel


class ChatUsersFragment :
    BaseFragment<FragmentChatUsersBinding>(R.layout.fragment_chat_users) {

    private lateinit var adapter: DeleteUsersListAdapter

    private val viewModel by lazy {
        ViewModelProvider(this)[ChatUsersViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chatId = requireArguments().getInt("chat_id")
        val mine = requireArguments().getBoolean("mine")
        val myId = App.sharedPreferences.getInt("my_id", -1)


        setFragmentResultListener("resultDialog") { requestKey, bundle ->
            if (bundle.getBoolean("successfulDialog")) viewModel.getChatInfo(chatId)
        }

        adapter = DeleteUsersListAdapter(
            myId,
            {
                findNavController().navigate(
                    R.id.action_chatUsersFragment_to_companyUserFragment4,
                    bundleOf("user_id" to it.userId)
                )
            },
            {
                findNavController().navigate(
                    R.id.action_chatUsersFragment_to_deleteUserChatFragment,
                    bundleOf("chat_id" to chatId, "user_id" to it.userId)
                )
            },
            {
                findNavController().navigate(
                    R.id.action_chatUsersFragment_to_quitChatFragment2,
                    bundleOf("chat_id" to chatId)
                )
            }
        )

        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            addBtn.setOnClickListener {
                findNavController().navigate(
                    R.id.action_chatUsersFragment_to_searchUserForGroupChatFragment,
                    bundleOf("chat_id" to chatId)
                )
            }
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(requireActivity())
            registerForContextMenu(recycler)

            if (mine) {
                addBtn.visibility = View.VISIBLE
                adapter.activeDelete = true
            }
        }

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.chatUsers
            .onEach {
                adapter.submitList(it.users)
                binding.toolbar.title = it.chatName
            }
            .launchWhenStarted(lifecycleScope)

        viewModel.getChatInfo(chatId)
    }
}