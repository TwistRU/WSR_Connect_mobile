package ru.fefu.wsr_connect_mobile.tasks

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.BASE_URL
import ru.fefu.wsr_connect_mobile.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.adapters.UsersListAdapter
import ru.fefu.wsr_connect_mobile.databinding.FragmentDetailCardBinding
import ru.fefu.wsr_connect_mobile.extensions.formatTo
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted
import ru.fefu.wsr_connect_mobile.extensions.toDate
import ru.fefu.wsr_connect_mobile.tasks.view_models.DetailCardViewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class DetailCardFragment : BaseFragment<FragmentDetailCardBinding>(R.layout.fragment_detail_card) {

    private lateinit var adapter: UsersListAdapter

    private val viewModel by lazy {
        ViewModelProvider(this)[DetailCardViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener("resultDialog") { requestKey, bundle ->
            if (bundle.getBoolean("successfulDialog")) findNavController().popBackStack()
        }

        val cardId = requireArguments().getInt("card_id")
        val creatorId = requireArguments().getInt("user_id")
        var editMode = false
        var users = mutableListOf<Int>()

        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        var deadline: String?

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.thisMonthInUtcMilliseconds())
                .build()

        datePicker.addOnPositiveButtonClickListener {
            val newDeadline = formatter.format(it)
            if (newDeadline != null && newDeadline.toDate().after(Date.from(Instant.now()))) {
                binding.cardDeadline.text = newDeadline.toDate().formatTo("dd MMM yyyy")
                binding.removeDeadlineBtn.visibility = View.VISIBLE
                deadline = newDeadline
            }
        }

        adapter = UsersListAdapter {

        }

        binding.apply {
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(requireActivity())

            toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

            cardDeadline.setOnClickListener {
                datePicker.show(
                    requireActivity().supportFragmentManager,
                    null
                )
            }
            creatorCard.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt("user_id", creatorId)
                findNavController().navigate(
                    R.id.action_detailCardFragment_to_companyUserFragment2,
                    bundle
                )
            }
            removeDeadlineBtn.setOnClickListener {
                deadline = null
                it.visibility = View.GONE
                cardDeadline.text = getString(R.string.add_deadline)
            }

            deleteCardBtn.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt("card_id", cardId)
                findNavController().navigate(
                    R.id.action_detailCardFragment_to_deleteCardFragment,
                    bundle
                )
            }
        }

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.creator
            .onEach {
                binding.apply {
                    val initials = "${it.firstName} ${it.lastName}"
                    val contacts = "${it.email} @${it.username}"
                    userFirstAndLastName.text = initials
                    userEmailAndUsername.text = contacts

                    val imgUrl = it.imgUrl
                    val imgView = binding.userImg
                    if (imgUrl != null) {
                        val url = BASE_URL + imgUrl
                        Glide.with(requireContext()).load(url)
                            .error(R.drawable.ic_image_not_supported).into(imgView)
                    } else imgView.visibility = View.GONE
                }
            }
            .launchWhenStarted(lifecycleScope)


        viewModel.card
            .onEach {
                adapter.submitList(it.users)
                for (i in it.users) users.add(i.userId)
                binding.apply {
                    toolbar.title = it.cardTitle
                    cardCreateDate.text = it.createDate.toDate().formatTo("dd MMM yyyy")
                    cardShortDesc.setText(it.cardShortDesc)
                    cardLongDesc.setText(it.cardLongDesc)
                    deadline = it.deadline

                    if (deadline != null) {
                        cardDeadline.text = it.deadline!!.toDate().formatTo("dd MMM yyyy")
                        removeDeadlineBtn.visibility = View.VISIBLE
                    }
                }
            }
            .launchWhenStarted(lifecycleScope)

        viewModel.getDetailCard(cardId)
        viewModel.getCreatorInfo(creatorId)
    }
}