package ru.fefu.wsr_connect_mobile.tasks

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.common.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.adapters.MultipleAddUsersListAdapter
import ru.fefu.wsr_connect_mobile.databinding.FragmentSearchUserBinding
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted
import ru.fefu.wsr_connect_mobile.tasks.view_models.SearchUserBoardViewModel

class SearchUserBoardFragment :
    BaseFragment<FragmentSearchUserBinding>(R.layout.fragment_search_user) {

    lateinit var adapter: MultipleAddUsersListAdapter

    private val viewModel by lazy {
        ViewModelProvider(this)[SearchUserBoardViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val boardId = requireArguments().getInt("board_id")
        val cardId = requireArguments().getInt("card_id")

        adapter = MultipleAddUsersListAdapter(
            {
                findNavController().navigate(
                    R.id.action_searchUserBoardFragment_to_companyUserFragment2,
                    bundleOf("user_id" to it.userId)
                )
            },
            {
                val cnt = adapter.users.count()
                binding.multipleCounter.text = cnt.toString()
                binding.multipleAddContainer.visibility = if (cnt > 0) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            },
        )

        binding.apply {
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(requireActivity())

            binding.searchUserView.queryHint = getString(R.string.search_for_board_users)

            searchUserView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel.searchUserBoard(boardId, query)
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.isEmpty()) viewModel.searchUserBoard(boardId,null)
                    return false
                }
            })

            multipleAddContainer.setOnClickListener {
                viewModel.addUsersToCard(adapter.users, cardId)
            }
        }

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.usersBoard
            .onEach { adapter.submitList(it) }
            .launchWhenStarted(lifecycleScope)

        viewModel.success
            .onEach {
                if (it) {
                    setFragmentResult("resultDialog", bundleOf("successfulDialog" to true))
                    findNavController().popBackStack()
                }
            }
            .launchWhenStarted(lifecycleScope)

        viewModel.searchUserBoard(boardId,null)
    }
}