package ru.fefu.wsr_connect_mobile.start

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentSignUpBinding
import ru.fefu.wsr_connect_mobile.launchWhenStarted
import ru.fefu.wsr_connect_mobile.start.view_models.SignUpViewModel


class SignUpFragment : BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {

    private val viewModel by lazy {
        ViewModelProvider(this)[SignUpViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO ишабки при изменении поля

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.showUsernameError
            .onEach { binding.etUsername.error = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.showEmailError
            .onEach { binding.etEmail.error = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.showPasswordError
            .onEach { binding.etPassword.error = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.showPasswordConfirmError
            .onEach { binding.etPasswordConfirm.error = it }
            .launchWhenStarted(lifecycleScope)

        binding.apply {
            cancelBtn.setOnClickListener {
                findNavController().popBackStack()
            }
            signUpBtn.setOnClickListener {
                viewModel.signUpClicked(
                    etUsername.text.toString(),
                    etEmail.text.toString(),
                    etPassword.text.toString(),
                    etPasswordConfirm.text.toString()
                )
//                findNavController().navigate(R.id.action_signUpFragment_to_navBottomFragment)
            }
        }
    }
}
