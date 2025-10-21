package com.example.csc202assignment

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.doOnLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.csc202assignment.databinding.FragmentPetDetailBinding

import kotlinx.coroutines.launch
import java.io.File
import java.util.Date

private const val DATE_FORMAT = "EEE, MMM, dd"

class PetDetailFragment : Fragment() {

    private var _binding: FragmentPetDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val args: PetDetailFragmentArgs by navArgs()

    private val petDetailViewModel: PetDetailViewModel by viewModels {
        PetDetailViewModelFactory(args.petId)
    }

    private val selectPet = registerForActivityResult(
        ActivityResultContracts.PickContact()
    ) { uri: Uri? ->
        uri?.let { parseContactSelection(it) }
    }

    private val takePhoto = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { didTakePhoto: Boolean ->
        if (didTakePhoto && photoName != null) {
            petDetailViewModel.updatePet { oldPet ->
                oldPet.copy(photoFileName = photoName)
            }
        }
    }

    private var photoName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentPetDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            petTitle.doOnTextChanged { text, _, _, _ ->
                petDetailViewModel.updatePet { oldPet ->
                    oldPet.copy(title = text.toString())
                }
            }

            petSighted.setOnCheckedChangeListener { _, isChecked ->
                petDetailViewModel.updatePet { oldPet ->
                    oldPet.copy(isFound = isChecked)
                }
            }

            petSighted.setOnClickListener {
                selectPet.launch(null)
            }

            val selectPetType = selectPet.contract.createIntent(
                requireContext(),
                null
            )
            petSighted.isEnabled = canResolveIntent(selectPetType)

            petCamera.setOnClickListener {
                photoName = "IMG_${Date()}.JPG"
                val photoFile = File(
                    requireContext().applicationContext.filesDir,
                    photoName
                )
                val photoUri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.example.csc202assignment.fileprovider",
                    photoFile
                )

                takePhoto.launch(photoUri)
            }



            petCamera.isEnabled = true
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                petDetailViewModel.pet.collect { pet ->
                    pet?.let { updateUi(it) }
                }
            }
        }

        setFragmentResultListener(
            DatePickerFragment.REQUEST_KEY_DATE
        ) { _, bundle ->
            val newDate =
                bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as Date
            petDetailViewModel.updatePet { it.copy(date = newDate) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUi(pet: Pet) {
        binding.apply {
            if (petTitle.text.toString() != pet.title) {
                petTitle.setText(pet.title)
            }


            patrolDate.text = pet.date.toString()
            patrolDate.setOnClickListener {
                findNavController().navigate(
                    PetDetailFragmentDirections.selectDate(pet.date)
                )
            }

            petSighted.isChecked = pet.isFound

            petReport.setOnClickListener {
                val reportIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, getCrimeReport(pet))
                    putExtra(
                        Intent.EXTRA_SUBJECT,
                        getString(R.string.pet_report_subject)
                    )
                }
                val chooserIntent = Intent.createChooser(
                    reportIntent,
                    getString(R.string.send_report)
                )
                startActivity(chooserIntent)
            }

            petType.text = pet.petType.ifEmpty {
                getString(R.string.pet_type_text)
            }

            updatePhoto(pet.photoFileName)
        }
    }

    private fun getCrimeReport(pet: Pet): String {
        val solvedString = if (pet.isFound) {
            getString(R.string.pet_report_solved)
        } else {
            getString(R.string.pet_report_unsolved)
        }

        val dateString = DateFormat.format(DATE_FORMAT, pet.date).toString()
        val suspectText = if (pet.petType.isBlank()) {
            getString(R.string.pet_report_not_found)
        } else {
            getString(R.string.pet_report_location, pet.petType)
        }

        return getString(
            R.string.pet_report,
            pet.title, dateString, solvedString, suspectText
        )
    }

    private fun parseContactSelection(contactUri: Uri) {
        val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)

        val queryCursor = requireActivity().contentResolver
            .query(contactUri, queryFields, null, null, null)

        queryCursor?.use { cursor ->
            if (cursor.moveToFirst()) {
                val suspect = cursor.getString(0)
                petDetailViewModel.updatePet { oldPet ->
                    oldPet.copy(petType = suspect)
                }
            }
        }
    }

    private fun canResolveIntent(intent: Intent): Boolean {
        val packageManager: PackageManager = requireActivity().packageManager
        val resolvedActivity: ResolveInfo? =
            packageManager.resolveActivity(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
            )
        return resolvedActivity != null
    }

    private fun updatePhoto(photoFileName: String?) {
        if (binding.petPhoto.tag != photoFileName) {
            val photoFile = photoFileName?.let {
                File(requireContext().applicationContext.filesDir, it)
            }

            if (photoFile?.exists() == true) {
                binding.petPhoto.doOnLayout { measuredView ->
                    val scaledBitmap = getScaledBitmap(
                        photoFile.path,
                        measuredView.width,
                        measuredView.height
                    )
                    binding.petPhoto.setImageBitmap(scaledBitmap)
                    binding.petPhoto.tag = photoFileName
                }
            } else {
                binding.petPhoto.setImageBitmap(null)
                binding.petPhoto.tag = null
            }
        }
    }
}
