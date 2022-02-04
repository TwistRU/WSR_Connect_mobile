package ru.fefu.wsr_connect_mobile.messenger

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.adapters.UsersListAdapter
import ru.fefu.wsr_connect_mobile.common.BaseFragment
import ru.fefu.wsr_connect_mobile.databinding.FragmentSearchUserBinding
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted
import ru.fefu.wsr_connect_mobile.messenger.view_models.NewChatSearchViewModel


class NewChatSearchFragment :
    BaseFragment<FragmentSearchUserBinding>(R.layout.fragment_search_user) {

    private lateinit var adapter: UsersListAdapter

    private val viewModel by lazy {
        ViewModelProvider(this)[NewChatSearchViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UsersListAdapter {
            findNavController().navigate(
                R.id.action_newChatSearchFragment_to_companyUserFragment3,
                bundleOf("user_id" to it.userId)
            )
        }

        binding.apply {
            recycler.layoutManager = LinearLayoutManager(requireActivity())
            recycler.adapter = adapter

            toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

            binding.searchUserView.queryHint = getString(R.string.search_for_company_users)

            searchUserView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel.searchUserCompany(query)
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.isEmpty()) viewModel.searchUserCompany(null)
                    return false
                }
            })
        }

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.users
            .onEach { adapter.submitList(it) }
            .launchWhenStarted(lifecycleScope)

        viewModel.searchUserCompany(null)
    }
}
