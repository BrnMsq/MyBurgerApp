package com.example.myburgerapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myburgerapp.databinding.ActivityFoodMenuBinding
import com.example.myburgerapp.models.Burger
import com.example.myburgerapp.util.Price
import android.content.Intent

class FoodMenu : AppCompatActivity() {

    private lateinit var binding: ActivityFoodMenuBinding
    private lateinit var burgerAdapter: BurgerAdapter


    private lateinit var burgers: List<Burger>




    private val orderLines = mutableListOf<String>()
    private var orderTotal = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnVolver = findViewById<Button>(R.id.btnVolverFLecha)
        btnVolver.setOnClickListener { navigatetoMain() }


        binding.backButton.setOnClickListener {
            finish()
        }


        burgers = listOf(
            Burger(1, getString(R.string.classic), "Doble carne, lechuga, tomate y salsa de la casa", 4200.0),
            Burger(2, getString(R.string.cheese), "Doble carne con queso cheddar y cebolla", 4500.0),
            Burger(3, getString(R.string.bacon), "Carne con bacon crocante y barbacoa", 4800.0),
            Burger(4, getString(R.string.veggie), "Medallón vegano, palta y tomate", 4300.0)
        )

        // Configura el adaptador y el RecyclerView
        burgerAdapter = BurgerAdapter(burgers.toMutableList()) { burger ->
            openCustomizeDialog(burger)
        }
        binding.rvBurgers.layoutManager = LinearLayoutManager(this)
        binding.rvBurgers.adapter = burgerAdapter

        // --- Lógica del buscador ---
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s?.toString()?.lowercase()?.trim() ?: ""
                val filteredList = if (query.isEmpty()) {
                    burgers
                } else {
                    burgers.filter { it.name.lowercase().contains(query) }
                }
                // Actualiza los datos en el adaptador
                burgerAdapter.updateData(filteredList)
            }
        })

        // --- Lógica del botón de Pagar ---
        /*binding.btnCheckout.setOnClickListener {
            val intent = Intent(this, OrderActivity::class.java)
            intent.putStringArrayListExtra("orderLines", ArrayList(orderLines))
            intent.putExtra("orderTotal", orderTotal)
            startActivity(intent)
        }*/
    }

    private fun openCustomizeDialog(burger: Burger) {

        val view = LayoutInflater.from(this).inflate(R.layout.dialog_customize, null)
        val rgSize: RadioGroup = view.findViewById(R.id.rgSize)
        val rgDoneness: RadioGroup = view.findViewById(R.id.rgDoneness)
        val cbCheese: CheckBox = view.findViewById(R.id.cbCheese)
        val cbBacon: CheckBox = view.findViewById(R.id.cbBacon)
        val seekSpice: SeekBar = view.findViewById(R.id.seekSpice)

        // Función para procesar el pedido
        fun computeAndAdd() {
            var price = burger.basePrice
            val size = when (rgSize.checkedRadioButtonId) {
                R.id.rbSmall -> { price -= 300; "Simple" }
                R.id.rbLarge -> { price += 500; "Triple" }
                else -> "Doble"
            }
            val doneness = when (rgDoneness.checkedRadioButtonId) {
                R.id.rbMediumRare -> "Jugosa"
                R.id.rbWellDone -> "Bien cocida"
                else -> "A punto"
            }
            if (cbCheese.isChecked) price += 250
            if (cbBacon.isChecked) price += 350
            val spice = seekSpice.progress

            val line = "${burger.name} ($size, $doneness, picor $spice) - ${Price.format(price)}"
            orderLines.add(line)
            orderTotal += price
        }


        // Crea y muestra un único diálogo
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_title))
            .setView(view)
            .setPositiveButton(getString(R.string.add_to_order)) { d, _ ->
                computeAndAdd()
                d.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { d, _ -> d.dismiss() }
            .show()
    }
    private fun navigatetoMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}