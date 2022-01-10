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
import ru.fefu.wsr_connect_mobile.databinding.FragmentInvitationsBinding
import ru.fefu.wsr_connect_mobile.launchWhenStarted
import ru.fefu.wsr_connect_mobile.adapters.InvitationListAdapter
import ru.fefu.wsr_connect_mobile.tasks.view_models.InvitationsViewModel


class InvitationsFragment :
    BaseFragment<FragmentInvitationsBinding>(R.layout.fragment_invitations) {

    private val viewModel by lazy {
        ViewModelProvider(this)[InvitationsViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.success
            .onEach { if (it) findNavController().navigate(R.id.action_invitationsFragment_to_tasksBoardsListFragment) }
            .launchWhenStarted(lifecycleScope)

        viewModel.invitations
            .onEach {
                binding.apply {
                    if (it.isEmpty()) {
                        //TODO сообщение о том, что юзера никто не пригласил((
                    } else {
                        val adapter = InvitationListAdapter(it)
                        recycler.adapter = adapter
                        recycler.layoutManager = LinearLayoutManager(requireActivity())

                        toolbar.setOnMenuItemClickListener {
                            if (adapter.selectedItemPos != -1) {
                                viewModel.acceptInvitation(adapter.selectedItemPos)
                                true
                            } else false
                        }
                    }
                }
            }
            .launchWhenStarted(lifecycleScope)
        viewModel.getInvitations()
    }
}