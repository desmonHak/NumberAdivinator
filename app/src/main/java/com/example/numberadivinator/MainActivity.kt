package com.example.numberadivinator

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text
import kotlin.properties.Delegates
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    /**
     * Se usa para almacenar el rango del numero misterioso y obtener los numeros
     * del usuario que intenta adivinar
     */
    private lateinit var fieldNumero : EditText;

    /**
     * Se usa para obtener la cantidad maximas de vidas
     */
    private lateinit var fieldMaximos : EditText;
    private lateinit var buttonReset : Button;
    private lateinit var buttonInit : Button;

    private var number_random by Delegates.notNull<Int>();
    private var max_lives: Int by Delegates.notNull<Int>();
    private var now_lives by Delegates.notNull<Int>();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init_componente();
    }

    private fun init_game() {

        // hacemos el campo de vidas invisible
        fieldMaximos.visibility = View.INVISIBLE;

        // obtener la cantidad maximas de vidas ingresadas por el usuario
        max_lives = fieldMaximos.text.toString().toInt()
        if (max_lives <= 0) {
            Toast.makeText(this, "número inválido, se te da 10 lives", Toast.LENGTH_SHORT).show()
            max_lives = 10
        }
        // no alteramos la variable de vidas maximas, asi podremos calcular las vidas usadas
        now_lives = max_lives;

        // obtenemos el rango maximo con el que generar el numero aleatorio
        var max = fieldNumero.text.toString().toIntOrNull()
        if (max == null || max <= 0) {
            Toast.makeText(this, "número inválido, se usara 5 para el rango", Toast.LENGTH_SHORT).show()
            max = 5
        }
        number_random = Random.nextInt(0, max)

        // cambiamos el texto del boton "Init" a comprobar, teniendo doble funcion
        buttonInit.text = "Comprobar";

        // reasignamos el comportamiento
        buttonInit.setOnClickListener {
            // obtenemos el numero introducido
            val number : Int? = fieldNumero.text.toString().toIntOrNull();
            var msg : String;

            // si se introducio el numero, calculamos las vidas usadas y hacemos el boton
            // "Init" invisible
            if (number == number_random) {
                now_lives = max_lives - now_lives;
                msg = "Has ganado!. Has necesitado $now_lives intentos"
                buttonInit.visibility = View.INVISIBLE;
            }
            else {
                if (number != null) { // si no se acerto el numero
                    if (number < number_random) {
                        msg = "El numero oculto es mayor"
                    } else {
                        msg = "El numero oculto es menor"
                    }
                    // al fallar restamos vidas
                    now_lives--;
                } else {
                    msg = "Error, por alguna razon el numero no fue valido"
                }
            }
            // si se acabo las vidas, escondemos el boton para
            // obligar reiniciar con el boton
            if (now_lives <= 0) {
                msg = "Perdiste!. Intentos consumidos"
                buttonInit.visibility = View.INVISIBLE;
            }

            // mostraremos un mensaje u otro dependiendo del estado de la aplicacion
            Toast.makeText(
                this,
                msg, Toast.LENGTH_LONG
            ).show()

            // David
        }
    }

    /**
     * Permite reiniciar el juego
     */
    private fun reinit_game() {
        // hacemos los elementos visible
        fieldMaximos.visibility = View.VISIBLE;
        buttonInit.visibility   = View.VISIBLE;
        buttonInit.text         = "Init";

        // reasignamos el comportamiento del boton "Init"
        buttonInit.setOnClickListener {
            init_game()
        }
    }

    /**
     * inicia los componentes graficos
     */
    fun init_componente() {
        fieldMaximos    = findViewById<EditText>(R.id.intetosMaximos);
        fieldNumero     = findViewById<EditText>(R.id.NumeroRango);

        // obtenemos los botones y definimos sus comportamientos
        buttonReset     = findViewById<Button>(R.id.buttonReset);
        buttonReset.setOnClickListener {
            reinit_game()
        }
        buttonInit      = findViewById<Button>(R.id.buttonInit)
        buttonInit.setOnClickListener {
            init_game()
        }
    }
}