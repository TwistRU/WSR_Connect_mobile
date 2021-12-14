package ru.fefu.wsr_connect_mobile

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ru.fefu.wsr_connect_mobile.databinding.FragmentNavBottomBinding


class NavBottomFragment : BaseFragment<FragmentNavBottomBinding>(R.layout.fragment_nav_bottom) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavView = binding.bottomNavView
        val navController =
            (childFragmentManager.findFragmentById(R.id.bottomNavContainer) as NavHostFragment).navController
        bottomNavView.setupWithNavController(navController)
    }
}