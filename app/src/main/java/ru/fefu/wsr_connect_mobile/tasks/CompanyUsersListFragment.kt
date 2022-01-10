package ru.fefu.wsr_connect_mobile.tasks

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.adapters.CompanyUsersListAdapter
import ru.fefu.wsr_connect_mobile.databinding.FragmentTasksCompanyUsersListBinding
import ru.fefu.wsr_connect_mobile.launchWhenStarted
import ru.fefu.wsr_connect_mobile.remote.models.CompanyUser
import ru.fefu.wsr_connect_mobile.tasks.view_models.CompanyUsersListViewModel


class CompanyUsersListFragment :
    BaseFragment<FragmentTasksCompanyUsersListBinding>(R.layout.fragment_tasks_company_users_list) {

    private val viewModel by lazy {
        ViewModelProvider(this)[CompanyUsersListViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.inviteBtn.setOnClickListener {
            val bundle = Bundle()
            val context = requireArguments().getString("context")
            val boardId = if (context == "company") {
                null
            } else {
                requireArguments().getInt("board_id")
            }
            if (boardId != null) bundle.putInt("board_id", boardId)
            bundle.putString("context", context)
            findNavController().navigate(
                R.id.action_companyUsersListFragment_to_sendInvitationFragment,
                bundle
            )
        }

        binding.apply {

            val adapter = CompanyUsersListAdapter(
                {
                    val bundle = Bundle()
                    val context = requireArguments().getString("context")
                    if (context != "company") {
                        bundle.putInt("board_id", requireArguments().getInt("board_id"))
                    }
                    bundle.putInt("user_id", it.userId)
                    bundle.putString("context", context)

                    findNavController().navigate(
                        R.id.action_companyUsersListFragment_to_deleteUserFragment,
                        bundle
                    )
                },
                listOf(
                    CompanyUser(0, "first", "last", "email", null),
                    CompanyUser(2, "first", "last", "email", null),
                    CompanyUser(3, "first", "last", "email", null),
                    CompanyUser(8, "first", "last", "email", null),
                    CompanyUser(12, "first", "last", "email", null),
                    CompanyUser(1, "first", "last", "email", null),
                    CompanyUser(0, "first", "last", "email", null),
                    CompanyUser(2, "first", "last", "email", null),
                    CompanyUser(3, "first", "last", "email", null),
                    CompanyUser(8, "first", "last", "email", null),
                    CompanyUser(12, "first", "last", "email", null),
                    CompanyUser(1, "first", "last", "email", null),
                ),
            )
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(requireActivity())

            registerForContextMenu(recycler)
        }

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.success
            .onEach { if (it) findNavController().popBackStack() }
            .launchWhenStarted(lifecycleScope)
    }
}