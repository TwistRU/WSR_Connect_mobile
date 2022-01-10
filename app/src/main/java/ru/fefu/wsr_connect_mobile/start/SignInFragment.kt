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
import ru.fefu.wsr_connect_mobile.databinding.FragmentSignInBinding
import ru.fefu.wsr_connect_mobile.launchWhenStarted
import ru.fefu.wsr_connect_mobile.start.view_models.SignInViewModel


class SignInFragment : BaseFragment<FragmentSignInBinding>(R.layout.fragment_sign_in) {

    private val viewModel by lazy {
        ViewModelProvider(this)[SignInViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO ишабки при изменении поля

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.showUsernameError
            .onEach { binding.usernameInput.error = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.showPasswordError
            .onEach { binding.passwordInput.error = it }
            .launchWhenStarted(lifecycleScope)

        binding.apply {
            signInBtn.setOnClickListener {
                viewModel.signInClicked(
                    etUsername.text.toString(),
                    etPassword.text.toString(),
                )
//                findNavController().navigate(R.id.action_signInFragment_to_navBottomFragment)
            }
            signUpBtn.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
            }
        }
    }
}
