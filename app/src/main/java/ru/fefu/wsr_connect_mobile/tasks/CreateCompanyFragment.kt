package ru.fefu.wsr_connect_mobile.tasks

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import ru.fefu.wsr_connect_mobile.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentCreateCompanyBinding


class CreateCompanyFragment :
    BaseFragment<FragmentCreateCompanyBinding>(R.layout.fragment_create_company) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            createCompanyBtn.setOnClickListener {
                findNavController().navigate(R.id.action_createCompanyFragment_to_tasksBoardsListFragment)
            }
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}