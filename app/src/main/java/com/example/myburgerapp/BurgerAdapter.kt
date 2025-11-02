package com.example.myburgerapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myburgerapp.models.Burger
import com.example.myburgerapp.util.Price
import com.google.android.material.button.MaterialButton
import com.example.myburgerapp.R

class BurgerAdapter(

    private var items: MutableList<Burger>,
    private val onAddClicked: (Burger) -> Unit
) : RecyclerView.Adapter<BurgerAdapter.BurgerVH>() {

    fun updateData(newBurgers: List<Burger>) {

        items.clear()
        items.addAll(newBurgers)
        notifyDataSetChanged()
    }



    inner class BurgerVH(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgThumb)
        val name: TextView = view.findViewById(R.id.tvName)
        val desc: TextView = view.findViewById(R.id.tvDesc)
        val price: TextView = view.findViewById(R.id.tvPrice)
        val btn: TextView = view.findViewById(R.id.btnAdd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BurgerVH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_burger, parent, false)
        return BurgerVH(v)
    }

    override fun onBindViewHolder(holder: BurgerVH, position: Int) {
        val item = items[position]
        holder.name.text = item.name
        holder.desc.text = item.description
        holder.price.text = Price.format(item.basePrice)
        holder.img.setImageResource(R.drawable.ic_burger)
        holder.btn.setOnClickListener { onAddClicked(item) }
    }

    override fun getItemCount(): Int = items.size
}
