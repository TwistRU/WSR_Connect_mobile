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
import ru.fefu.wsr_connect_mobile.databinding.FragmentDeleteAlertBinding
import ru.fefu.wsr_connect_mobile.dialogs.view_models.DeleteUserChatViewModel
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted


class DeleteUserChatFragment : DialogFragment(R.layout.fragment_delete_alert) {

    private val viewModel by lazy {
        ViewModelProvider(this)[DeleteUserChatViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDeleteAlertBinding.bind(view)

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
            deleteBtn.setOnClickListener {
                viewModel.deleteUserFromGroupChat(
                    requireArguments().getInt("chat_id"),
                    requireArguments().getInt("user_id")
                )
            }
        }
    }
}