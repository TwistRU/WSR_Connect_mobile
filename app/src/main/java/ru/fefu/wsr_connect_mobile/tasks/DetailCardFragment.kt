package ru.fefu.wsr_connect_mobile.tasks

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.common.BASE_URL
import ru.fefu.wsr_connect_mobile.common.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.adapters.DeleteUsersListAdapter
import ru.fefu.wsr_connect_mobile.common.App
import ru.fefu.wsr_connect_mobile.databinding.FragmentDetailCardBinding
import ru.fefu.wsr_connect_mobile.extensions.formatTo
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted
import ru.fefu.wsr_connect_mobile.extensions.toDate
import ru.fefu.wsr_connect_mobile.tasks.view_models.DetailCardViewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class DetailCardFragment : BaseFragment<FragmentDetailCardBinding>(R.layout.fragment_detail_card) {

    private lateinit var adapter: DeleteUsersListAdapter
    var deadline: String? = null

    private val viewModel by lazy {
        ViewModelProvider(this)[DetailCardViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val boardId = requireArguments().getInt("board_id")
        val cardId = requireArguments().getInt("card_id")
        val creatorId = requireArguments().getInt("user_id")
        val mine = requireArguments().getBoolean("mine")
        val myId = App.sharedPreferences.getInt("my_id", -1)

        setFragmentResultListener("resultDialog") { requestKey, bundle ->
            if (bundle.getBoolean("successfulDialogDeleteCard") || bundle.getBoolean("successfulDialogQuitCard")) {
                findNavController().popBackStack()
            }
            if (bundle.getBoolean("successfulDialog")) {
                viewModel.refreshCardUserList(cardId)
            }
        }

        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)

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

        adapter = DeleteUsersListAdapter(
            myId,
            {
                findNavController().navigate(
                    R.id.action_detailCardFragment_to_companyUserFragment2,
                    bundleOf("user_id" to it.userId)
                )
            },
            {
                findNavController().navigate(
                    R.id.action_detailCardFragment_to_deleteUserCardFragment,
                    bundleOf("card_id" to cardId, "user_id" to it.userId)
                )
            },
            {
                findNavController().navigate(
                    R.id.action_detailCardFragment_to_quitCardFragment,
                    bundleOf("card_id" to cardId)
                )
            },
        )

        binding.apply {
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(requireActivity())

            toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

            creatorCard.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt("user_id", creatorId)
                findNavController().navigate(
                    R.id.action_detailCardFragment_to_companyUserFragment2,
                    bundle
                )
            }
            if (mine) {
                cardDeadline.setOnClickListener {
                    datePicker.show(
                        requireActivity().supportFragmentManager,
                        null
                    )
                }
                removeDeadlineBtn.setOnClickListener {
                    deadline = null
                    it.visibility = View.GONE
                    cardDeadline.text = getString(R.string.add_deadline)
                }
                deleteCardBtn.setOnClickListener {
                    findNavController().navigate(
                        R.id.action_detailCardFragment_to_deleteCardFragment,
                        bundleOf("card_id" to cardId)
                    )
                }
                addUserBtn.setOnClickListener {
                    findNavController().navigate(
                        R.id.action_detailCardFragment_to_searchUserBoardFragment,
                        bundleOf("board_id" to boardId, "card_id" to cardId)
                    )
                }
                editCardBtn.setOnClickListener { editMode(true) }
                cancelEditCardBtn.setOnClickListener { (viewModel.getDetailCard(cardId)) }
                saveEditCardBtn.setOnClickListener {
                    binding.apply {
                        viewModel.editCard(
                            cardId,
                            cardTitle.text.toString(),
                            deadline,
                            cardShortDesc.text.toString(),
                            cardLongDesc.text.toString()
                        )
                    }
                }
                editMode(false)
                adapter.activeDelete = true
            }
        }

        viewModel.showLoading
            .onEach {
                binding.loader.isVisible = it
                visibleMode(!it)
            }
            .launchWhenStarted(lifecycleScope)

        viewModel.success
            .onEach { if (it) viewModel.getDetailCard(cardId) }
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

                    val url = BASE_URL + imgUrl
                    Glide.with(requireContext()).load(url)
                        .error(R.drawable.ic_no_image).into(imgView)

                }
            }
            .launchWhenStarted(lifecycleScope)


        viewModel.card
            .onEach {
                adapter.submitList(it.users)
                binding.apply {
                    toolbar.title = if (it.cardTitle.length > 16) {
                        it.cardTitle.substring(0, 15) + "..."
                    } else {
                        it.cardTitle
                    }

                    cardTitle.setText(it.cardTitle)
                    cardCreateDate.text = it.createDate.toDate().formatTo("dd MMM yyyy")
                    cardShortDesc.setText(it.cardShortDesc)
                    cardLongDesc.setText(it.cardLongDesc)
                    deadline = it.deadline

                    if (deadline != null) {
                        cardDeadline.text = it.deadline!!.toDate().formatTo("dd MMM yyyy")
                    }
                    if (mine) editMode(false)
                }

            }
            .launchWhenStarted(lifecycleScope)

        viewModel.users
            .onEach {
                adapter.submitList(it)
            }
            .launchWhenStarted(lifecycleScope)

        viewModel.getDetailCard(cardId)
        viewModel.getCreatorInfo(creatorId)
    }

    private fun editMode(status: Boolean) {
        val v = View.VISIBLE
        val g = View.GONE
        binding.apply {
            if (status) {
                cardDeadline.isEnabled = true
                cardShortDesc.isEnabled = true
                cardLongDesc.isEnabled = true
                addUserBtn.visibility = v
                editCardBtn.visibility = g
                deleteCardBtn.visibility = g
                saveEditCardBtn.visibility = v
                cancelEditCardBtn.visibility = v
                cardTitleContainer.visibility = v
                if (deadline != null) removeDeadlineBtn.visibility = v
            } else {
                cardDeadline.isEnabled = false
                cardShortDesc.isEnabled = false
                cardLongDesc.isEnabled = false
                addUserBtn.visibility = g
                editCardBtn.visibility = v
                deleteCardBtn.visibility = v
                saveEditCardBtn.visibility = g
                cancelEditCardBtn.visibility = g
                removeDeadlineBtn.visibility = g
                cardTitleContainer.visibility = g
            }
        }
    }

    private fun visibleMode(status: Boolean) {
        val v = View.VISIBLE
        val i = View.INVISIBLE
        binding.apply {
            if (status) {
                cardCreateDate.visibility = v
                cardDeadline.visibility = v
                cardShortDescContainer.visibility = v
                cardLongDescContainer.visibility = v
                cardUserListContainer.visibility = v
            } else {
                cardCreateDate.visibility = i
                cardDeadline.visibility = i
                cardShortDescContainer.visibility = i
                cardLongDescContainer.visibility = i
                cardUserListContainer.visibility = i
            }
        }
    }
}