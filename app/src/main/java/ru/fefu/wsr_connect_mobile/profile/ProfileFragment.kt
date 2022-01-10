package ru.fefu.wsr_connect_mobile.profile

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
import ru.fefu.wsr_connect_mobile.databinding.FragmentProfileBinding
import ru.fefu.wsr_connect_mobile.launchWhenStarted
import ru.fefu.wsr_connect_mobile.profile.new_models.ProfileViewModel


class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    private val viewModel by lazy {
        ViewModelProvider(this)[ProfileViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.showFirstNameResult
            .onEach { if (it != "success") binding.firstNameInput.error = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.showLastNameResult
            .onEach { if (it != "success") binding.lastNameInput.error = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.showEmailResult
            .onEach { if (it != "success") binding.emailInput.error = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.showAboutMeResult
            .onEach {
                if (it == "success") {
                }
            }
            .launchWhenStarted(lifecycleScope)

        viewModel.userInfo
            .onEach {
                binding.apply {
                    currentNickname.text = it.username
                    currentEmail.text = it.email
                    etFirstName.setText(it.firstName)
                    etLastName.setText(it.lastName)
                    etAboutMe.setText(it.aboutMe)
                    etEmail.setText(it.email)
                    etAboutMe.setText(it.aboutMe)
                }
            }
            .launchWhenStarted(lifecycleScope)

        binding.apply {
            avatar.setImageResource(R.drawable.ava_example3)
            changePasswordBtn.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_changePasswordFragment)
            }
            changeAvatarBtn.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_changeAvatarFragment)
            }

            firstNameInput.setEndIconOnClickListener {
                viewModel.changeFirstName(etFirstName.text.toString())
            }
            lastNameInput.setEndIconOnClickListener {
                viewModel.changeLastName(etLastName.text.toString())
            }
            emailInput.setEndIconOnClickListener {
                viewModel.changeEmail(etEmail.text.toString())
            }
            aboutMeInput.setEndIconOnClickListener {
                viewModel.changeAboutMe(etAboutMe.text.toString())
            }

            etFirstName.addTextChangedListener {
                firstNameInput.isErrorEnabled = false
            }
            etLastName.addTextChangedListener {
                lastNameInput.isErrorEnabled = false
            }
            etEmail.addTextChangedListener {
                emailInput.isErrorEnabled = false
            }
            etAboutMe.addTextChangedListener {
                aboutMeInput.isErrorEnabled = false
            }

            logoutBtn.setOnClickListener {
                App.sharedPreferences.edit().clear().apply()
                //выход на экран логина
            }
        }
    }
}