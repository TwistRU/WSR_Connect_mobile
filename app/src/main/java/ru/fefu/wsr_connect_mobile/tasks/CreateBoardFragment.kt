package ru.fefu.wsr_connect_mobile.tasks

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentCreateBoardBinding
import ru.fefu.wsr_connect_mobile.launchWhenStarted
import ru.fefu.wsr_connect_mobile.tasks.view_models.CreateBoardViewModel


class CreateBoardFragment : DialogFragment(R.layout.fragment_create_board) {

    private val viewModel by lazy {
        ViewModelProvider(this)[CreateBoardViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentCreateBoardBinding.bind(view)

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.showFieldError
            .onEach { binding.boardNameInput.error = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.success
            .onEach { if (it) findNavController().popBackStack() }
            .launchWhenStarted(lifecycleScope)

        binding.apply {
            createBoardBtn.setOnClickListener {
                viewModel.createBoard(
                    etBoardName.text.toString(),
                )
            }
            etBoardName.addTextChangedListener {
                boardNameInput.isErrorEnabled = false
            }
        }
    }
}