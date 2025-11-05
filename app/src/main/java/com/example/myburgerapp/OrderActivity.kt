package com.example.burgerapp

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myburgerapp.R
import com.example.myburgerapp.databinding.ActivityOrderBinding
import com.example.myburgerapp.util.Price
import android.widget.Button
import kotlin.jvm.java
import com.example.myburgerapp.TicketFinal
import android.content.Intent

class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnFinish = findViewById<Button>(R.id.btnFinish)
        btnFinish.setOnClickListener { navigatetoTicket() }
        val lines = intent.getStringArrayListExtra("orderLines") ?: arrayListOf()
        val total = intent.getDoubleExtra("orderTotal", 0.0)

        binding.tvTotal.text = getString(R.string.total_label) + ": " + Price.format(total)

        if (lines.isEmpty()) {
            binding.tvEmpty.visibility = android.view.View.VISIBLE
        } else {
            binding.tvEmpty.visibility = android.view.View.GONE
            binding.rvOrder.layoutManager = LinearLayoutManager(this)
            binding.rvOrder.adapter = SimpleTextAdapter(lines)
        }

        binding.btnClear.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage(getString(R.string.confirm_clear))
                .setPositiveButton(getString(R.string.yes)) { d, _ ->
                    lines.clear()
                    binding.rvOrder.adapter = SimpleTextAdapter(lines)
                    binding.tvEmpty.visibility = android.view.View.VISIBLE
                    binding.tvTotal.text = getString(R.string.total_label) + ": " + Price.format(0.0)
                    d.dismiss()
                }
                .setNegativeButton(getString(R.string.no)) { d, _ -> d.dismiss() }
                .show()
        }

        binding.btnFinish.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage("¡Gracias por tu compra!")
                .setPositiveButton("OK") { d, _ -> d.dismiss() }
                .show()
        }
    }

    private fun navigatetoTicket(){
        val intent = Intent(this, TicketFinal::class.java)
        startActivity(intent)
    }

    class SimpleTextAdapter(private val items: List<String>) :
        androidx.recyclerview.widget.RecyclerView.Adapter<SimpleTextAdapter.VH>() {

        inner class VH(v: android.view.View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {
            val tv: android.widget.TextView = v.findViewById(android.R.id.text1)
        }

        override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): VH {
            val v = android.view.LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
            return VH(v)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            holder.tv.text = items[position]
        }

        override fun getItemCount(): Int = items.size
    }
}