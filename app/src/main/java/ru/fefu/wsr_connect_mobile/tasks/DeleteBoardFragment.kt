package ru.fefu.wsr_connect_mobile.tasks

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentDeleteAlertBinding
import ru.fefu.wsr_connect_mobile.launchWhenStarted
import ru.fefu.wsr_connect_mobile.tasks.view_models.DeleteBoardViewModel


class DeleteBoardFragment : DialogFragment(R.layout.fragment_delete_alert) {

    private val viewModel by lazy {
        ViewModelProvider(this)[DeleteBoardViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDeleteAlertBinding.bind(view)

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.success
            .onEach { if (it) findNavController().popBackStack() }
            .launchWhenStarted(lifecycleScope)

        binding.apply {
            deleteBtn.setOnClickListener {
                viewModel.deleteBoard(
                    requireArguments().getInt("board_id"),
                )
            }
        }
    }
}