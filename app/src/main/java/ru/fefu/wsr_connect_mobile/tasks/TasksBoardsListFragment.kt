package ru.fefu.wsr_connect_mobile.tasks

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import ru.fefu.wsr_connect_mobile.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentTasksBoardsListBinding
import ru.fefu.wsr_connect_mobile.tasks.board_list.BoardListAdapter
import ru.fefu.wsr_connect_mobile.tasks.board_list.BoardsRepository


class TasksBoardsListFragment :
    BaseFragment<FragmentTasksBoardsListBinding>(R.layout.fragment_tasks_boards_list) {

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
            recycler.layoutManager = LinearLayoutManager(requireActivity())
            recycler.adapter = BoardListAdapter(
                { },
                BoardsRepository().getBoardsList()
            )
            boardAddBtn.setOnClickListener { }
            boardDeleteBtn.setOnClickListener { }
            boardEditBtn.setOnClickListener { }
            boardUsersBtn.setOnClickListener { }
            boardFabBtn.setOnClickListener { onFabButtonClicked() }
        }
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
                boardDeleteBtn.visibility = View.VISIBLE
                boardEditBtn.visibility = View.VISIBLE
                boardUsersBtn.visibility = View.VISIBLE
            } else {
                boardAddBtn.visibility = View.INVISIBLE
                boardDeleteBtn.visibility = View.INVISIBLE
                boardEditBtn.visibility = View.INVISIBLE
                boardUsersBtn.visibility = View.INVISIBLE
            }
        }
    }

    private fun setAnimation(fabClicked: Boolean) {
        binding.apply {
            if (!fabClicked) {
                boardAddBtn.startAnimation(fromBottom)
                boardDeleteBtn.startAnimation(fromBottom)
                boardEditBtn.startAnimation(fromBottom)
                boardUsersBtn.startAnimation(fromBottom)
                boardFabBtn.startAnimation(rotateOpen)
            } else {
                boardAddBtn.startAnimation(toBottom)
                boardDeleteBtn.startAnimation(toBottom)
                boardEditBtn.startAnimation(toBottom)
                boardUsersBtn.startAnimation(toBottom)
                boardFabBtn.startAnimation(rotateClose)
            }
        }
    }

    private fun setClickable(fabClicked: Boolean) {
        binding.apply {
            if (!fabClicked) {
                boardAddBtn.isClickable = true
                boardDeleteBtn.isClickable = true
                boardEditBtn.isClickable = true
                boardUsersBtn.isClickable = true
            } else {
                boardAddBtn.isClickable = false
                boardDeleteBtn.isClickable = false
                boardEditBtn.isClickable = false
                boardUsersBtn.isClickable = false
            }
        }
    }
}