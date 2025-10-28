package Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.example.myapplication.R
import android.os.Handler
import android.widget.SeekBar
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import android.media.MediaPlayer
import android.media.SoundPool
import android.media.AudioAttributes


class pantallapreguntas : ComponentActivity() {

    data class Question(
        val questionText: String,
        val categoria: String,
        val options: List<String>,
        val correctAnswerIndex:Int
    )

    private lateinit var tvUsuario: TextView
    private lateinit var tvTiempo: TextView
    private lateinit var tvQuestion: TextView
    private lateinit var optionButtons: List<Button>
    private lateinit var btnNext: Button
    private lateinit var btnPrevious: Button
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var btnSettings: Button

    // Ajustes
    private lateinit var seekVolume: SeekBar
    private lateinit var btnGuardarAjustes: Button
    private lateinit var prefs: SharedPreferences

    private lateinit var categoriaSeleccionada: String
    private lateinit var dificultadSeleccionada: String

    private lateinit var preguntasFiltradas: List<Question>

    private lateinit var musicaFondo: MediaPlayer
    private lateinit var soundPool: SoundPool


    private val questions = listOf(
        // ================= Ciencia =================
        Question(
            questionText = "¿Cuál es el planeta más grande del sistema solar?",
            categoria = "Ciencia",
            options = listOf("Tierra", "Marte", "Júpiter", "Saturno"),
            correctAnswerIndex = 2
        ),
        Question(
            questionText = "¿Cuál es el elemento químico con símbolo O?",
            categoria = "Ciencia",
            options = listOf("Oro", "Oxígeno", "Osmio", "Oganesón"),
            correctAnswerIndex = 1
        ),
        Question(
            questionText = "¿Qué fuerza mantiene a los planetas en órbita alrededor del Sol?",
            categoria = "Ciencia",
            options = listOf("Magnetismo", "Gravedad", "Fuerza centrífuga", "Electromagnetismo"),
            correctAnswerIndex = 1
        ),
        Question(
            questionText = "¿Qué tipo de energía se produce al quemar combustibles fósiles?",
            categoria = "Ciencia",
            options = listOf("Eléctrica", "Térmica", "Química", "Solar"),
            correctAnswerIndex = 2
        ),
        Question(
            questionText = "¿Cuál es la velocidad de la luz en el vacío (aprox.)?",
            categoria = "Ciencia",
            options = listOf("300.000 km/s", "150.000 km/s", "3.000 km/s", "30.000 km/s"),
            correctAnswerIndex = 0
        ),

        Question(
            questionText = "¿Qué unidad se usa para medir la intensidad luminosa?",
            categoria = "Ciencia",
            options = listOf("Lux", "Candela", "Lumen", "Watt"),
            correctAnswerIndex = 1
        ),
        Question(
            questionText = "¿Cuál es el órgano más grande del cuerpo humano?",
            categoria = "Ciencia",
            options = listOf("Corazón", "Hígado", "Pulmones", "Piel"),
            correctAnswerIndex = 3
        ),
        Question(
            questionText = "¿Cuál es el metal más ligero?",
            categoria = "Ciencia",
            options = listOf("Aluminio", "Litio", "Sodio", "Magnesio"),
            correctAnswerIndex = 1
        ),
        Question(
            questionText = "¿Qué órgano del cuerpo humano produce la insulina?",
            categoria = "Ciencia",
            options = listOf("Hígado", "Riñón", "Páncreas", "Estómago"),
            correctAnswerIndex = 2
        ),
        Question(
            questionText = "¿Qué fenómeno explica la descomposición de la luz blanca en colores?",
            categoria = "Ciencia",
            options = listOf("Reflexión", "Difracción", "Refracción", "Dispersión"),
            correctAnswerIndex = 3
        ),

        // ================= Historia =================
        Question(
            questionText = "¿En qué año comenzó la Segunda Guerra Mundial?",
            categoria = "Historia",
            options = listOf("1935", "1939", "1941", "1945"),
            correctAnswerIndex = 1
        ),
        Question(
            questionText = "¿Quién fue el primer presidente de Estados Unidos?",
            categoria = "Historia",
            options = listOf("George Washington", "Abraham Lincoln", "Thomas Jefferson", "John Adams"),
            correctAnswerIndex = 0
        ),
        Question(
            questionText = "¿Cuál civilización construyó Machu Picchu?",
            categoria = "Historia",
            options = listOf("Azteca", "Inca", "Maya", "Egipcia"),
            correctAnswerIndex = 1
        ),
        Question(
            questionText = "¿Qué imperio se expandió bajo Alejandro Magno?",
            categoria = "Historia",
            options = listOf("Persa", "Macedonio", "Romano", "Egipcio"),
            correctAnswerIndex = 1
        ),
        Question(
            questionText = "¿Quién pintó La Mona Lisa?",
            categoria = "Historia",
            options = listOf("Miguel Ángel", "Leonardo da Vinci", "Rafael", "Van Gogh"),
            correctAnswerIndex = 1
        ),
        Question(
            questionText = "¿En qué año llegó Cristóbal Colón a América?",
            categoria = "Historia",
            options = listOf("1492", "1500", "1485", "1512"),
            correctAnswerIndex = 0
        ),
        Question(
            questionText = "¿Qué guerra terminó con el Tratado de Versalles?",
            categoria = "Historia",
            options = listOf("Primera Guerra Mundial", "Segunda Guerra Mundial", "Guerra Fría", "Guerra Civil Española"),
            correctAnswerIndex = 0
        ),
        Question(
            questionText = "¿Quién fue el principal autor de la teoría heliocéntrica?",
            categoria = "Historia",
            options = listOf("Galileo Galilei", "Isaac Newton", "Nicolás Copérnico", "Johannes Kepler"),
            correctAnswerIndex = 2
        ),
        Question(
            questionText = "¿En qué año se fundó oficialmente la Unión Europea?",
            categoria = "Historia",
            options = listOf("1991", "1993", "1995", "2000"),
            correctAnswerIndex = 1
        ),
        Question(
            questionText = "¿Qué imperio construyó la ciudad de Tenochtitlán?",
            categoria = "Historia",
            options = listOf("Azteca", "Inca", "Maya", "Tolteca"),
            correctAnswerIndex = 0
        ),

        // ================= Deportes =================
        Question(
            questionText = "¿Quién ganó la Copa Mundial de Fútbol en 2018?",
            categoria = "Deportes",
            options = listOf("Brasil", "Alemania", "Francia", "Argentina"),
            correctAnswerIndex = 2
        ),
        Question(
            questionText = "¿Cuántos jugadores hay en un equipo de baloncesto en cancha?",
            categoria = "Deportes",
            options = listOf("5", "6", "7", "11"),
            correctAnswerIndex = 0
        ),
        Question(
            questionText = "¿En qué deporte se usa un disco llamado 'puck'?",
            categoria = "Deportes",
            options = listOf("Hockey sobre hielo", "Balonmano", "Rugby", "Fútbol"),
            correctAnswerIndex = 0
        ),
        Question(
            questionText = "¿Qué país organiza los Juegos Olímpicos modernos?",
            categoria = "Deportes",
            options = listOf("Grecia", "Francia", "Japón", "Suiza"),
            correctAnswerIndex = 0
        ),
        Question(
            questionText = "¿Cuántos sets se juegan en un partido de tenis masculino de Grand Slam?",
            categoria = "Deportes",
            options = listOf("3", "5", "7", "4"),
            correctAnswerIndex = 1
        ),
        Question(
            questionText = "¿Qué país ganó la Copa Mundial de Fútbol en 2022?",
            categoria = "Deportes",
            options = listOf("Francia", "Argentina", "Brasil", "Alemania"),
            correctAnswerIndex = 1
        ),
        Question(
            questionText = "¿Qué tenista español ha ganado múltiples títulos de Roland Garros?",
            categoria = "Deportes",
            options = listOf("Rafael Nadal", "David Ferrer", "Carlos Moyá", "Pablo Carreño"),
            correctAnswerIndex = 0
        ),
        Question(
            questionText = "¿Qué nadador tiene más medallas olímpicas en la historia?",
            categoria = "Deportes",
            options = listOf("Michael Phelps", "Ryan Lochte", "Mark Spitz", "Ian Thorpe"),
            correctAnswerIndex = 0
        ),
        Question(
            questionText = "¿Cuál es la distancia oficial de un maratón?",
            categoria = "Deportes",
            options = listOf("42,125 km", "42,185 km", "42,195 km", "42,165 km"),
            correctAnswerIndex = 2
        ),
        Question(
            questionText = "¿En qué país se originó el fútbol moderno?",
            categoria = "Deportes",
            options = listOf("España", "Alemania", "Italia", "Inglaterra"),
            correctAnswerIndex = 3
        )
    )

