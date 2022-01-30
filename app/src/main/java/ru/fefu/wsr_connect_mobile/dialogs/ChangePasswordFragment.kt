package ru.fefu.wsr_connect_mobile.dialogs

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentChangePasswordBinding
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted
import ru.fefu.wsr_connect_mobile.dialogs.view_models.ChangePasswordViewModel


class ChangePasswordFragment : DialogFragment(R.layout.fragment_change_password) {

    private val viewModel by lazy {
        ViewModelProvider(this)[ChangePasswordViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentChangePasswordBinding.bind(view)

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.showCurrentPasswordError
            .onEach { binding.currentPasswordInput.error = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.showNewPasswordError
            .onEach { binding.newPasswordInput.error = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.showNewPasswordConfirmError
            .onEach { binding.newPasswordConfirmInput.error = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.success
            .onEach { if (it) findNavController().popBackStack() }
            .launchWhenStarted(lifecycleScope)

        binding.apply {
            changePasswordBtn.setOnClickListener {
                viewModel.changePassword(
                    etCurrentPassword.text.toString(),
                    etNewPassword.text.toString(),
                    etNewPasswordConfirm.text.toString(),
                )
            }
            etCurrentPassword.addTextChangedListener {
                currentPasswordInput.isErrorEnabled = false
            }
            etNewPassword.addTextChangedListener {
                newPasswordInput.isErrorEnabled = false
            }
            etNewPasswordConfirm.addTextChangedListener {
                newPasswordConfirmInput.isErrorEnabled = false
            }
        }
    }
}