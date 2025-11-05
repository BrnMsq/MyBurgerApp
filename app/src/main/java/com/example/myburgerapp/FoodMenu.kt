
package com.example.myburgerapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler // <-- IMPORTANTE AÑADIR
import android.os.Looper  // <-- IMPORTANTE AÑADIR
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myburgerapp.databinding.ActivityFoodMenuBinding
import com.example.myburgerapp.modelsimport.Burger
import com.example.myburgerapp.util.Price
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.radiobutton.MaterialRadioButton

class FoodMenu : AppCompatActivity() {

    private lateinit var binding: ActivityFoodMenuBinding
    private lateinit var burgers: List<Burger>

    companion object {
        val orderLines = ArrayList<String>()
        var orderTotal = 0.0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButtonMenu.setOnClickListener {
            finish()
        }

        // --- LA SOLUCIÓN DEFINITIVA APLICADA AL INTENT ---
        binding.btnGoToCart.setOnClickListener {
            // Usamos un Handler para asegurar que el Intent se lance después de que cualquier
            // otro evento de UI se haya procesado.
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, OrderActivity::class.java).apply {
                    putStringArrayListExtra("orderLines", orderLines)
                    putExtra("orderTotal", orderTotal)
                }
                startActivity(intent)
            }, 100) // 100 milisegundos de retraso es suficiente.
        }
        // --- FIN DE LA SOLUCIÓN ---

        setupBurgerList()
        setupRecyclerView()
    }

    // El resto del código (setupBurgerList, setupRecyclerView, showCustomizeDialog)
    // se mantiene exactamente igual que en la versión anterior, que ya era robusta.
    // ... (pega aquí el resto de tu código sin cambios)

    // Dentro de la clase FoodMenu.kt

    private fun setupBurgerList() {
        burgers = listOf(
            Burger(
                id = 1,
                name = getString(R.string.classic),
                description = "Doble carne, lechuga, tomate y salsa de la casa",
                basePrice = 4200.0,
                image = R.drawable.burger_classic // <-- Asegúrate de que el nombre del drawable coincida
            ),
            Burger(
                id = 2,
                name = getString(R.string.cheese),
                description = "Doble carne con queso cheddar y cebolla",
                basePrice = 4500.0,
                image = R.drawable.burger_cheese
            ),
            Burger(
                id = 3,
                name = getString(R.string.bacon),
                description = "Carne con bacon crocante y barbacoa",
                basePrice = 4800.0,
                image = R.drawable.burger_bacon
            ),
            Burger(
                id = 4,
                name = getString(R.string.veggie),
                description = "Medallón vegano, palta y tomate",
                basePrice = 4300.0,
                image = R.drawable.burger_veggie
            )
        )
    }


    private fun setupRecyclerView() {
        val burgerAdapter = BurgerAdapter(burgers.toMutableList()) { burger ->
            showCustomizeDialog(burger)
        }
        binding.rvBurgers.layoutManager = LinearLayoutManager(this)
        binding.rvBurgers.adapter = burgerAdapter
    }

    private fun showCustomizeDialog(burger: Burger) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_customize, null, false)

        val rgSize = dialogView.findViewById<RadioGroup>(R.id.rgSize)
        val rgDoneness = dialogView.findViewById<RadioGroup>(R.id.rgDoneness)
        val cbCheese = dialogView.findViewById<MaterialCheckBox>(R.id.cbCheese)
        val cbBacon = dialogView.findViewById<MaterialCheckBox>(R.id.cbBacon)
        val seekSpice = dialogView.findViewById<SeekBar>(R.id.seekSpice)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Confirmar y Agregar", null)
            .create()

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                var finalPrice = burger.basePrice
                val customizations = mutableListOf<String>()

                when (rgSize.checkedRadioButtonId) {
                    R.id.rbSmall -> {
                        finalPrice -= 500
                        customizations.add("Simple")
                    }
                    R.id.rbLarge -> {
                        finalPrice += 500
                        customizations.add("Triple")
                    }
                    else -> customizations.add("Doble")
                }

                val selectedDonenessButton = dialogView.findViewById<MaterialRadioButton>(rgDoneness.checkedRadioButtonId)
                customizations.add(selectedDonenessButton.text.toString())

                if (cbCheese.isChecked) {
                    finalPrice += 400
                    customizations.add("+ Queso")
                }
                if (cbBacon.isChecked) {
                    finalPrice += 500
                    customizations.add("+ Bacon")
                }

                val spiceLevel = seekSpice.progress
                if (spiceLevel > 1) {
                    customizations.add("Picante ($spiceLevel/5)")
                }

                val customizationText = customizations.joinToString()
                val line = "${burger.name} ($customizationText) - ${Price.format(finalPrice)}"

                orderLines.add(line)
                orderTotal += finalPrice

                Toast.makeText(this, "${burger.name} añadido al carrito", Toast.LENGTH_SHORT).show()

                dialog.dismiss()
            }
        }

        dialog.show()
    }
}
