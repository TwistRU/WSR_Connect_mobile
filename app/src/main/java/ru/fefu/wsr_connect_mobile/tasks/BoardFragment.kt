package ru.fefu.wsr_connect_mobile.tasks

import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import android.widget.RelativeLayout
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.adapters.ColumnListAdapter
import ru.fefu.wsr_connect_mobile.common.BASE_URL
import ru.fefu.wsr_connect_mobile.common.BaseFragment
import ru.fefu.wsr_connect_mobile.databinding.FragmentBoardBinding
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted
import ru.fefu.wsr_connect_mobile.tasks.view_models.BoardViewModel


class BoardFragment :
    BaseFragment<FragmentBoardBinding>(R.layout.fragment_board) {

    private lateinit var adapter: ColumnListAdapter

    private val viewModel by lazy {
        ViewModelProvider(this)[BoardViewModel::class.java]
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
    private val fromRight: Animation by lazy {
        AnimationUtils.loadAnimation(this.context, R.anim.from_right_anim)
    }
    private val toRight: Animation by lazy {
        AnimationUtils.loadAnimation(this.context, R.anim.to_right_anim)
    }
    private var fabClicked = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val boardId = requireArguments().getInt("board_id")
        val boardName = requireArguments().getString("board_name")
        val boardImgUrl = requireArguments().getString("board_img_url")
        val mine = requireArguments().getBoolean("mine")

        setFragmentResultListener("resultDialog") { requestKey, bundle ->
            if (bundle.getBoolean("successfulDialog")) viewModel.getColumns(boardId)
        }

        binding.apply {
            toolbar.title = boardName
            val imgView = binding.boardImg
            if (boardImgUrl != null) {
                val url = BASE_URL + boardImgUrl
                Glide.with(requireContext()).load(url).error(R.drawable.ic_no_image)
                    .into(imgView)
            } else imgView.visibility = View.GONE

            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            fabBtn.setOnClickListener { onFabButtonClicked() }

            columnAddBtn.setOnClickListener {
                onFabButtonClicked()
                findNavController().navigate(
                    R.id.action_boardFragment_to_createColumnFragment,
                    bundleOf("board_id" to boardId)
                )
            }

            boardUsersBtn.setOnClickListener {
                onFabButtonClicked()
                findNavController().navigate(
                    R.id.action_boardFragment_to_boardUsersFragment,
                    bundleOf(
                        "board_id" to boardId,
                        "board_name" to boardName,
                        "mine" to mine,
                    )
                )
            }

            adapter = ColumnListAdapter(
                {
                    findNavController().navigate(
                        R.id.action_boardFragment_to_companyUserFragment,
                        bundleOf("user_id" to it.cardCreatorId)
                    )
                },
                {
                    if (it.available) {
                        Navigation.findNavController(requireActivity(), R.id.rootActivityContainer)
                            .navigate(
                                R.id.action_navBottomFragment_to_nav_graph_detail_card,
                                bundleOf(
                                    "board_id" to boardId,
                                    "card_id" to it.cardId,
                                    "user_id" to it.cardCreatorId,
                                    "mine" to it.mine
                                )
                            )
                    }
                },
                {
                    findNavController().navigate(
                        R.id.action_boardFragment_to_createCardFragment,
                        bundleOf("column_id" to it.columnId, "board_id" to boardId)
                    )
                },
                object : ColumnListAdapter.OptionsMenuClickListener {
                    override fun onOptionsMenuClicked(
                        columnId: Int,
                        columnTitle: String,
                        view: View
                    ) {
                        performColumnActionsMenuClick(columnId, columnTitle, view)
                    }
                }
            )
            recycler.adapter = adapter
            recycler.layoutManager =
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            PagerSnapHelper().attachToRecyclerView(recycler)
        }

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.columns
            .onEach {
                adapter.submitList(it)
            }
            .launchWhenStarted(lifecycleScope)
        viewModel.getColumns(boardId)
    }

    private fun performColumnActionsMenuClick(
        columnId: Int,
        columnTitle: String,
        view: View
    ) {
        val path = view.rootView.findViewById<RelativeLayout>(R.id.columnHeader)
        val popupMenu =
            PopupMenu(path.context, path, Gravity.END)
        popupMenu.inflate(R.menu.columns_list_menu)
        popupMenu.setForceShowIcon(true)
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.editColumn -> {
                        findNavController().navigate(
                            R.id.action_boardFragment_to_editColumnFragment,
                            bundleOf("column_id" to columnId, "column_title" to columnTitle)
                        )
                        return true
                    }
                    R.id.deleteColumn -> {
                        findNavController().navigate(
                            R.id.action_boardFragment_to_deleteColumnFragment,
                            bundleOf("column_id" to columnId)
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
                columnAddText.visibility = View.VISIBLE
                boardUsersBtn.visibility = View.VISIBLE
                boardUsersText.visibility = View.VISIBLE
            } else {
                columnAddBtn.visibility = View.INVISIBLE
                columnAddText.visibility = View.INVISIBLE
                boardUsersBtn.visibility = View.INVISIBLE
                boardUsersText.visibility = View.INVISIBLE
            }
        }
    }

    private fun setAnimation(fabClicked: Boolean) {
        binding.apply {
            if (!fabClicked) {
                columnAddBtn.startAnimation(fromBottom)
                boardUsersBtn.startAnimation(fromBottom)
                columnAddText.startAnimation(fromRight)
                boardUsersText.startAnimation(fromRight)
                fabBtn.startAnimation(rotateOpen)
            } else {
                columnAddBtn.startAnimation(toBottom)
                boardUsersBtn.startAnimation(toBottom)
                columnAddText.startAnimation(toRight)
                boardUsersText.startAnimation(toRight)
                fabBtn.startAnimation(rotateClose)
            }
        }
    }

    private fun setClickable(fabClicked: Boolean) {
        binding.apply {
            if (!fabClicked) {
                columnAddBtn.isClickable = true
                boardUsersBtn.isClickable = true
            } else {
                columnAddBtn.isClickable = false
                boardUsersBtn.isClickable = false
            }
        }
    }
}