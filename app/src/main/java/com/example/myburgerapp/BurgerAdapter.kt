package com.example.myburgerapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myburgerapp.databinding.ItemBurgerBinding
import com.example.myburgerapp.modelsimport.Burger
import com.example.myburgerapp.util.Price

class BurgerAdapter(

    private var burgers: MutableList<Burger>,
    private val onAddClicked: (Burger) -> Unit
) : RecyclerView.Adapter<BurgerAdapter.BurgerViewHolder>() {

    inner class BurgerViewHolder(private val binding: ItemBurgerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(burger: Burger) {
            binding.tvName.text = burger.name
            binding.tvDesc.text = burger.description
            binding.tvPrice.text = Price.format(burger.basePrice)
            binding.imgThumb.setImageResource(burger.image)

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



    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Burger>) {
        burgers.clear()
        burgers.addAll(newList)

        notifyDataSetChanged()
    }
}
