package ru.fefu.wsr_connect_mobile.tasks

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentCreateCompanyBinding
import ru.fefu.wsr_connect_mobile.launchWhenStarted
import ru.fefu.wsr_connect_mobile.tasks.view_models.CreateCompanyViewModel


class CreateCompanyFragment :
    BaseFragment<FragmentCreateCompanyBinding>(R.layout.fragment_create_company) {

    private val viewModel by lazy {
        ViewModelProvider(this)[CreateCompanyViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.showFieldError
            .onEach { binding.companyNameInput.error = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.success
            .onEach { if (it) findNavController().navigate(R.id.action_createCompanyFragment_to_tasksBoardsListFragment) }
            .launchWhenStarted(lifecycleScope)

        binding.apply {
            createCompanyBtn.setOnClickListener {
                viewModel.createCompany(etCompanyName.text.toString())
            }
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            etCompanyName.addTextChangedListener {
                companyNameInput.isErrorEnabled = false
            }
        }
    }
}