package ru.fefu.wsr_connect_mobile.tasks

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.common.App
import ru.fefu.wsr_connect_mobile.common.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentInvitationsBinding
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted
import ru.fefu.wsr_connect_mobile.adapters.InvitationListAdapter
import ru.fefu.wsr_connect_mobile.tasks.view_models.InvitationsViewModel


class InvitationsFragment :
    BaseFragment<FragmentInvitationsBinding>(R.layout.fragment_invitations) {

    private val viewModel by lazy {
        ViewModelProvider(this)[InvitationsViewModel::class.java]
    }

    private lateinit var adapter: InvitationListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            adapter = InvitationListAdapter()
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(requireActivity())

            toolbar.setOnMenuItemClickListener {
                val itemPos = adapter.selectedItemPos
                if (itemPos != -1) {
                    val item = adapter.currentList[itemPos]
                    viewModel.acceptInvitation(item.inviteId)
                    true
                } else false
            }
        }

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.success
            .onEach {
                if (it) {
                    App.sharedPreferences.edit().putBoolean("have_company", true).apply()
                    findNavController().navigate(R.id.action_invitationsFragment_to_companyFragment)
                }
            }
            .launchWhenStarted(lifecycleScope)

        viewModel.invitations
            .onEach {
                adapter.submitList(it)
            }
            .launchWhenStarted(lifecycleScope)

        viewModel.getInvitations()
    }
}