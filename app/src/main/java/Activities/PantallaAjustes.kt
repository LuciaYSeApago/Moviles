package Activities

import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.SoundPool
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
    private lateinit var btnJugar: Button
    private lateinit var tvBienvenida: TextView
    private lateinit var prefs: SharedPreferences

    private lateinit var soundPool: SoundPool

    private var sonidoClick = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes_inicio)

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        sonidoClick = soundPool.load(this, R.raw.click, 1)

        // Inicializar variables
        initListeners()

        // SharedPreferences
        prefs = getSharedPreferences("ajustesJuego", MODE_PRIVATE)

        // Obtener nombre desde Intent
        nombreUsuario = intent.getStringExtra("nombreUsuario") ?: "Invitado"
        tvBienvenida.text = "Bienvenido/a, $nombreUsuario"

        // Configurar opciones de spinners
        val categorias = listOf("Ciencia", "Historia", "Deportes")
        val dificultades = listOf("Facil", "Media", "Dificil")

        spCategoria.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categorias)
        spDificultad.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dificultades)

        // Cargar ajustes previos si existen
        loadPreferences()

        // Botón jugar → pasa a la pantalla de preguntas
        btnJugar.setOnClickListener {
            soundPool.play(sonidoClick, 0.5f, 0.5f, 1, 0, 1f)
            savePreferences()
            val intent = Intent(this, pantallapreguntas::class.java).apply {
                putExtra("nombreUsuario", nombreUsuario)
                putExtra("categoria", spCategoria.selectedItem.toString())
                putExtra("dificultad", spDificultad.selectedItem.toString())
            }
            startActivity(intent)
            Toast.makeText(this, "Ajustes guardados. ¡Empezamos!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initListeners(){
        spCategoria = findViewById(R.id.spinnerCategoria)
        spDificultad = findViewById(R.id.spinnerDificultad)
        btnJugar = findViewById(R.id.btnJugar)
        tvBienvenida = findViewById(R.id.tvBienvenida)
    }

    private fun savePreferences(){
        prefs.edit().apply {
            putInt("categoria", spCategoria.selectedItemPosition)
            putInt("dificultad", spDificultad.selectedItemPosition)
            apply()
        }
    }

    private fun loadPreferences(){
        spCategoria.setSelection(prefs.getInt("categoria", 0))
        spDificultad.setSelection(prefs.getInt("dificultad", 0))
    }

}