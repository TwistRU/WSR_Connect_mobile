package ru.fefu.wsr_connect_mobile.dialogs

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.common.App
import ru.fefu.wsr_connect_mobile.databinding.FragmentQuitAlertBinding
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted
import ru.fefu.wsr_connect_mobile.dialogs.view_models.QuitCompanyViewModel


class QuitCompanyFragment : DialogFragment(R.layout.fragment_quit_alert) {

    private val viewModel by lazy {
        ViewModelProvider(this)[QuitCompanyViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentQuitAlertBinding.bind(view)
        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.success
            .onEach {
                if (it) {
                    findNavController().navigate(R.id.action_quitCompanyFragment_to_tasksEmptyFragment)
                    App.sharedPreferences.edit().remove("have_company").apply()
                }
            }
            .launchWhenStarted(lifecycleScope)

        binding.quiteBtn.setOnClickListener {
            viewModel.quitCompany()
        }
    }
}