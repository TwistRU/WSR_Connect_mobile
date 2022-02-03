package ru.fefu.wsr_connect_mobile.dialogs

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentDeleteGroupChatBinding
import ru.fefu.wsr_connect_mobile.dialogs.view_models.DeleteGroupChatViewModel
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted


class DeleteGroupChatFragment : DialogFragment(R.layout.fragment_delete_group_chat) {

    private val viewModel by lazy {
        ViewModelProvider(this)[DeleteGroupChatViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDeleteGroupChatBinding.bind(view)
        val chatId = requireArguments().getInt("chat_id")

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.success
            .onEach {
                if (it) {
                    setFragmentResult("resultDialog", bundleOf("successfulDialog" to true))
                    findNavController().popBackStack()
                }
            }
            .launchWhenStarted(lifecycleScope)

        binding.apply {
            deleteChatBtn.setOnClickListener {
                viewModel.deleteGroupChat(chatId)

            }
            quitChatBtn.setOnClickListener {
                viewModel.quitChat(chatId)

            }
        }
    }
}