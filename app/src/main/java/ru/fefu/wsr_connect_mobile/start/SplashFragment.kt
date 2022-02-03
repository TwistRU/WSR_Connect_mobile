package ru.fefu.wsr_connect_mobile.start

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.common.App
import ru.fefu.wsr_connect_mobile.common.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentSplashBinding
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted
import ru.fefu.wsr_connect_mobile.remote.SocketHandler
import ru.fefu.wsr_connect_mobile.start.view_models.SplashViewModel


class SplashFragment : BaseFragment<FragmentSplashBinding>(R.layout.fragment_splash) {

    private val viewModel by lazy {
        ViewModelProvider(this)[SplashViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            topLogo.visibility = View.GONE
            startLogo.visibility = View.VISIBLE
        }

        val navController =
            Navigation.findNavController(requireActivity(), R.id.rootActivityContainer)
        val mainGraph = navController.navInflater.inflate(R.navigation.nav_graph_start)
        mainGraph.setStartDestination(R.id.nav_graph_auth)

        if (App.sharedPreferences.contains("token")) {
            SocketHandler.setSocket()
            SocketHandler.establishConnection()
            SocketHandler.mSocket.emit("authorization", App.sharedPreferences.getString("token", ""))

            viewModel.getProfileInfo()
        } else {
            navController.graph = mainGraph
        }

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.info
            .onEach {
                if (it.companyId != null) {
                    App.sharedPreferences.edit().putBoolean("have_company", true).apply()
                }
                mainGraph.setStartDestination(R.id.navBottomFragment)
                navController.graph = mainGraph
            }
            .launchWhenStarted(lifecycleScope)
    }
}