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
import ru.fefu.wsr_connect_mobile.databinding.FragmentEditColumnBinding
import ru.fefu.wsr_connect_mobile.launchWhenStarted
import ru.fefu.wsr_connect_mobile.tasks.view_models.EditBoardViewModel
import ru.fefu.wsr_connect_mobile.tasks.view_models.EditColumnViewModel


class EditColumnFragment : DialogFragment(R.layout.fragment_edit_column) {

    private val viewModel by lazy {
        ViewModelProvider(this)[EditColumnViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentEditColumnBinding.bind(view)
        binding.etColumnTitle.setText(requireArguments().getString("column_title"))

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.showFieldError
            .onEach { binding.columnTitleInput.error = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.success
            .onEach { if (it) findNavController().popBackStack() }
            .launchWhenStarted(lifecycleScope)

        binding.apply {
            editColumnBtn.setOnClickListener {
                viewModel.editBoard(
                    requireArguments().getInt("board_id"),
                    requireArguments().getInt("column_id"),
                    etColumnTitle.text.toString(),
                )
            }
            etColumnTitle.addTextChangedListener {
                columnTitleInput.isErrorEnabled = false
            }
        }
    }
}