package ru.fefu.wsr_connect_mobile.start

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.navigation.fragment.findNavController
import ru.fefu.wsr_connect_mobile.App
import ru.fefu.wsr_connect_mobile.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentSplashBinding


class SplashFragment : BaseFragment<FragmentSplashBinding>(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        binding.apply {
            topLogo.visibility = View.GONE
            startLogo.visibility = View.VISIBLE
        }

        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        if (App.sharedPreferences.contains("token")) {
            findNavController().navigate(R.id.action_splashFragment_to_navBottomFragment)
        } else {
            findNavController().navigate(R.id.action_splashFragment_to_signInFragment)
        }
    }
}