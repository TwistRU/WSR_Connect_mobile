package ru.fefu.wsr_connect_mobile.dialogs

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
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted
import ru.fefu.wsr_connect_mobile.dialogs.view_models.SendInvitationViewModel


class SendInvitationFragment : DialogFragment(R.layout.fragment_send_invitation) {

    private val viewModel by lazy {
        ViewModelProvider(this)[SendInvitationViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = requireArguments().getInt("user_id")

        val binding = FragmentSendInvitationBinding.bind(view)

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.showInviteTextError
            .onEach { binding.inviteTextInput.error = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.success
            .onEach { if (it) findNavController().popBackStack() }
            .launchWhenStarted(lifecycleScope)

        binding.apply {
            etUsername.setText(requireArguments().getString("username"))
            sendBtn.setOnClickListener {
                    viewModel.sendCompanyInvite(
                        etUsername.text.toString(),
                        userId,
                        etInviteText.text.toString()
                    )
                }
            etInviteText.addTextChangedListener {
                inviteTextInput.isErrorEnabled = false
            }
        }
    }
}