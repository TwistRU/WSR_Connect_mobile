package ru.fefu.wsr_connect_mobile.dialogs

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentEditColumnBinding
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted
import ru.fefu.wsr_connect_mobile.dialogs.view_models.EditColumnViewModel


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
            .onEach {
                if (it) {
                    setFragmentResult("resultDialog", bundleOf("successfulDialog" to true))
                    findNavController().popBackStack()
                }
            }
            .launchWhenStarted(lifecycleScope)

        binding.apply {
            editColumnBtn.setOnClickListener {
                viewModel.editColumn(
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