package ru.fefu.wsr_connect_mobile.tasks

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.fefu.wsr_connect_mobile.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentInvitationsBinding
import ru.fefu.wsr_connect_mobile.tasks.invitation_list.InvitationListAdapter
import ru.fefu.wsr_connect_mobile.tasks.invitation_list.InvitationsRepository


class InvitationsFragment :
    BaseFragment<FragmentInvitationsBinding>(R.layout.fragment_invitations) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            val adapter = InvitationListAdapter(InvitationsRepository().getInvitationsList())
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(requireActivity())

            toolbar.setOnMenuItemClickListener {
                if (adapter.selectedItemPos != -1) {
                    findNavController().navigate(R.id.action_invitationsFragment_to_tasksBoardsListFragment)
                    true
                } else false
            }
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}