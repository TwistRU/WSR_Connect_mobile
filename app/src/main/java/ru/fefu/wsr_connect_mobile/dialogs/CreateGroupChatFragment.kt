package ru.fefu.wsr_connect_mobile.dialogs

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.common.BASE_URL
import ru.fefu.wsr_connect_mobile.databinding.FragmentCreateGroupChatBinding
import ru.fefu.wsr_connect_mobile.dialogs.view_models.CreateGroupChatViewModel
import ru.fefu.wsr_connect_mobile.extensions.createBitmapFromResult
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted


class CreateGroupChatFragment : DialogFragment(R.layout.fragment_create_group_chat) {

    private val viewModel by lazy {
        ViewModelProvider(this)[CreateGroupChatViewModel::class.java]
    }

    lateinit var binding: FragmentCreateGroupChatBinding
    lateinit var imgView: ImageView
    private var chatImg: Bitmap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCreateGroupChatBinding.bind(view)
        imgView = binding.chatImg

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.showFieldError
            .onEach { binding.chatNameInput.error = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.success
            .onEach {
                if (it) {
                    setFragmentResult("resultDialog", bundleOf("successfulDialog" to true))
                    findNavController().popBackStack()
                }
            }
            .launchWhenStarted(lifecycleScope)

        binding.createGroupChatBtn.setOnClickListener {
            viewModel.createGroupChat(chatImg, binding.etChatName.text.toString())
        }

        binding.deleteImgBtn.setOnClickListener {
            Glide.with(requireContext())
                .load(R.drawable.ic_add_image)
                .error(R.drawable.ic_no_image)
                .into(imgView)
            chatImg = null
            binding.deleteImgBtn.visibility = View.GONE
        }

        binding.apply {
            chatImg.setOnClickListener {
                loadFileFromDevice()
            }
            etChatName.addTextChangedListener {
                chatNameInput.isErrorEnabled = false
            }
            val imgView = binding.chatImg
            Glide.with(requireContext())
                .load(R.drawable.ic_add_image)
                .error(R.drawable.ic_add_image)
                .into(imgView)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CODE_IMG_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            val imageBitmap = data.createBitmapFromResult(requireActivity())
            chatImg = imageBitmap!!
            Glide.with(requireContext()).load(chatImg).error(R.drawable.ic_no_image).into(imgView)
            binding.deleteImgBtn.visibility = View.VISIBLE
        }
    }

    private fun loadFileFromDevice() {
        when (ContextCompat.checkSelfPermission(requireContext(), STORAGE_PERMISSION)) {
            PackageManager.PERMISSION_GRANTED -> addPhotoFromIntent()
            else -> requestPermissionLauncher.launch(STORAGE_PERMISSION)
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
}