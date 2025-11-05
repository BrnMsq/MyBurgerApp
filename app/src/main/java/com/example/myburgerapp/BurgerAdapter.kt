package com.example.myburgerapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myburgerapp.databinding.ItemBurgerBinding
import com.example.myburgerapp.modelsimport.Burger
import com.example.myburgerapp.util.Price

class BurgerAdapter(
    private val burgers: MutableList<Burger>,
    private val onAddClicked: (Burger) -> Unit
) : RecyclerView.Adapter<BurgerAdapter.BurgerViewHolder>() {

    // El ViewHolder ahora usa los IDs de tu layout
    inner class BurgerViewHolder(private val binding: ItemBurgerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(burger: Burger) {
            // Asignar los datos a las vistas usando los IDs correctos
            binding.tvName.text = burger.name
            binding.tvDesc.text = burger.description // <-- ID de tu XML
            binding.tvPrice.text = Price.format(burger.basePrice)

            // Cargar la imagen en el ImageView con ID 'imgThumb'
            binding.imgThumb.setImageResource(burger.image) // <-- ID de tu XML

            // Configurar el clic en el botón con ID 'btnAdd'
            binding.btnAdd.setOnClickListener {
                onAddClicked(burger)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BurgerViewHolder {
        val binding =
            ItemBurgerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BurgerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BurgerViewHolder, position: Int) {
        holder.bind(burgers[position])
    }

    override fun getItemCount(): Int = burgers.size
}