    private var currentQuestionIndex = 0
    private var correctAnswers = 0
    private val userAnswers = mutableListOf<Int?>()

    private var tiempoSegundos = 0
    private var handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null

    private var sonidoClick = 0
    private var sonidoAcierto = 0
    private var sonidoError = 0
    private var volumenGeneral = 1.0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_test_aitor2)


        musicaFondo = MediaPlayer.create(this, R.raw.musica_fondo)
        musicaFondo.isLooping = true
        musicaFondo.setVolume(0.5f, 0.5f)
        musicaFondo.start()

//  Efectos de sonido
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

// Cargar sonidos
        sonidoClick = soundPool.load(this, R.raw.click, 1)
        sonidoAcierto = soundPool.load(this, R.raw.correct, 1)
        sonidoError = soundPool.load(this, R.raw.wrong, 1)

        initListeners()

        preguntasFiltradas = questions.filter { it.categoria == categoriaSeleccionada }

        val numPreguntas: Int = when(dificultadSeleccionada) {
            "Facil" -> 5
            "Media" -> 8
            "Dificil" -> preguntasFiltradas.size
            else -> 3
        }

        preguntasFiltradas = preguntasFiltradas.take(numPreguntas)

        prefs = getSharedPreferences("ajustesJuego", MODE_PRIVATE)

        // Cargar preferencias previas
        loadPreferences()

        // Abrir panel
        btnSettings.setOnClickListener {
            drawerLayout.openDrawer(findViewById(R.id.settingsPanel))
        }

        // Guardar ajustes
        btnGuardarAjustes.setOnClickListener {
            soundPool.play(sonidoClick, volumenGeneral, volumenGeneral, 1, 0, 1f)
            savePreferences()
            drawerLayout.closeDrawers()
            Toast.makeText(this, "Ajustes guardados", Toast.LENGTH_SHORT).show()
        }

        seekVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                volumenGeneral = progress / 100f
                musicaFondo.setVolume(volumenGeneral, volumenGeneral)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })


        // Obtener nombre del usuario desde el intent
        val nombreUsuario = intent.getStringExtra("nombreUsuario") ?: "Invitado"
        tvUsuario.text = "$nombreUsuario"

        // Inicializa lista de respuestas del tamaño de las preguntas
        repeat(preguntasFiltradas.size){userAnswers.add(null)}

        startTimer()
        showQuestion()

        // Configurar listeners de los botones
        optionButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                handleAnswer(index)
            }
        }

        // Boton siguiente
        btnNext.setOnClickListener {
            if(currentQuestionIndex < preguntasFiltradas.size-1){
                currentQuestionIndex++
                showQuestion()
            }else{
                stopTimer()
                showResults()
            }
            soundPool.play(sonidoClick, volumenGeneral, volumenGeneral, 1, 0, 1f)
        }

        // Boton anterior
        btnPrevious.setOnClickListener {
            if(currentQuestionIndex > 0){
                currentQuestionIndex--
                showQuestion()
            }
            soundPool.play(sonidoClick, volumenGeneral, volumenGeneral, 1, 0, 1f)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (musicaFondo.isPlaying) musicaFondo.stop()
        musicaFondo.release()
        soundPool.release()
    }

    @SuppressLint("ResourceAsColor")
    private fun showQuestion(){
        val question = preguntasFiltradas[currentQuestionIndex]
        tvQuestion.text = question.questionText

        // Desactivar botón "Siguiente" al mostrar una nueva pregunta
        btnNext.isEnabled = false
        btnNext.alpha = 0.5f  // efecto visual de desactivado

        question.options.forEachIndexed { index, option ->
            optionButtons[index].text = option
            optionButtons[index].isEnabled = true
            optionButtons[index].setBackgroundColor(ContextCompat.getColor(this,R.color.rosa_boton))
        }

        // Mostrar colores si ya respondió
        val answer = userAnswers[currentQuestionIndex]
        if(answer != null){
            showAnswerColors(answer)
            btnNext.isEnabled = true
            btnNext.alpha = 1f
        }

        // Controlar visibilidad de los botones
        btnPrevious.isEnabled = currentQuestionIndex > 0
        btnNext.text = if(currentQuestionIndex == preguntasFiltradas.size - 1) "Finalizar" else "Siguiente"
    }


    private fun handleAnswer(selectedIndex: Int){
        val question = preguntasFiltradas[currentQuestionIndex]
        userAnswers[currentQuestionIndex] = selectedIndex

        soundPool.play(sonidoClick, volumenGeneral, volumenGeneral, 1, 0, 1f)

        showAnswerColors(selectedIndex)
        if (selectedIndex == question.correctAnswerIndex) {
            correctAnswers++
            soundPool.play(sonidoAcierto, volumenGeneral, volumenGeneral, 1, 0, 1f)
        } else {
            soundPool.play(sonidoError, volumenGeneral, volumenGeneral, 1, 0, 1f)
        }

        btnNext.isEnabled = true
        btnNext.alpha = 1f
    }

    @SuppressLint("ResourceAsColor")
    private fun showAnswerColors(selectedIndex: Int){
        val correctIndex = preguntasFiltradas[currentQuestionIndex].correctAnswerIndex

        optionButtons.forEachIndexed { index, button ->
            button.isEnabled = false
            when{
                index == correctIndex -> {
                    // Correcta siempre verde
                    button.setBackgroundColor(ContextCompat.getColor(this, R.color.verde_suave))
                }
                index == selectedIndex -> {
                    // Incorrecta seleccionada en rojo
                    button.setBackgroundColor(ContextCompat.getColor(this, R.color.rojo_coral))
                }
                else -> {
                    // Las demás opciones gris
                    button.setBackgroundColor(ContextCompat.getColor(this, R.color.gris_claro))
                }
            }
        }
    }

    private fun startTimer(){
        runnable = object : Runnable{
            override fun run() {
                tiempoSegundos++
                val minutos = tiempoSegundos / 60
                val segundos = tiempoSegundos % 60
                tvTiempo.text = String.format("%02d:%02d", minutos, segundos)
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable!!)
    }

    private fun stopTimer(){
        runnable?.let {handler.removeCallbacks(it)}
    }

    private fun showResults(){
        val total = preguntasFiltradas.size

        // Datos del jugador
        val resultadoIntent = Intent(this, PantallaResultados::class.java).apply {
            putExtra("nombreUsuario", tvUsuario.text.toString())
            putExtra("aciertos", correctAnswers)
            putExtra("total", total)
            putExtra("tiempo", tvTiempo.text.toString())
        }

        startActivity(resultadoIntent)
        finish()
    }

    private fun savePreferences(){
        prefs.edit().apply {
            putInt("volumen", seekVolume.progress)
            apply()
        }
    }

    private fun loadPreferences(){
        seekVolume.progress = prefs.getInt("volumen", 50)
    }

    // Funciones auxiliares
    private fun initListeners(){
        tvUsuario = findViewById(R.id.tvUsuario)
        tvTiempo = findViewById(R.id.tvTiempo)
        tvQuestion = findViewById(R.id.tvQuestion)
        optionButtons = listOf(
            findViewById(R.id.btnOption1),
            findViewById(R.id.btnOption2),
            findViewById(R.id.btnOption3),
            findViewById(R.id.btnOption4)
        )
        btnNext = findViewById(R.id.btnNext)
        btnPrevious = findViewById(R.id.btnPrevious)

        drawerLayout = findViewById(R.id.drawerLayout)
        btnSettings = findViewById(R.id.btnSettings)
        seekVolume = findViewById(R.id.seekVolume)
        btnGuardarAjustes = findViewById(R.id.btnGuardarAjustes)

        categoriaSeleccionada = intent.getStringExtra("categoria") ?: "General"
        dificultadSeleccionada = intent.getStringExtra("dificultad") ?: "Fácil"
    }
}