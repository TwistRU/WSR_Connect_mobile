package ru.fefu.wsr_connect_mobile.tasks

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import ru.fefu.wsr_connect_mobile.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentTasksBinding


class TasksFragment : BaseFragment<FragmentTasksBinding>(R.layout.fragment_tasks) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            haveBtn.setOnClickListener {
                findNavController().navigate(R.id.action_tasksFragment_to_tasksBoardsListFragment)
            }
            noHaveBtn.setOnClickListener {
                findNavController().navigate(R.id.action_tasksFragment_to_tasksEmptyFragment)
            }
        }
    }
}