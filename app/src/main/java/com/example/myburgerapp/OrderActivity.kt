package com.example.myburgerapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myburgerapp.databinding.ActivityOrderBinding
import com.example.myburgerapp.util.Price

class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding
    private lateinit var orderLines: ArrayList<String>
    private var orderTotal: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Recibir los datos del Intent
        orderLines = intent.getStringArrayListExtra("orderLines") ?: ArrayList()
        orderTotal = intent.getDoubleExtra("orderTotal", 0.0)

        // 2. Configurar listeners
        binding.backButtonOrder?.setOnClickListener {
            finish()
        }

        binding.btnClear.setOnClickListener {
            showClearConfirmationDialog()
        }

        // El botón para finalizar la compra con la nueva lógica
        binding.btnFinish.setOnClickListener {
            // Mostrar un diálogo de confirmación
            AlertDialog.Builder(this)
                .setTitle("Finalizar Compra")
                .setMessage("Tu pedido ha sido procesado. Serás redirigido al menú principal.")
                .setPositiveButton("Aceptar") { dialog, _ ->
                    // Limpiar los datos del carrito
                    FoodMenu.orderLines.clear()
                    FoodMenu.orderTotal = 0.0

                    // Crear el Intent para volver a MainActivity
                    val intent = Intent(this, MainActivity::class.java).apply {
                        // Limpiar la pila de navegación
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    dialog.dismiss()
                }
                .setCancelable(false)
                .show()
        }

        // 3. Actualizar la UI
        updateUI()
    }

    private fun updateUI() {
        binding.tvTotal.text = "${getString(R.string.total_label)} ${Price.format(orderTotal)}"

        if (orderLines.isEmpty()) {
            binding.rvOrder.visibility = View.GONE
            binding.tvEmpty.visibility = View.VISIBLE
            binding.btnFinish.visibility = View.GONE
            binding.btnClear.visibility = View.GONE
            binding.tvTotal.visibility = View.GONE
        } else {
            binding.rvOrder.visibility = View.VISIBLE
            binding.tvEmpty.visibility = View.GONE
            binding.btnFinish.visibility = View.VISIBLE
            binding.btnClear.visibility = View.VISIBLE
            binding.tvTotal.visibility = View.VISIBLE
            binding.rvOrder.layoutManager = LinearLayoutManager(this)
            binding.rvOrder.adapter = SimpleTextAdapter(orderLines)
        }
    }

    private fun showClearConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar")
            .setMessage("¿Estás seguro de que quieres vaciar el carrito?")
            .setPositiveButton("Sí") { dialog, _ ->
                FoodMenu.orderLines.clear()
                FoodMenu.orderTotal = 0.0
                orderLines.clear()
                orderTotal = 0.0
                updateUI()
                dialog.dismiss()
            }
            .setNegativeButton("No", null)
            .show()
    }

    // Adaptador simple (sin cambios)
    private class SimpleTextAdapter(private val items: List<String>) :
        androidx.recyclerview.widget.RecyclerView.Adapter<SimpleTextAdapter.VH>() {
        inner class VH(v: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {
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
