package com.example.myburgerapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myburgerapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isDrawerOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Listener para el botón que abre el menú
        binding.menuUser.setOnClickListener {
            openDrawer()
        }

        // Listener para la capa oscura, que cierra el menú
        binding.scrimView.setOnClickListener {
            closeDrawer()
        }

        // Listener para el botón de ir al menú de comida (ya lo tenías)
        binding.btnMenu.setOnClickListener {
            val intent = Intent(this, FoodMenu::class.java)
            startActivity(intent)
        }
    }

    fun openDrawer() {
        if (!isDrawerOpen) {
            isDrawerOpen = true

            // AÑADIDO: Traer las vistas al frente antes de animar
            binding.userMenuContainer.bringToFront()
            binding.scrimView.bringToFront()

            // Hacer visibles el menú y la capa oscura
            binding.userMenuContainer.visibility = View.VISIBLE
            binding.scrimView.visibility = View.VISIBLE

            // Animar la entrada del menú
            binding.userMenuContainer.animate().translationX(0f).setDuration(300).start()
            // Animar la aparición de la capa oscura
            binding.scrimView.animate().alpha(1f).setDuration(300).start()
        }
    }


    fun closeDrawer() {
        if (isDrawerOpen) {
            isDrawerOpen = false
            // Animar la salida del menú
            binding.userMenuContainer.animate().translationX(-binding.userMenuContainer.width.toFloat()).setDuration(300).withEndAction {
                // Cuando la animación termina, ocultar las vistas
                binding.userMenuContainer.visibility = View.GONE
                binding.scrimView.visibility = View.GONE
            }.start()
            // Animar la desaparición de la capa oscura
            binding.scrimView.animate().alpha(0f).setDuration(300).start()
        }
    }

    // Gestionar el botón "atrás" del sistema
    override fun onBackPressed() {
        if (isDrawerOpen) {
            closeDrawer()
        } else {
            super.onBackPressed()
        }
    }
}
