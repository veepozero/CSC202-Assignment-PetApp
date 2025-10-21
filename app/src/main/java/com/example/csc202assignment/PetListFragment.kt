package com.example.csc202assignment


import PetListAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.csc202assignment.databinding.FragmentPetDetailBinding
import com.example.csc202assignment.databinding.FragmentPetListBinding
import com.example.csc202assignment.databinding.ListItemPetBinding

import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

private const val TAG = "PetListFragment"

class PetListFragment : Fragment() {

    private var _binding: FragmentPetListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val petListViewModel: PetListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPetListBinding.inflate(inflater, container, false)
        binding.petRecyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fragment_pet_list, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.new_pet -> {
                        showNewPet()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                petListViewModel.pets.collect { pets ->  // ← Now matches ViewModel
                    binding.petRecyclerView.adapter =
                        PetListAdapter(pets) { petId ->
                            findNavController().navigate(
                                PetListFragmentDirections.showPetDetail(petId)
                            )
                        }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showNewPet() {
        viewLifecycleOwner.lifecycleScope.launch {
            val newPet = Pet(
                id = UUID.randomUUID(),
                title = "",
                date = Date(),
                isFound = false
            )
            petListViewModel.addPet(newPet)
            findNavController().navigate(
                PetListFragmentDirections.showPetDetail(newPet.id)
            )
        }
    }
}