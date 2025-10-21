import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.csc202assignment.Pet
import com.example.csc202assignment.databinding.ListItemPetBinding

import java.util.UUID

class PetHolder(
    private val binding: ListItemPetBinding


) : RecyclerView.ViewHolder(binding.root) {
    fun bind(pet: Pet, onPetClicked: (petId: UUID) -> Unit) {
        binding.petTitle.text = pet.title
        binding.patrolDate.text = pet.date.toString()

        binding.root.setOnClickListener {
            onPetClicked(pet.id)
        }

        binding.petTitle.visibility = if (pet.isFound) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}

class PetListAdapter(
    private val pets: List<Pet>,
    private val onPetClicked: (petId: UUID) -> Unit
) : RecyclerView.Adapter<PetHolder>() {
    override fun onCreateViewHolder(

        parent: ViewGroup,
        viewType: Int
    ): PetHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemPetBinding.inflate(inflater, parent, false)
        return PetHolder(binding)
    }

    override fun onBindViewHolder(holder: PetHolder, position: Int) {
        val pet = pets[position]
        holder.bind(pet, onPetClicked)
    }

    override fun getItemCount() = pets.size
}
