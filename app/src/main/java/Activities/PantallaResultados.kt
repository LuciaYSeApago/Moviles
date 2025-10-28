package Activities

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.example.myapplication.R


class PantallaResultados: ComponentActivity() {
    private lateinit var prefs: SharedPreferences
    private lateinit var tvResume: TextView
    private lateinit var layoutRanking: LinearLayout

    private lateinit var btnMenuPrincipal: Button

    private lateinit var soundPool: SoundPool
    private var sonidoClick = 0

    data class Jugador(
        val nombre: String,
        val aciertos: Int,
        val tiempo: String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_aitor)

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        sonidoClick = soundPool.load(this, R.raw.click, 1)

        initListeners()

        prefs = getSharedPreferences("rankingJuego", MODE_PRIVATE)

        // Obtener datos del jugador actual
        val nombre = intent.getStringExtra("nombreUsuario") ?: "Invitado"
        val aciertos = intent.getIntExtra("aciertos", 0)
        val total = intent.getIntExtra("total", 0)
        val tiempo = intent.getStringExtra("tiempo") ?: "00:00"

        tvResume.text = "♡ $nombre      $aciertos de $total\n♡ Tiempo: $tiempo"

        // Guardar resultado en ranking
        guardarResultado(nombre, aciertos, tiempo)

        // Mostrar ranking
        mostrarRanking()

        // Volver al inicio
        btnMenuPrincipal.setOnClickListener {
            soundPool.play(sonidoClick, 0.5f, 0.5f, 1, 0, 1f)
            val intent = Intent(this, PantallaPrincipal::class.java)
            startActivity(intent)
            Toast.makeText(this, "Otro quiz", Toast.LENGTH_SHORT).show()
        }
    }

    private fun guardarResultado(nombre: String, aciertos: Int, tiempo: String){
        val ranking = prefs.getStringSet("ranking", mutableSetOf())!!.toMutableSet()
        ranking.add("$nombre|$aciertos|$tiempo")
        prefs.edit().putStringSet("ranking", ranking).apply()
    }

    private fun mostrarRanking(){
        val ranking = prefs.getStringSet("ranking", setOf()) ?: setOf()

        val listaJugadores = ranking.mapNotNull { linea ->
            val datos = linea.split("|")
            if(datos.size == 3)
                Jugador(datos[0], datos[1].toInt(), datos[2])
            else null
        }.sortedWith(
            compareByDescending<Jugador> { it.aciertos }
                .thenBy { convertirTiempoASegundos(it.tiempo) }
        )
            .take(5)  // Solo los 5 mejores jugadores

        layoutRanking.removeAllViews()

        listaJugadores.forEachIndexed { index, jugador ->
            val tv = TextView(this).apply {
                text = "${index + 1}. ${jugador.nombre} - ${jugador.aciertos} aciertos - ${jugador.tiempo}"
                textSize = 20f
                setTextColor(getColor(R.color.rojo_coral))
                gravity = Gravity.START
                setPadding(32, 8, 32, 8)
            }

            layoutRanking.addView(tv)
        }
    }

    private fun convertirTiempoASegundos(tiempo: String): Int{
        val partes = tiempo.split(":")
        if (partes.size != 2) return Int.MAX_VALUE
        val minutos = partes[0].toIntOrNull() ?: 0
        val segundos = partes[1].toIntOrNull() ?: 0
        return minutos * 60 + segundos
    }
    private fun initListeners(){
        tvResume = findViewById(R.id.tvResumen)
        layoutRanking = findViewById(R.id.layoutRanking)
        btnMenuPrincipal = findViewById(R.id.btnMenuPrincipal)
    }
}