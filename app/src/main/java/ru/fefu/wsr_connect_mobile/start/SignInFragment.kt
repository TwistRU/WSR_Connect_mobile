package ru.fefu.wsr_connect_mobile.start

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.App
import ru.fefu.wsr_connect_mobile.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentSignInBinding
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted
import ru.fefu.wsr_connect_mobile.start.view_models.SignInViewModel


class SignInFragment : BaseFragment<FragmentSignInBinding>(R.layout.fragment_sign_in) {

    private val viewModel by lazy {
        ViewModelProvider(this)[SignInViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.result
            .onEach {
                if (it) {
                    viewModel.getUserInfo()
                }
            }
            .launchWhenStarted(lifecycleScope)

        viewModel.info
            .onEach {
                if (it.companyId != null) {
                    App.sharedPreferences.edit().putBoolean("have_company", true).apply()
                }
                else {
                    App.sharedPreferences.edit().putBoolean("have_company", false).apply()
                }
                val result = findNavController().popBackStack(R.id.nav_graph_auth, true)
                if (result.not())
                    findNavController().navigate(R.id.navBottomFragment)
            }
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
            }
            signUpBtn.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
            }

            etUsername.addTextChangedListener { usernameInput.isErrorEnabled = false }
            etPassword.addTextChangedListener { passwordInput.isErrorEnabled = false }
        }
    }
}
