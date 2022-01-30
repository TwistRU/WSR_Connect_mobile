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
import ru.fefu.wsr_connect_mobile.databinding.FragmentBoardUsersBinding
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted
import ru.fefu.wsr_connect_mobile.tasks.view_models.BoardUsersListViewModel


class BoardUsersFragment :
    BaseFragment<FragmentBoardUsersBinding>(R.layout.fragment_board_users) {

    private lateinit var adapter: UsersListAdapter

    private val viewModel by lazy {
        ViewModelProvider(this)[BoardUsersListViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val boardId = requireArguments().getInt("board_id")

        adapter = UsersListAdapter {
//            val bundle = Bundle()
//            bundle.putInt("user_id", it.userId)
//
//            findNavController().navigate(
//                R.id.action_companyUsersListFragment_to_deleteUserFragment,
//                bundle
//            )
        }

        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            addBtn.setOnClickListener {
                findNavController().navigate(
                    R.id.action_boardUsersFragment_to_searchUserCompanyFragment
                )
            }
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(requireActivity())
            registerForContextMenu(recycler)
        }

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.boardUsers
            .onEach {
                adapter.submitList(it)
            }
            .launchWhenStarted(lifecycleScope)

        viewModel.getBoardUsers(boardId)
    }
}