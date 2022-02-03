package ru.fefu.wsr_connect_mobile.common

import android.os.Bundle
import android.view.View
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentNavBottomBinding
import ru.fefu.wsr_connect_mobile.extensions.setupWithNavController


class NavBottomFragment : BaseFragment<FragmentNavBottomBinding>(R.layout.fragment_nav_bottom) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {

        val navGraphIds = listOf(
            R.navigation.nav_graph_profile,
            R.navigation.nav_graph_messenger,
            R.navigation.nav_graph_tasks,
        )
        binding.bottomNavView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = childFragmentManager,
            containerId = R.id.bottomNavContainer,
            intent = requireActivity().intent
        )
    }
}