package com.example.myburgerapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myburgerapp.OrderActivity
import com.example.myburgerapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isDrawerOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // =========================================================
        // FIX CLAVE: Establecer la posición inicial del menú
        // =========================================================
        // Necesitamos esperar a que la vista haya sido dibujada para conocer su ancho.
        binding.cvModalMenuUsuario.post {
            // Posiciona el menú completamente fuera del borde izquierdo
            binding.cvModalMenuUsuario.translationX = -binding.cvModalMenuUsuario.width.toFloat()
        }

        // Configurar el overlay para la animación de fade
        binding.viewModalOverlay.alpha = 0f
        binding.viewModalOverlay.visibility = View.GONE


        // =========================================================
        // LISTENERS
        // =========================================================

        // Listener para el botón que abre el menú
        binding.menuUser.setOnClickListener {
            openDrawer()
        }

        // Listener para la capa oscura, que cierra el menú
        binding.viewModalOverlay.setOnClickListener {
            closeDrawer()
        }

        // Listener para el botón de ir al carrito de compras
        binding.cartBuy.setOnClickListener { navigatetoBuy() }

        // Listener para el botón de ir al menú de comida (Asumo que está en el ScrollView)
        // Nota: Asegúrate de reemplazar este ID si el botón de menú de comida tiene otro ID.
        binding.btnMenu.setOnClickListener {
            val intent = Intent(this, FoodMenu::class.java)
            startActivity(intent)
        }

        // Listener de ejemplo para una opción del menú de usuario (Cerrar Sesión)
        // Para acceder a un botón dentro del <include>, usas binding.userMenu.id_del_componente
        // Ejemplo asumiendo que el include se llama 'userMenu' y el botón 'btn_cerrar_sesion'
        /* binding.userMenu.btnCerrarSesion.setOnClickListener {
             // Lógica de cierre de sesión
             closeDrawer()
        }
        */
    }

    fun openDrawer() {
        if (!isDrawerOpen) {
            isDrawerOpen = true

            // Mostrar el menú y el overlay
            binding.cvModalMenuUsuario.visibility = View.VISIBLE
            binding.viewModalOverlay.visibility = View.VISIBLE

            // Animación del modal: Se desliza de la izquierda (posición negativa) a la posición 0
            binding.cvModalMenuUsuario.animate().translationX(0f).setDuration(300).start()

            // Animación del overlay: Se hace visible gradualmente
            binding.viewModalOverlay.animate().alpha(1f).setDuration(300).start()

            // Buena práctica: trae el menú al frente para asegurar que no quede cubierto por la BottomNav
            binding.cvModalMenuUsuario.bringToFront()
            binding.viewModalOverlay.bringToFront()
        }
    }


    fun closeDrawer() {
        if (isDrawerOpen) {
            isDrawerOpen = false

            // Animación del modal: Se desliza de vuelta a su posición off-screen
            binding.cvModalMenuUsuario.animate()
                .translationX(-binding.cvModalMenuUsuario.width.toFloat())
                .setDuration(300)
                .withEndAction {
                    // Ocultar las vistas solo después de que la animación termine
                    binding.cvModalMenuUsuario.visibility = View.GONE
                    binding.viewModalOverlay.visibility = View.GONE
                }
                .start()

            // Animación del overlay: Se vuelve transparente gradualmente
            binding.viewModalOverlay.animate().alpha(0f).setDuration(300).start()
        }
    }

    private fun navigatetoBuy() {
        val intent = Intent(this, OrderActivity::class.java)
        startActivity(intent)
    }
}