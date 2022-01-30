package ru.fefu.wsr_connect_mobile.tasks

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.adapters.UsersListAdapter
import ru.fefu.wsr_connect_mobile.databinding.FragmentSearchUserBinding
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted
import ru.fefu.wsr_connect_mobile.tasks.view_models.SearchUserCompanyViewModel

class SearchUserCompanyFragment :
    BaseFragment<FragmentSearchUserBinding>(R.layout.fragment_search_user) {

    lateinit var adapter: UsersListAdapter

    private val viewModel by lazy {
        ViewModelProvider(this)[SearchUserCompanyViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UsersListAdapter {
//            findNavController().navigate(
//                R.id.action_searchUserAppFragment_to_sendInvitationFragment,
//                bundleOf(
//                    "user_id" to it.userId,
//                    "username" to it.username
//                )
//            )
        }

        binding.apply {
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(requireActivity())

            searchUserView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel.searchUserCompany(query)
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }
            })
        }

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.usersCompany
            .onEach { adapter.submitList(it) }
            .launchWhenStarted(lifecycleScope)
    }
}