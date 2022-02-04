package ru.fefu.wsr_connect_mobile.dialogs

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.common.BASE_URL
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.common.App
import ru.fefu.wsr_connect_mobile.databinding.FragmentCompanyUserBinding
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted
import ru.fefu.wsr_connect_mobile.dialogs.view_models.CompanyUserViewModel


class CompanyUserFragment : DialogFragment(R.layout.fragment_company_user) {

    private val viewModel by lazy {
        ViewModelProvider(this)[CompanyUserViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentCompanyUserBinding.bind(view)

        val userId = requireArguments().getInt("user_id")
        val myId = App.sharedPreferences.getInt("my_id", -1)
        if (userId == myId) binding.messageBtn.visibility = View.GONE

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.user
            .onEach {
                binding.apply {
                    val userInfo = "${it.email} @${it.username}"
                    userEmailAndUsername.text = userInfo

                    val userInitials = "${it.firstName} ${it.lastName}"
                    userFirstAndLastName.text = userInitials

                    userAbout.text = it.aboutMe

                    val url = "$BASE_URL${it.imgUrl}"
                    val imgView = binding.userImg
                    Glide.with(requireContext()).load(url).error(R.drawable.ic_profile)
                        .into(imgView)
                }
            }
            .launchWhenStarted(lifecycleScope)

        viewModel.chat
            .onEach {
                Navigation.findNavController(requireActivity(), R.id.rootActivityContainer)
                    .navigate(
                        R.id.action_navBottomFragment_to_nav_graph_in_chat,
                        bundleOf("chat_id" to it)
                    )
            }
            .launchWhenStarted(lifecycleScope)

        viewModel.getCompanyUserInfo(userId)

        binding.messageBtn.setOnClickListener { viewModel.startPrivateChat(userId) }
    }
}