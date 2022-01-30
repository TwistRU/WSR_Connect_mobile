package ru.fefu.wsr_connect_mobile.profile

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.onEach
import ru.fefu.wsr_connect_mobile.databinding.FragmentProfileBinding
import ru.fefu.wsr_connect_mobile.profile.view_models.ProfileViewModel
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import ru.fefu.wsr_connect_mobile.*
import ru.fefu.wsr_connect_mobile.extensions.createBitmapFromResult
import ru.fefu.wsr_connect_mobile.extensions.launchWhenStarted
import androidx.navigation.Navigation


class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    private val viewModel by lazy {
        ViewModelProvider(this)[ProfileViewModel::class.java]
    }

    private val userInfo = object {
        var firstName = ""
        var lastName = ""
        var email = ""
        var aboutMe = ""

        fun gotNewInfo() {
            binding.apply {
                firstName = etFirstName.text.toString()
                lastName = etLastName.text.toString()
                email = etEmail.text.toString()
                aboutMe = etAboutMe.text.toString()

                firstNameInput.isEndIconVisible = false
                lastNameInput.isEndIconVisible = false
                emailInput.isEndIconVisible = false
                aboutMeInput.isEndIconVisible = false
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.showLoading
            .onEach { binding.loader.isVisible = it }
            .launchWhenStarted(lifecycleScope)

        viewModel.profileImage
            .onEach {
                val url = "${BASE_URL}$it"
                val imgView = binding.profileImg
                Glide.with(this).load(url).error(R.drawable.ic_image_not_supported).into(imgView)
            }
            .launchWhenStarted(lifecycleScope)

        viewModel.showFirstNameResult
            .onEach {
                binding.apply {
                    if (it == "success") {
                        firstNameInput.isEndIconVisible = false
                        userInfo.firstName = etFirstName.text.toString()
                    } else
                        firstNameInput.error = it
                }
            }
            .launchWhenStarted(lifecycleScope)

        viewModel.showLastNameResult
            .onEach {
                binding.apply {
                    if (it == "success") {
                        lastNameInput.isEndIconVisible = false
                        userInfo.lastName = etLastName.text.toString()
                    } else
                        lastNameInput.error = it
                }
            }
            .launchWhenStarted(lifecycleScope)

        viewModel.showEmailResult
            .onEach {
                binding.apply {
                    if (it == "success") {
                        emailInput.isEndIconVisible = false
                        userInfo.email = etEmail.text.toString()
                    } else
                        emailInput.error = it
                }
            }
            .launchWhenStarted(lifecycleScope)

        viewModel.showAboutMeResult
            .onEach {
                binding.apply {
                    if (it == "success") {
                        aboutMeInput.isEndIconVisible = false
                        userInfo.aboutMe = etAboutMe.text.toString()
                    }
                }
            }
            .launchWhenStarted(lifecycleScope)

        viewModel.userInfo
            .onEach {
                binding.apply {
                    val username = "@" + it.username
                    currentNickname.text = username
                    currentEmail.text = it.email
                    etFirstName.setText(it.firstName)
                    etLastName.setText(it.lastName)
                    etAboutMe.setText(it.aboutMe)
                    etEmail.setText(it.email)

                    val url = "${BASE_URL}${it.imgUrl}"
                    val imgView = binding.profileImg
                    Glide.with(requireContext()).load(url).error(R.drawable.ic_image_not_supported)
                        .into(imgView)

                    userInfo.gotNewInfo()
                }
            }
            .launchWhenStarted(lifecycleScope)

        binding.apply {
            firstNameInput.isEndIconVisible = false
            lastNameInput.isEndIconVisible = false
            emailInput.isEndIconVisible = false
            aboutMeInput.isEndIconVisible = false
            changePasswordBtn.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_changePasswordFragment)
            }
            changeProfileImgBtn.setOnClickListener { loadFileFromDevice() }

            logoutBtn.setOnClickListener { viewModel.logout() }

            firstNameInput.setEndIconOnClickListener {
                viewModel.changeFirstName(etFirstName.text.toString())
            }
            lastNameInput.setEndIconOnClickListener {
                viewModel.changeLastName(etLastName.text.toString())
            }
            emailInput.setEndIconOnClickListener {
                viewModel.changeEmail(etEmail.text.toString())
            }
            aboutMeInput.setEndIconOnClickListener {
                viewModel.changeAboutMe(etAboutMe.text.toString())
            }

            etFirstName.addTextChangedListener {
                firstNameInput.isErrorEnabled = false
                firstNameInput.isEndIconVisible = etFirstName.text.toString() != userInfo.firstName
            }
            etLastName.addTextChangedListener {
                lastNameInput.isErrorEnabled = false
                lastNameInput.isEndIconVisible = etLastName.text.toString() != userInfo.lastName
            }
            etEmail.addTextChangedListener {
                emailInput.isErrorEnabled = false
                emailInput.isEndIconVisible = etEmail.text.toString() != userInfo.email
            }
            etAboutMe.addTextChangedListener {
                aboutMeInput.isErrorEnabled = false
                aboutMeInput.isEndIconVisible = etAboutMe.text.toString() != userInfo.aboutMe
            }

            logoutBtn.setOnClickListener {
                App.sharedPreferences.edit().clear().apply()
                Navigation.findNavController(requireActivity(), R.id.rootActivityContainer)
                    .navigate(R.id.action_navBottomFragment_to_nav_graph_auth)
            }
        }
        viewModel.getInfo()
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
            viewModel.sendProfileImage(image = imageBitmap!!)
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