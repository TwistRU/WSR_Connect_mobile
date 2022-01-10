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
import ru.fefu.wsr_connect_mobile.databinding.FragmentEditCardBinding
import ru.fefu.wsr_connect_mobile.launchWhenStarted
import ru.fefu.wsr_connect_mobile.tasks.view_models.EditCardViewModel


class EditCardFragment : DialogFragment(R.layout.fragment_edit_card) {

    private val viewModel by lazy {
        ViewModelProvider(this)[EditCardViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentEditCardBinding.bind(view)
        binding.apply {
            etCardTitle.setText(requireArguments().getString("card_title"))
            etCardShortDesc.setText(requireArguments().getString("card_short_desc"))
            etCardLongDesc.setText(requireArguments().getString("card_long_desc"))
        }

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.showTitleError
            .onEach { binding.cardTitleInput.error = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.showShortDescError
            .onEach { binding.cardShortDescInput.error = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.success
            .onEach { if (it) findNavController().popBackStack() }
            .launchWhenStarted(lifecycleScope)

        binding.apply {
            editCardBtn.setOnClickListener {
                viewModel.editCard(
                    requireArguments().getInt("board_id"),
                    requireArguments().getInt("column_id"),
                    requireArguments().getInt("card_id"),
                    etCardTitle.text.toString(),
                    etCardShortDesc.text.toString(),
                    etCardLongDesc.text.toString()
                )
            }
            etCardTitle.addTextChangedListener {
                cardTitleInput.isErrorEnabled = false
            }
            etCardShortDesc.addTextChangedListener {
                cardShortDescInput.isErrorEnabled = false
            }
        }
    }
}