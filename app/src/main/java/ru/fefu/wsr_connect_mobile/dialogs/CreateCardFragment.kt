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
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentCreateCardBinding
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted
import ru.fefu.wsr_connect_mobile.dialogs.view_models.CreateCardViewModel
import ru.fefu.wsr_connect_mobile.extensions.formatTo
import ru.fefu.wsr_connect_mobile.extensions.toDate
import java.text.SimpleDateFormat
import java.util.*


class CreateCardFragment : DialogFragment(R.layout.fragment_create_card) {

    private val viewModel by lazy {
        ViewModelProvider(this)[CreateCardViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentCreateCardBinding.bind(view)
        var deadline: String? = null
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.showTitleError
            .onEach { binding.cardTitleInput.error = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.showShortDescError
            .onEach { binding.cardShortDescInput.error = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.showDeadlineError
            .onEach { binding.cardDeadlineInput.error = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.success
            .onEach {
                if (it) {
                    setFragmentResult("resultDialog", bundleOf("successfulDialog" to true))
                    findNavController().popBackStack()
                }
            }
            .launchWhenStarted(lifecycleScope)

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.thisMonthInUtcMilliseconds())
                .build()

        datePicker.addOnPositiveButtonClickListener {
            deadline = formatter.format(it)
            deadline?.let { s: String ->
                binding.etCardDeadline.setText(s.toDate().formatTo("dd MMM yyyy"))
            }.also { binding.cardDeadlineInput.isEndIconVisible = true }
        }

        binding.apply {
            cardDeadlineInput.isEndIconVisible = false
            editCardBtn.setOnClickListener {
                viewModel.createCard(
                    requireArguments().getInt("board_id"),
                    requireArguments().getInt("column_id"),
                    etCardTitle.text.toString(),
                    etCardShortDesc.text.toString(),
                    etCardLongDesc.text.toString(),
                    deadline
                )
            }
            etCardTitle.addTextChangedListener {
                cardTitleInput.isErrorEnabled = false
            }
            etCardShortDesc.addTextChangedListener {
                cardShortDescInput.isErrorEnabled = false
            }
            etCardDeadline.addTextChangedListener {
                cardDeadlineInput.isErrorEnabled = false
            }
            etCardDeadline.setOnClickListener {
                datePicker.show(requireActivity().supportFragmentManager, null)
            }
            cardDeadlineInput.setEndIconOnClickListener {
                etCardDeadline.text = null
                deadline = null
                cardDeadlineInput.isEndIconVisible = false
            }
        }
    }
}