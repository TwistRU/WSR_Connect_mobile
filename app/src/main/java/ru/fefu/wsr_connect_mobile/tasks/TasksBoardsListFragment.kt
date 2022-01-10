package ru.fefu.wsr_connect_mobile.tasks

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentTasksBoardsListBinding
import ru.fefu.wsr_connect_mobile.adapters.BoardListAdapter
import ru.fefu.wsr_connect_mobile.launchWhenStarted
import ru.fefu.wsr_connect_mobile.remote.models.Board
import ru.fefu.wsr_connect_mobile.tasks.view_models.BoardsListViewModel


class TasksBoardsListFragment :
    BaseFragment<FragmentTasksBoardsListBinding>(R.layout.fragment_tasks_boards_list) {

    private val viewModel by lazy {
        ViewModelProvider(this)[BoardsListViewModel::class.java]
    }

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(this.context, R.anim.rotate_open_anim)
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(this.context, R.anim.rotate_close_anim)
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(this.context, R.anim.from_bottom_anim)
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(this.context, R.anim.to_bottom_anim)
    }
    private var fabClicked = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            boardAddBtn.setOnClickListener {
                findNavController().navigate(R.id.action_tasksBoardsListFragment_to_createBoardFragment)
            }
            boardUsersBtn.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("context", "company")
                findNavController().navigate(
                    R.id.action_tasksBoardsListFragment_to_companyUsersListFragment,
                    bundle
                )
            }
            boardFabBtn.setOnClickListener { onFabButtonClicked() }

            val adapter = BoardListAdapter(
                {
                    if (it.isAvailable) {
                        val bundle = Bundle()
                        bundle.putInt("board_id", it.boardId)
                        bundle.putString("board_name", it.boardName)
                        findNavController().navigate(
                            R.id.action_tasksBoardsListFragment_to_tasksColumnsListFragment,
                            bundle
                        )
                    }
                },
                object : BoardListAdapter.OptionsMenuClickListener {
                    override fun onOptionsMenuClicked(
                        boardId: Int,
                        boardName: String,
                        view: View
                    ) {
                        performOptionsMenuClick(boardId, boardName, view)
                    }
                },
                listOf(
                    Board(1, "field", "field", "field", "field", true, null),
                    Board(2, "field", "field", "field", "field", true, null),
                    Board(3, "field", "field", "field", "field", true, null),
                    Board(15, "field", "field", "field", "field", true, null),
                    Board(5, "field", "field", "field", "field", true, null),
                    Board(6, "field", "field", "field", "field", true, null),
                    Board(7, "field", "field", "field", "field", true, null),
                    Board(8, "field", "field", "field", "field", true, null)
                ),
            )
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(requireActivity())

            registerForContextMenu(recycler)
        }

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.boards
            .onEach {
                binding.apply {
                    if (it.isEmpty()) {
                        //TODO сообщение о том, что досок нет
                    } else {
                        val adapter = BoardListAdapter(
                            {
                                if (it.isAvailable) {
                                    val bundle = Bundle()
                                    bundle.putInt("board_id", it.boardId)
                                    bundle.putString("board_name", it.boardName)
                                    findNavController().navigate(
                                        R.id.action_tasksBoardsListFragment_to_tasksColumnsListFragment,
                                        bundle
                                    )
                                }
                            }, object : BoardListAdapter.OptionsMenuClickListener {
                                override fun onOptionsMenuClicked(
                                    boardId: Int,
                                    boardName: String,
                                    view: View
                                ) {
                                    performOptionsMenuClick(boardId, boardName, view)
                                }
                            }, it
                        )
                        recycler.adapter = adapter
                        recycler.layoutManager = LinearLayoutManager(requireActivity())
                    }
                }
            }
            .launchWhenStarted(lifecycleScope)
//        viewModel.getBoards()
    }

    private fun performOptionsMenuClick(boardId: Int, boardName: String, view: View) {
        val popupMenu =
            PopupMenu(binding.recycler.context, view)
        popupMenu.inflate(R.menu.boards_list_menu)
        popupMenu.setForceShowIcon(true)
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.editBoard -> {
                        val bundle = Bundle()
                        bundle.putInt("board_id", boardId)
                        bundle.putString("board_name", boardName)
                        findNavController().navigate(
                            R.id.action_tasksBoardsListFragment_to_editBoardFragment,
                            bundle
                        )
                        return true
                    }
                    R.id.deleteBoard -> {
                        val bundle = Bundle()
                        bundle.putInt("board_id", boardId)
                        findNavController().navigate(
                            R.id.action_tasksBoardsListFragment_to_deleteBoardFragment,
                            bundle
                        )
                        return true
                    }
                }
                return false
            }
        })
        popupMenu.show()
    }

    private fun onFabButtonClicked() {
        setVisibility(fabClicked)
        setAnimation(fabClicked)
        setClickable(fabClicked)
        fabClicked = !fabClicked
    }

    private fun setVisibility(fabClicked: Boolean) {
        binding.apply {
            if (!fabClicked) {
                boardAddBtn.visibility = View.VISIBLE
                boardUsersBtn.visibility = View.VISIBLE
            } else {
                boardAddBtn.visibility = View.INVISIBLE
                boardUsersBtn.visibility = View.INVISIBLE
            }
        }
    }

    private fun setAnimation(fabClicked: Boolean) {
        binding.apply {
            if (!fabClicked) {
                boardAddBtn.startAnimation(fromBottom)
                boardUsersBtn.startAnimation(fromBottom)
                boardFabBtn.startAnimation(rotateOpen)
            } else {
                boardAddBtn.startAnimation(toBottom)
                boardUsersBtn.startAnimation(toBottom)
                boardFabBtn.startAnimation(rotateClose)
            }
        }
    }

    private fun setClickable(fabClicked: Boolean) {
        binding.apply {
            if (!fabClicked) {
                boardAddBtn.isClickable = true
                boardUsersBtn.isClickable = true
            } else {
                boardAddBtn.isClickable = false
                boardUsersBtn.isClickable = false
            }
        }
    }
}