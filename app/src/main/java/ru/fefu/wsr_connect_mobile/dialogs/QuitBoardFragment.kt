package ru.fefu.wsr_connect_mobile.dialogs

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentQuitAlertBinding
import ru.fefu.wsr_connect_mobile.dialogs.view_models.QuitBoardViewModel
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted


class QuitBoardFragment : DialogFragment(R.layout.fragment_quit_alert) {

    private val viewModel by lazy {
        ViewModelProvider(this)[QuitBoardViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentQuitAlertBinding.bind(view)
        val boardId = requireArguments().getInt("board_id")
        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.success
            .onEach {
                if (it) {
                    findNavController().navigate(R.id.action_quitBoardFragment_to_companyFragment)
                }
            }
            .launchWhenStarted(lifecycleScope)

        binding.quiteBtn.setOnClickListener {
            viewModel.quitBoard(boardId)
        }
    }
}