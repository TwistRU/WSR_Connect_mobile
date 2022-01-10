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
import ru.fefu.wsr_connect_mobile.databinding.FragmentCreateColumnBinding
import ru.fefu.wsr_connect_mobile.launchWhenStarted
import ru.fefu.wsr_connect_mobile.tasks.view_models.CreateColumnViewModel


class CreateColumnFragment : DialogFragment(R.layout.fragment_create_column) {

    private val viewModel by lazy {
        ViewModelProvider(this)[CreateColumnViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentCreateColumnBinding.bind(view)

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.showFieldError
            .onEach { binding.columnNameInput.error = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.success
            .onEach { if (it) findNavController().popBackStack() }
            .launchWhenStarted(lifecycleScope)

        binding.apply {
            createBoardBtn.setOnClickListener {
                viewModel.createColumn(
//                    requireArguments().getInt("board_id")
                    1,
                    etColumnName.text.toString(),
                )
            }
            etColumnName.addTextChangedListener {
                columnNameInput.isErrorEnabled = false
            }
        }
    }
}