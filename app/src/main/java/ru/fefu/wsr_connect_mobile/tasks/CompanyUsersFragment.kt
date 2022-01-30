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
import ru.fefu.wsr_connect_mobile.adapters.UsersListAdapter
import ru.fefu.wsr_connect_mobile.databinding.FragmentCompanyUsersBinding
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted
import ru.fefu.wsr_connect_mobile.tasks.view_models.CompanyUsersViewModel


class CompanyUsersFragment :
    BaseFragment<FragmentCompanyUsersBinding>(R.layout.fragment_company_users) {

    private lateinit var adapter: UsersListAdapter

    private val viewModel by lazy {
        ViewModelProvider(this)[CompanyUsersViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UsersListAdapter {
//            findNavController().navigate(
//                R.id.action_companyUsersFragment_to_deleteUserFragment,
//                bundleOf("user_id" to it.userId)
//            )
        }

        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            toolbar.title = requireArguments().getString("company_name")
            inviteBtn.setOnClickListener {
                findNavController().navigate(
                    R.id.action_companyUsersFragment_to_searchUserAppFragment
                )
            }
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(requireActivity())
            registerForContextMenu(recycler)
        }

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.companyUsers
            .onEach {
                adapter.submitList(it)
            }
            .launchWhenStarted(lifecycleScope)

        viewModel.success
            .onEach { if (it) findNavController().popBackStack() }
            .launchWhenStarted(lifecycleScope)

        viewModel.getCompanyUsers()
    }
}