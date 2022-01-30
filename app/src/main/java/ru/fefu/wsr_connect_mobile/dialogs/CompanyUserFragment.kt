package ru.fefu.wsr_connect_mobile.dialogs

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.BASE_URL
import ru.fefu.wsr_connect_mobile.R
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
                    Glide.with(requireContext()).load(url).error(R.drawable.ic_delete2)
                        .into(imgView)
                }
            }
            .launchWhenStarted(lifecycleScope)

        viewModel.chat
            .onEach {
                val bundle = Bundle()
                bundle.putInt("chat_id", it)
                Navigation.findNavController(requireActivity(), R.id.rootActivityContainer)
                    .navigate(R.id.action_navBottomFragment_to_nav_graph_in_chat, bundle)
            }
            .launchWhenStarted(lifecycleScope)

        viewModel.getCompanyUserInfo(userId)

        binding.toWriteBtn.setOnClickListener { viewModel.startChat(userId)}
    }
}