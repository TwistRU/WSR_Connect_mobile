package ru.fefu.wsr_connect_mobile.start

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import ru.fefu.wsr_connect_mobile.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentSignInBinding


class SignInFragment : BaseFragment<FragmentSignInBinding>(R.layout.fragment_sign_in) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.apply {
            signInBtn.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_navBottomFragment)
            }
            signUpBtn.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
            }
        }
    }
}
