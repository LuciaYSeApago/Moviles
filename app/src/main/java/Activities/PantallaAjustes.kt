package Activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.myapplication.R

class PantallaAjustes: ComponentActivity() {

    private lateinit var nombreUsuario : String
    private lateinit var spCategoria: Spinner
    private lateinit var spDificultad: Spinner
    private lateinit var seekPreguntas: SeekBar
    private lateinit var tvNumPreguntas: TextView
    private lateinit var btnJugar: Button
    private lateinit var tvBienvenida: TextView
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes_inicio)

        spCategoria = findViewById(R.id.spinnerCategoria)
        spDificultad = findViewById(R.id.spinnerDificultad)
        seekPreguntas = findViewById(R.id.seekPreguntas)
        tvNumPreguntas = findViewById(R.id.tvNumPreguntas)
        btnJugar = findViewById(R.id.btnJugar)
        tvBienvenida = findViewById(R.id.tvBienvenida)

        // SharedPreferences
        prefs = getSharedPreferences("ajustesJuego", MODE_PRIVATE)

        // Obtener nombre desde Intent
        nombreUsuario = intent.getStringExtra("nombreUsuario") ?: "Invitado"
        tvBienvenida.text = "Bienvenido/a, $nombreUsuario"

        // Configurar opciones de spinners
        val categorias = listOf("General", "Ciencia", "Historia", "Deportes")
        val dificultades = listOf("Fácil", "Media", "Dificil")

        spCategoria.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categorias)
        spDificultad.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dificultades)


        // Cargar ajustes previos si existen
        loadPreferences()

        // Cambiar texto del número de preguntas
        seekPreguntas.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvNumPreguntas.text = "Número de preguntas: ${progress + 1}"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Botón jugar → pasa a la pantalla de preguntas
        btnJugar.setOnClickListener {
            savePreferences()
            val intent = Intent(this, pantallapreguntas::class.java).apply {
                putExtra("nombreUsuario", nombreUsuario)
                putExtra("categoria", spCategoria.selectedItem.toString())
                putExtra("dificultad", spDificultad.selectedItem.toString())
                putExtra("numPreguntas", seekPreguntas.progress + 1)
            }
            startActivity(intent)
            Toast.makeText(this, "Ajustes guardados. ¡Empezamos!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun savePreferences(){
        prefs.edit().apply {
            putInt("categoria", spCategoria.selectedItemPosition)
            putInt("dificultad", spDificultad.selectedItemPosition)
            putInt("numPreguntas", seekPreguntas.progress + 1)
            apply()
        }
    }

    private fun loadPreferences(){
        spCategoria.setSelection(prefs.getInt("categoria", 0))
        spDificultad.setSelection(prefs.getInt("dificultad", 0))
        val num = prefs.getInt("numPreguntas", 10)
        seekPreguntas.progress = num - 1
        tvNumPreguntas.text = "Preguntas: $num"
    }
}