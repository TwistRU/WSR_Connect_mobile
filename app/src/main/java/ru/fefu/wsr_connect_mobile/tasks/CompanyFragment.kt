package ru.fefu.wsr_connect_mobile.tasks

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.BASE_URL
import ru.fefu.wsr_connect_mobile.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentCompanyBinding
import ru.fefu.wsr_connect_mobile.adapters.BoardListAdapter
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted
import ru.fefu.wsr_connect_mobile.tasks.view_models.CompanyViewModel


class CompanyFragment :
    BaseFragment<FragmentCompanyBinding>(R.layout.fragment_company) {

    private lateinit var adapter: BoardListAdapter

    private val viewModel by lazy {
        ViewModelProvider(this)[CompanyViewModel::class.java]
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

        setFragmentResultListener("resultDialog") { requestKey, bundle ->
            if (bundle.getBoolean("successfulDialog")) viewModel.getBoards()
        }

        binding.apply {
            boardAddBtn.setOnClickListener {
                findNavController().navigate(R.id.action_companyFragment_to_createBoardFragment)
            }
            companyUsersBtn.setOnClickListener {
                findNavController().navigate(
                    R.id.action_companyFragment_to_companyUsersFragment,
                    bundleOf("company_name" to binding.toolbar.title)
                )
            }
            fabBtn.setOnClickListener { onFabButtonClicked() }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getBoards()
            }

            adapter = BoardListAdapter(
                {
                    findNavController().navigate(
                        R.id.action_companyFragment_to_companyUserFragment,
                        bundleOf("user_id" to it.boardCreatorId)
                    )
                },
                {
                    if (it.isAvailable) {
                        findNavController().navigate(
                            R.id.action_companyFragment_to_boardFragment,
                            bundleOf(
                                "board_id" to it.boardId,
                                "board_name" to it.boardName,
                                "board_img_url" to it.imgUrl
                            )
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
            )
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(requireActivity())
            registerForContextMenu(recycler)
        }

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.companyInfo
            .onEach {
                binding.apply {
                    toolbar.title = it.companyName
                    val imgUrl = it.imgUrl
                    val imgView = binding.companyImg
                    if (imgUrl != null) {
                        val url = BASE_URL + imgUrl
                        Glide.with(requireContext()).load(url)
                            .error(R.drawable.ic_image_not_supported).into(imgView)
                    } else imgView.visibility = View.GONE
                }

            }
            .launchWhenStarted(lifecycleScope)

        viewModel.boards
            .onEach {
                adapter.submitList(it)
                binding.swipeRefreshLayout.isRefreshing = false
            }
            .launchWhenStarted(lifecycleScope)
        viewModel.getCompanyInfo()
        viewModel.getBoards()
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
                        findNavController().navigate(
                            R.id.action_companyFragment_to_editBoardFragment,
                            bundleOf("board_id" to boardId, "board_name" to boardName)
                        )
                        return true
                    }
                    R.id.deleteBoard -> {
                        findNavController().navigate(
                            R.id.action_companyFragment_to_deleteBoardFragment,
                            bundleOf("board_id" to boardId)
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
                companyUsersBtn.visibility = View.VISIBLE
            } else {
                boardAddBtn.visibility = View.INVISIBLE
                companyUsersBtn.visibility = View.INVISIBLE
            }
        }
    }

    private fun setAnimation(fabClicked: Boolean) {
        binding.apply {
            if (!fabClicked) {
                boardAddBtn.startAnimation(fromBottom)
                companyUsersBtn.startAnimation(fromBottom)
                fabBtn.startAnimation(rotateOpen)
            } else {
                boardAddBtn.startAnimation(toBottom)
                companyUsersBtn.startAnimation(toBottom)
                fabBtn.startAnimation(rotateClose)
            }
        }
    }

    private fun setClickable(fabClicked: Boolean) {
        binding.apply {
            if (!fabClicked) {
                boardAddBtn.isClickable = true
                companyUsersBtn.isClickable = true
            } else {
                boardAddBtn.isClickable = false
                companyUsersBtn.isClickable = false
            }
        }
    }
}