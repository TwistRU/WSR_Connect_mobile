package ru.fefu.wsr_connect_mobile.tasks

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentSendInvitationBinding
import ru.fefu.wsr_connect_mobile.launchWhenStarted
import ru.fefu.wsr_connect_mobile.tasks.view_models.SendInvitationViewModel


class SendInvitationFragment : DialogFragment(R.layout.fragment_send_invitation) {

    private val viewModel by lazy {
        ViewModelProvider(this)[SendInvitationViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSendInvitationBinding.bind(view)

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.showNicknameError
            .onEach { binding.userNicknameInput.error = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.showInviteTextError
            .onEach { binding.inviteTextInput.error = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.success
            .onEach { if (it) findNavController().popBackStack() }
            .launchWhenStarted(lifecycleScope)

        binding.apply {
            sendBtn.setOnClickListener {
                val context = requireArguments().getString("context")
                val boardId = if (context == "company") {
                    null
                } else {
                    requireArguments().getInt("board_id")
                }
                if (context != null)
                    viewModel.sendInvite(
                        context,
                        boardId,
                        etUserNickname.text.toString(),
                        etInviteText.text.toString()
                    )
            }
            etUserNickname.addTextChangedListener {
                userNicknameInput.isErrorEnabled = false
            }
            etInviteText.addTextChangedListener {
                inviteTextInput.isErrorEnabled = false
            }
        }
    }
}