package ru.fefu.wsr_connect_mobile.tasks

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import ru.fefu.wsr_connect_mobile.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentTasksColumnsListBinding
import ru.fefu.wsr_connect_mobile.tasks.column_list.ColumnListAdapter
import ru.fefu.wsr_connect_mobile.tasks.column_list.ColumnsRepository


class TasksColumnsListFragment :
    BaseFragment<FragmentTasksColumnsListBinding>(R.layout.fragment_tasks_columns_list) {
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
            recycler.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            recycler.adapter = ColumnListAdapter(
                { },
                ColumnsRepository().getColumnsList()
            )
            PagerSnapHelper().attachToRecyclerView(recycler)

            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            columnAddBtn.setOnClickListener { }
            columnDeleteBtn.setOnClickListener { }
            columnEditBtn.setOnClickListener { }
            columnUsersBtn.setOnClickListener { }
            columnFabBtn.setOnClickListener { onFabButtonClicked() }
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
                columnAddBtn.visibility = View.VISIBLE
                columnDeleteBtn.visibility = View.VISIBLE
                columnEditBtn.visibility = View.VISIBLE
                columnUsersBtn.visibility = View.VISIBLE
            } else {
                columnAddBtn.visibility = View.INVISIBLE
                columnDeleteBtn.visibility = View.INVISIBLE
                columnEditBtn.visibility = View.INVISIBLE
                columnUsersBtn.visibility = View.INVISIBLE
            }
        }
    }

    private fun setAnimation(fabClicked: Boolean) {
        binding.apply {
            if (!fabClicked) {
                columnAddBtn.startAnimation(fromBottom)
                columnDeleteBtn.startAnimation(fromBottom)
                columnEditBtn.startAnimation(fromBottom)
                columnUsersBtn.startAnimation(fromBottom)
                columnFabBtn.startAnimation(rotateOpen)
            } else {
                columnAddBtn.startAnimation(toBottom)
                columnDeleteBtn.startAnimation(toBottom)
                columnEditBtn.startAnimation(toBottom)
                columnUsersBtn.startAnimation(toBottom)
                columnFabBtn.startAnimation(rotateClose)
            }
        }
    }

    private fun setClickable(fabClicked: Boolean) {
        binding.apply {
            if (!fabClicked) {
                columnAddBtn.isClickable = true
                columnDeleteBtn.isClickable = true
                columnEditBtn.isClickable = true
                columnUsersBtn.isClickable = true
            } else {
                columnAddBtn.isClickable = false
                columnDeleteBtn.isClickable = false
                columnEditBtn.isClickable = false
                columnUsersBtn.isClickable = false
            }
        }
    }
}