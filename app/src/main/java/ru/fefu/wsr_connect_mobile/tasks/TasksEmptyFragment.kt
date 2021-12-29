package ru.fefu.wsr_connect_mobile.tasks

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import ru.fefu.wsr_connect_mobile.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentTasksEmptyBinding

class TasksEmptyFragment : BaseFragment<FragmentTasksEmptyBinding>(R.layout.fragment_tasks_empty) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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