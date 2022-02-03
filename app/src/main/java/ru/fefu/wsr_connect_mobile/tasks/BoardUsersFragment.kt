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
import ru.fefu.wsr_connect_mobile.databinding.FragmentBoardUsersBinding
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted
import ru.fefu.wsr_connect_mobile.tasks.view_models.BoardUsersViewModel


class BoardUsersFragment :
    BaseFragment<FragmentBoardUsersBinding>(R.layout.fragment_board_users) {

    private lateinit var adapter: DeleteUsersListAdapter

    private val viewModel by lazy {
        ViewModelProvider(this)[BoardUsersViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val boardId = requireArguments().getInt("board_id")
        val boardName = requireArguments().getString("board_name")
        val mine = requireArguments().getBoolean("mine")
        val myId = App.sharedPreferences.getInt("my_id", -1)


        setFragmentResultListener("resultDialog") { requestKey, bundle ->
            if (bundle.getBoolean("successfulDialog")) viewModel.getBoardUsers(boardId)
        }

        adapter = DeleteUsersListAdapter(
            myId,
            {
                findNavController().navigate(
                    R.id.action_boardUsersFragment_to_companyUserFragment,
                    bundleOf("user_id" to it.userId)
                )
            },
            {
                findNavController().navigate(
                    R.id.action_boardUsersFragment_to_deleteUserBoardFragment,
                    bundleOf("board_id" to boardId, "user_id" to it.userId)
                )
            },
            {
                findNavController().navigate(
                    R.id.action_boardUsersFragment_to_quitBoardFragment,
                    bundleOf("board_id" to boardId)
                )
            }
        )

        binding.apply {
            toolbar.title = boardName
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            addBtn.setOnClickListener {
                findNavController().navigate(
                    R.id.action_boardUsersFragment_to_searchUserCompanyFragment,
                    bundleOf("board_id" to boardId)
                )
            }
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(requireActivity())
            registerForContextMenu(recycler)

            if (mine) {
                addBtn.visibility = View.VISIBLE
                adapter.activeDelete = true
            }
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