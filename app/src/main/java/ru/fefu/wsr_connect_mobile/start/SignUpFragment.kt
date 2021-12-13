package ru.fefu.wsr_connect_mobile.start

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import ru.fefu.wsr_connect_mobile.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentSignUpBinding


class SignUpFragment : BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.apply {
            cancelBtn.setOnClickListener {
                findNavController().popBackStack()
            }
            signUpBtn.setOnClickListener {
                findNavController().navigate(R.id.action_signUpFragment_to_navBottomFragment)
            }
        }
    }
}
