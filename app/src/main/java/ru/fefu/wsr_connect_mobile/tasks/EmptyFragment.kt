package ru.fefu.wsr_connect_mobile.tasks

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import ru.fefu.wsr_connect_mobile.common.App
import ru.fefu.wsr_connect_mobile.common.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentEmptyBinding

class EmptyFragment : BaseFragment<FragmentEmptyBinding>(R.layout.fragment_empty) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (App.sharedPreferences.getBoolean("have_company", false)) {
            findNavController().navigate(R.id.action_tasksEmptyFragment_to_companyFragment)
        }

        binding.apply {
            toInvitationsBtn.setOnClickListener {
                findNavController().navigate(R.id.action_tasksEmptyFragment_to_invitationsFragment)
            }
            createCompanyBtn.setOnClickListener {
                findNavController().navigate(R.id.action_tasksEmptyFragment_to_createCompanyFragment)
            }
        }
    }
}