package ru.fefu.wsr_connect_mobile.tasks

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.common.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.adapters.DeleteUsersListAdapter
import ru.fefu.wsr_connect_mobile.common.App
import ru.fefu.wsr_connect_mobile.databinding.FragmentCompanyUsersBinding
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted
import ru.fefu.wsr_connect_mobile.tasks.view_models.CompanyUsersViewModel


class CompanyUsersFragment :
    BaseFragment<FragmentCompanyUsersBinding>(R.layout.fragment_company_users) {

    private lateinit var adapter: DeleteUsersListAdapter

    private val viewModel by lazy {
        ViewModelProvider(this)[CompanyUsersViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mine = requireArguments().getBoolean("mine")
        val myId = App.sharedPreferences.getInt("my_id", -1)

        setFragmentResultListener("resultDialog") { requestKey, bundle ->
            if (bundle.getBoolean("successfulDialog")) viewModel.getCompanyUsers()
        }

        adapter = DeleteUsersListAdapter(
            myId,
            {
                findNavController().navigate(
                    R.id.action_companyUsersFragment_to_companyUserFragment,
                    bundleOf("user_id" to it.userId)
                )
            },
            {
                findNavController().navigate(
                    R.id.action_companyUsersFragment_to_deleteUserCompanyFragment,
                    bundleOf("user_id" to it.userId)
                )
            },
            {
                findNavController().navigate(
                    R.id.action_companyUsersFragment_to_quitCompanyFragment,
                )
            }
        )

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

            if (mine) {
                adapter.activeDelete = true
                inviteBtn.visibility = View.VISIBLE
            }
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