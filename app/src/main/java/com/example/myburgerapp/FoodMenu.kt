package com.example.myburgerapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
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
import java.util.Locale
import kotlin.text.lowercase

class FoodMenu : AppCompatActivity() {

    private lateinit var binding: ActivityFoodMenuBinding

    private lateinit var fullBurgerList: List<Burger>

    private lateinit var burgerAdapter: BurgerAdapter

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

        binding.btnGoToCart.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, OrderActivity::class.java).apply {
                    putStringArrayListExtra("orderLines", orderLines)
                    putExtra("orderTotal", orderTotal)
                }
                startActivity(intent)
            }, 100)
        }


        setupBurgerList()
        setupRecyclerView()
        setupSearchListener()
    }

    private fun setupBurgerList() {

        fullBurgerList = listOf(
            Burger(1, getString(R.string.classic), "Doble carne, lechuga, tomate y salsa de la casa", 4200.0, R.drawable.burger_classic),
            Burger(2, getString(R.string.cheese), "Doble carne con queso cheddar y cebolla", 4500.0, R.drawable.burger_cheese),
            Burger(3, getString(R.string.bacon), "Carne con bacon crocante y barbacoa", 4800.0, R.drawable.burger_bacon),
            Burger(4, getString(R.string.veggie), "Medallón vegano, palta y tomate", 4300.0, R.drawable.burger_veggie)
        )
    }

    private fun setupRecyclerView() {

        burgerAdapter = BurgerAdapter(fullBurgerList.toMutableList()) { burger ->
            showCustomizeDialog(burger)
        }
        binding.rvBurgers.layoutManager = LinearLayoutManager(this)
        binding.rvBurgers.adapter = burgerAdapter
    }

    // --- FUNCIÓN DE BÚSQUEDA ---
    private fun setupSearchListener() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Cada vez que el texto cambia, filtramos la lista
                val query = s.toString()
                filterList(query)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // --- FUNCIÓN DE FILTRADO ---
    private fun filterList(query: String?) {
        if (query.isNullOrBlank()) {

            burgerAdapter.updateList(fullBurgerList)
        } else {

            val filteredList = fullBurgerList.filter { burger ->

                val nameMatch = burger.name.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))
                val descriptionMatch = burger.description.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))
                nameMatch || descriptionMatch
            }

            burgerAdapter.updateList(filteredList)
        }
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
