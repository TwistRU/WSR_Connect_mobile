package ru.fefu.wsr_connect_mobile.tasks

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.adapters.CardListAdapter
import ru.fefu.wsr_connect_mobile.databinding.FragmentTasksColumnsListBinding
import ru.fefu.wsr_connect_mobile.adapters.ColumnListAdapter
import ru.fefu.wsr_connect_mobile.launchWhenStarted
import ru.fefu.wsr_connect_mobile.remote.models.Card
import ru.fefu.wsr_connect_mobile.remote.models.Column
import ru.fefu.wsr_connect_mobile.tasks.view_models.ColumnsListViewModel


class TasksColumnsListFragment :
    BaseFragment<FragmentTasksColumnsListBinding>(R.layout.fragment_tasks_columns_list) {

    private val viewModel by lazy {
        ViewModelProvider(this)[ColumnsListViewModel::class.java]
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

            val adapter = ColumnListAdapter(
                {
                    val bundle = Bundle()
                    bundle.putInt("board_id", requireArguments().getInt("board_id"))
                    bundle.putInt("column_id", it.columnId)
                    findNavController().navigate(
                        R.id.action_tasksColumnsListFragment_to_deleteColumnFragment,
                        bundle
                    )
                },
                {
                    val bundle = Bundle()
                    bundle.putInt("board_id", requireArguments().getInt("board_id"))
                    bundle.putInt("column_id", it.columnId)
                    bundle.putString("column_title", it.columnTitle)
                    findNavController().navigate(
                        R.id.action_tasksColumnsListFragment_to_editColumnFragment,
                        bundle
                    )
                },
                object : CardListAdapter.OptionsMenuClickListener {
                    override fun onOptionsMenuClicked(
                        columnId: Int,
                        card: Card,
                        view: View
                    ) {
                        performOptionsMenuClick(columnId, card, view)
                    }
                }, listOf(
                    Column(
                        111,
                        "title",
                        listOf(
                            Card(
                                0,
                                "title",
                                "short",
                                "long",
                                "date",
                                "creator"
                            ),
                            Card(
                                1,
                                "title",
                                "short",
                                "long",
                                "date",
                                "creator"
                            ),
                        )
                    ),
                    Column(
                        222,
                        "title",
                        listOf(
                            Card(
                                0,
                                "title",
                                "short",
                                "long",
                                "date",
                                "creator"
                            )
                        )
                    ),
                )
            )
            recycler.adapter = adapter
            recycler.layoutManager =
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            PagerSnapHelper().attachToRecyclerView(recycler)





            toolbar.title = requireArguments().getString("board_name")
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            columnAddBtn.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt("board_id", requireArguments().getInt("board_id"))
                findNavController().navigate(
                    R.id.action_tasksColumnsListFragment_to_createColumnFragment,
                    bundle
                )
            }
            columnUsersBtn.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("context", "board")
                bundle.putInt("board_id", requireArguments().getInt("board_id"))
                findNavController().navigate(
                    R.id.action_tasksColumnsListFragment_to_companyUsersListFragment,
                    bundle
                )
            }
            columnFabBtn.setOnClickListener { onFabButtonClicked() }
        }

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

//        viewModel.columns
//            .onEach {
//                binding.apply {
//                    if (it.isEmpty()) {
//                        //TODO сообщение о том, что колонок нет
//                    } else {
//                        val adapter = ColumnListAdapter(
//                            {}, object : CardListAdapter.OptionsMenuClickListener {
//                                override fun onOptionsMenuClicked(
//                                    position: Int,
//                                    boardId: Int,
//                                    columnId: Int,
//                                    card: Card
//                                ) {
//                                    performOptionsMenuClick(position, boardId, columnId, card)
//                                }
//                            }, listOf(
//                                Column(
//                                    0,
//                                    "title",
//                                    listOf(
//                                        Card(
//                                            0,
//                                            "title",
//                                            "short",
//                                            "long",
//                                            "date",
//                                            "creator"
//                                        )
//                                    )
//                                )
//                            )
//                        )
//                        recycler.adapter = adapter
//                        recycler.layoutManager =
//                            LinearLayoutManager(
//                                requireContext(),
//                                LinearLayoutManager.HORIZONTAL,
//                                false
//                            )
//                        PagerSnapHelper().attachToRecyclerView(recycler)
//                    }
//                }
//            }
//            .launchWhenStarted(lifecycleScope)
//        viewModel.getColumns(requireArguments().getInt("board_id", 0))
    }

    private fun performOptionsMenuClick(
        columnId: Int,
        card: Card,
        view: View
    ) {
        val boardId = requireArguments().getInt("board_id")
        val popupMenu =
            PopupMenu(this.context, view)
        popupMenu.inflate(R.menu.cards_list_menu)
        popupMenu.setForceShowIcon(true)
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.editCard -> {
                        val bundle = Bundle()
                        bundle.putInt("board_id", boardId)
                        bundle.putInt("column_id", columnId)
                        bundle.putInt("card_id", card.cardId)
                        bundle.putString("card_title", card.cardTitle)
                        bundle.putString("card_short_desc", card.cardShortDesc)
                        bundle.putString("card_long_desc", card.cardLongDesc)
                        findNavController().navigate(
                            R.id.action_tasksColumnsListFragment_to_editCardFragment,
                            bundle
                        )
                        return true
                    }
                    R.id.deleteCard -> {
                        val bundle = Bundle()
                        bundle.putInt("board_id", boardId)
                        bundle.putInt("column_id", columnId)
                        bundle.putInt("card_id", card.cardId)
                        findNavController().navigate(
                            R.id.action_tasksColumnsListFragment_to_deleteCardFragment,
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
                columnAddBtn.visibility = View.VISIBLE
                columnUsersBtn.visibility = View.VISIBLE
            } else {
                columnAddBtn.visibility = View.INVISIBLE
                columnUsersBtn.visibility = View.INVISIBLE
            }
        }
    }

    private fun setAnimation(fabClicked: Boolean) {
        binding.apply {
            if (!fabClicked) {
                columnAddBtn.startAnimation(fromBottom)
                columnUsersBtn.startAnimation(fromBottom)
                columnFabBtn.startAnimation(rotateOpen)
            } else {
                columnAddBtn.startAnimation(toBottom)
                columnUsersBtn.startAnimation(toBottom)
                columnFabBtn.startAnimation(rotateClose)
            }
        }
    }

    private fun setClickable(fabClicked: Boolean) {
        binding.apply {
            if (!fabClicked) {
                columnAddBtn.isClickable = true
                columnUsersBtn.isClickable = true
            } else {
                columnAddBtn.isClickable = false
                columnUsersBtn.isClickable = false
            }
        }
    }
}