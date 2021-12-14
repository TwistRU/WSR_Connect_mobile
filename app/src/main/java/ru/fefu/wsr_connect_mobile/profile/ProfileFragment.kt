package ru.fefu.wsr_connect_mobile.profile

import android.os.Bundle
import android.view.View
import androidx.compose.ui.graphics.Color
import androidx.navigation.fragment.findNavController
import ru.fefu.wsr_connect_mobile.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentProfileBinding


class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            avatar.setImageResource(R.drawable.ava_example3)
            changePasswordBtn.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_changePasswordFragment)
            }
            changeAvatarBtn.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_changeAvatarFragment)
            }

            currentEmail.text = "example@mail.com"
            currentNickname.text = "Nickname"
        }
    }
}