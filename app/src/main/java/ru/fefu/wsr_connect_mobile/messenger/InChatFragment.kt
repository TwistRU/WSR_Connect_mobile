package ru.fefu.wsr_connect_mobile.messenger

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.BASE_URL
import ru.fefu.wsr_connect_mobile.BaseFragment
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.databinding.FragmentInChatBinding
import ru.fefu.wsr_connect_mobile.adapters.MessagesListAdapter
import ru.fefu.wsr_connect_mobile.extensions.createBitmapFromResult
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted
import ru.fefu.wsr_connect_mobile.messenger.view_models.InChatViewModel
import ru.fefu.wsr_connect_mobile.remote.models.Message


class InChatFragment : BaseFragment<FragmentInChatBinding>(R.layout.fragment_in_chat) {

    private lateinit var adapter: MessagesListAdapter

    private val viewModel by lazy {
        ViewModelProvider(this)[InChatViewModel::class.java]
    }

    var chatId = -1

    private val messBuff = object {
        var replyMode = false
        var editMode = false
        var replyMessageId = -1
        var editMessageId = -1

        fun reset() {
            replyMode = false
            editMode = false
            replyMessageId = -1
            editMessageId = -1
            binding.apply {
                replyMessageBody.text = null
                replyCreatorName.text = null
                input.text = null
                replyMessageContainer.visibility = View.GONE
                sendBtn.visibility = View.VISIBLE
                attachBtn.visibility = View.VISIBLE
                editBtn.visibility = View.GONE
                cancelEditBtn.visibility = View.GONE
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        chatId = requireArguments().getInt("chat_id")
        adapter = MessagesListAdapter(
            object : MessagesListAdapter.OptionsMenuClickListener {
                override fun onOptionsMenuClicked(
                    message: Message,
                    view: View
                ) {
                    performOptionsMenuClick(message, view)
                }
            }
        )

        binding.apply {
            val linearLayout = LinearLayoutManager(requireActivity())
            linearLayout.stackFromEnd = true
            recycler.layoutManager = linearLayout
            recycler.adapter = adapter

            toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

            attachBtn.setOnClickListener { loadFileFromDevice() }
            cancelReplyBtn.setOnClickListener { messBuff.reset() }
            cancelEditBtn.setOnClickListener { messBuff.reset() }
            sendBtn.setOnClickListener {
                val body = input.text.toString()
                if (body.isNotBlank()) {
                    if (messBuff.replyMode) {
                        viewModel.replyMessage(chatId, messBuff.replyMessageId, body)
                    } else {
                        viewModel.sendMessage(chatId, body)
                    }
                }
            }

            editBtn.setOnClickListener {
                val body = input.text.toString()
                if (body.isNotBlank()) {
                    viewModel.editMessage(chatId, messBuff.editMessageId, body)
                    messBuff.reset()
                }
            }
        }

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.success
            .onEach {
                if (it) {
                    viewModel.getMessages(chatId)
                }
            }
            .launchWhenStarted(lifecycleScope)

        viewModel.chatInfo
            .onEach {
                binding.toolbar.title = it.chatName
                val url = "$BASE_URL${it.imgUrl}"
                val imgView = binding.chatImage
                Glide.with(requireContext()).load(url).error(R.drawable.ic_delete2).into(imgView)
            }
            .launchWhenStarted(lifecycleScope)

        viewModel.messages
            .onEach {
                messBuff.reset()
                adapter.submitList(it.messages)
            }
            .launchWhenStarted(lifecycleScope)

        viewModel.getChatInfo(chatId)
        viewModel.getMessages(chatId)
    }

    private fun performOptionsMenuClick(message: Message, view: View) {
        val popupMenu =
            PopupMenu(binding.recycler.context, view)
        popupMenu.inflate(R.menu.messages_list_menu)
        if (message.mine) {
            popupMenu.menu.findItem(R.id.replyMessage).isVisible = false
        } else {
            popupMenu.menu.findItem(R.id.deleteMessage).isVisible = false
            popupMenu.menu.findItem(R.id.editMessage).isVisible = false
        }

        popupMenu.setForceShowIcon(true)
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.editMessage -> {
                        messBuff.reset()
                        messBuff.editMode = true
                        messBuff.editMessageId = message.messageId
                        binding.apply {
                            input.setText(message.messageBody)
                            input.focusable = View.FOCUSABLE
                            input.setSelection(input.text!!.length)
                            sendBtn.visibility = View.GONE
                            attachBtn.visibility = View.GONE
                            editBtn.visibility = View.VISIBLE
                            cancelEditBtn.visibility = View.VISIBLE
                        }
                        return true
                    }
                    R.id.deleteMessage -> {
                        val bundle = Bundle()
                        bundle.putInt("message_id", message.messageId)
                        findNavController().navigate(
                            R.id.action_inChatFragment_to_deleteMessageFragment,
                            bundle
                        )
                        return true
                    }
                    R.id.copyMessage -> {
                        val clipboard: ClipboardManager =
                            requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("text", message.messageBody);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(requireContext(), "Text Copied ", Toast.LENGTH_SHORT)
                            .show();
                        return true
                    }
                    R.id.replyMessage -> {
                        messBuff.reset()
                        messBuff.replyMode = true
                        messBuff.replyMessageId = message.messageId
                        binding.apply {
                            replyMessageBody.text = message.messageBody
                            replyCreatorName.text = message.creatorName
                            replyMessageContainer.visibility = View.VISIBLE
                        }
                        return true
                    }
                }
                return false
            }
        })
        popupMenu.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CODE_IMG_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            val imageBitmap = data.createBitmapFromResult(requireActivity())
            viewModel.sendImage(image = imageBitmap!!, chatId)
        }
    }

    private fun loadFileFromDevice() {
        when (ContextCompat.checkSelfPermission(requireContext(), STORAGE_PERMISSION)) {
            PackageManager.PERMISSION_GRANTED -> addPhotoFromIntent()
            else -> requestPermissionLauncher.launch(STORAGE_PERMISSION)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                addPhotoFromIntent()
            } else {
                Toast.makeText(
                    this.context,
                    resources.getString(R.string.ask_for_permission),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

    private fun addPhotoFromIntent() {
        val galleryIntent = Intent(Intent.ACTION_PICK).apply { this.type = "image/*" }

        val intentChooser = Intent(Intent.ACTION_CHOOSER).apply {
            this.putExtra(Intent.EXTRA_INTENT, galleryIntent)
        }
        startActivityForResult(intentChooser, CODE_IMG_GALLERY)
    }

    companion object {
        private const val CODE_IMG_GALLERY = 111
        private const val STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }
}
