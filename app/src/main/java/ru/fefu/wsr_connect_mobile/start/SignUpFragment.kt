package ru.fefu.wsr_connect_mobile.start

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.common.App
import ru.fefu.wsr_connect_mobile.common.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentSignUpBinding
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted
import ru.fefu.wsr_connect_mobile.remote.SocketHandler
import ru.fefu.wsr_connect_mobile.start.view_models.SignUpViewModel


class SignUpFragment : BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {

    private val viewModel by lazy {
        ViewModelProvider(this)[SignUpViewModel::class.java]
    }

    private fun offErrors() {
        binding.apply {
            usernameInput.isErrorEnabled = false
            firstNameInput.isErrorEnabled = false
            lastNameInput.isErrorEnabled = false
            emailInput.isErrorEnabled = false
            passwordInput.isErrorEnabled = false
            passwordInput.isErrorEnabled = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.result
            .onEach {
                if (it) {
                    App.sharedPreferences.edit().putBoolean("have_company", false).apply()
                    val result = findNavController().popBackStack(R.id.nav_graph_auth, true)
                    if (result.not())
                        findNavController().navigate(R.id.navBottomFragment)
                    SocketHandler.setSocket()
                    SocketHandler.establishConnection()
                    SocketHandler.mSocket.emit(
                        "authorization",
                        App.sharedPreferences.getString("token", "")
                    )
                }
            }
            .launchWhenStarted(lifecycleScope)

        viewModel.showUsernameError
            .onEach {
                offErrors()
                binding.usernameInput.error = it
            }
            .launchWhenStarted(lifecycleScope)

        viewModel.showFirstNameError
            .onEach {
                offErrors()
                binding.firstNameInput.error = it
            }
            .launchWhenStarted(lifecycleScope)

        viewModel.showLastNameError
            .onEach {
                offErrors()
                binding.lastNameInput.error = it
            }
            .launchWhenStarted(lifecycleScope)

        viewModel.showEmailError
            .onEach {
                offErrors()
                binding.emailInput.error = it
            }
            .launchWhenStarted(lifecycleScope)

        viewModel.showPasswordError
            .onEach {
                offErrors()
                binding.passwordInput.error = it
            }
            .launchWhenStarted(lifecycleScope)

        viewModel.showPasswordConfirmError
            .onEach {
                offErrors()
                binding.confirmPasswordInput.error = it
            }
            .launchWhenStarted(lifecycleScope)

        binding.apply {
            cancelBtn.setOnClickListener {
                findNavController().popBackStack()
            }
            signUpBtn.setOnClickListener {
                viewModel.signUpClicked(
                    etUsername.text.toString(),
                    etFirstName.text.toString(),
                    etLastName.text.toString(),
                    etEmail.text.toString(),
                    etPassword.text.toString(),
                    etPasswordConfirm.text.toString()
                )
            }
            etUsername.addTextChangedListener { usernameInput.isErrorEnabled = false }
            etFirstName.addTextChangedListener { firstNameInput.isErrorEnabled = false }
            etLastName.addTextChangedListener { lastNameInput.isErrorEnabled = false }
            etEmail.addTextChangedListener { emailInput.isErrorEnabled = false }
            etPassword.addTextChangedListener { passwordInput.isErrorEnabled = false }
            etPasswordConfirm.addTextChangedListener { passwordInput.isErrorEnabled = false }

            etPasswordConfirm.setOnKeyListener { view, i, keyEvent ->
                if (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                    viewModel.signUpClicked(
                        etUsername.text.toString(),
                        etFirstName.text.toString(),
                        etLastName.text.toString(),
                        etEmail.text.toString(),
                        etPassword.text.toString(),
                        etPasswordConfirm.text.toString()
                    )
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }
    }
}
