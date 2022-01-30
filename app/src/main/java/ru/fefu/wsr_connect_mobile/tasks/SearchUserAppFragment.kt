package ru.fefu.wsr_connect_mobile.tasks

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.compose.ui.graphics.Color
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.adapters.UsersListAdapter
import ru.fefu.wsr_connect_mobile.databinding.FragmentSearchUserBinding
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted
import ru.fefu.wsr_connect_mobile.tasks.view_models.SearchUserAppViewModel

class SearchUserAppFragment :
    BaseFragment<FragmentSearchUserBinding>(R.layout.fragment_search_user) {

    lateinit var adapter: UsersListAdapter

    private val viewModel by lazy {
        ViewModelProvider(this)[SearchUserAppViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UsersListAdapter {
            findNavController().navigate(
                R.id.action_searchUserAppFragment_to_sendInvitationFragment,
                bundleOf(
                    "user_id" to it.userId,
                    "username" to it.username
                )
            )
        }

        binding.apply {
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(requireActivity())

            searchUserView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    if (query.isNotEmpty() && query.length > 3) viewModel.searchUserApp(query)
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.isNotEmpty()) {
                        val red = Color(1f, 0f, 0f, 0.1f).hashCode()
                        val green = Color(0f, 1f, 0f, 0.1f).hashCode()
                        if (newText.length < 4) {
                            searchUserView.setBackgroundColor(red)
                        } else {
                            searchUserView.setBackgroundColor(green)
                        }
                    }
                    return false
                }
            })
        }

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.usersApp
            .onEach { adapter.submitList(it) }
            .launchWhenStarted(lifecycleScope)
    }
}