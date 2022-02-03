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
import ru.fefu.wsr_connect_mobile.databinding.FragmentQuitAlertBinding
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted
import ru.fefu.wsr_connect_mobile.dialogs.view_models.QuitChatViewModel


class QuitChatFragment : DialogFragment(R.layout.fragment_quit_alert) {

    private val viewModel by lazy {
        ViewModelProvider(this)[QuitChatViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentQuitAlertBinding.bind(view)
        val chatId = requireArguments().getInt("chat_id")
        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.success
            .onEach {
                if (it) {
                    val result = findNavController().popBackStack(R.id.nav_graph_in_chat, true)
                    if (result.not())
                        findNavController().navigate(R.id.navBottomFragment)
                }
            }
            .launchWhenStarted(lifecycleScope)

        binding.quiteBtn.setOnClickListener {
            viewModel.quitChat(chatId)
        }
    }
}