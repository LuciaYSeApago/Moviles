package Activities

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.example.myapplication.R
import android.os.Handler
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout


class pantallapreguntas : ComponentActivity() {

    data class Question(
        val questionText: String,
        val options: List<String>,
        val correctAnswerIndex:Int
    )

    private lateinit var tvUsuario: TextView
    private lateinit var tvTitulo: TextView
    private lateinit var tvTiempo: TextView
    private lateinit var tvQuestion: TextView
    private lateinit var optionButtons: List<Button>
    private lateinit var btnNext: Button
    private lateinit var btnPrevious: Button
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var btnSettings: Button
    private lateinit var etNombreUsuario: EditText
    private lateinit var spCategoria: Spinner
    private lateinit var spDificultad: Spinner
    private lateinit var seekPreguntas: SeekBar
    private lateinit var tvNumPreguntas: TextView
    private lateinit var switchTema: Switch
    private lateinit var btnGuardarAjustes: Button
    private lateinit var prefs: SharedPreferences

    private val questions = listOf(
        Question("¿Cuál es el planeta más grande?", listOf("Tierra", "Marte", "Júpiter", "Saturno"), 2),
        Question("¿Cuál es el río más largo?", listOf("Nilo", "Amazonas", "Yangtsé", "Misisipi"), 1),
        Question("¿Qué idioma se habla en Brasil?", listOf("Español", "Portugués", "Inglés", "Francés"), 1)
    )

    private var currentQuestionIndex = 0
    private var correctAnswers = 0
    private val userAnswers = mutableListOf<Int?>()

    private var tiempoSegundos = 0
    private var handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_test_aitor2)

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        initListeners()

        prefs = getSharedPreferences("ajustesJuego", MODE_PRIVATE)

        // Cargar preferencias previas
        loadPreferences()

        // Abrir panel
        btnSettings.setOnClickListener {
            drawerLayout.openDrawer(findViewById(R.id.settingsPanel))
        }

        // Cambiar número de preguntas
        seekPreguntas.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvNumPreguntas.text = "Preguntas: ${progress + 1}"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Guardar ajustes
        btnGuardarAjustes.setOnClickListener {
            savePreferences()
            drawerLayout.closeDrawers()
            Toast.makeText(this, "Ajustes guardados", Toast.LENGTH_SHORT).show()
        }

        // Cambiar tema oscuro/claro
        switchTema.setOnCheckedChangeListener { _, isChecked ->
            val modo = if (isChecked)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(modo)
        }


        // Obtener nombre del usuario desde el intent
        val nombreUsuario = intent.getStringExtra("nombreUsuario") ?: "Invitado"
        tvUsuario.text = "$nombreUsuario"

        // Inicializa lista de respuestas del tamaño de las preguntas
        repeat(questions.size){userAnswers.add(null)}

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
            if(currentQuestionIndex < questions.size-1){
                currentQuestionIndex++
                showQuestion()
            }else{
                stopTimer()
                showResults()
            }
        }

        // Boton anterior
        btnPrevious.setOnClickListener {
            if(currentQuestionIndex > 0){
                currentQuestionIndex--
                showQuestion()
            }
        }

    }

    @SuppressLint("ResourceAsColor")
    private fun showQuestion(){
        val question = questions[currentQuestionIndex]
        tvQuestion.text = question.questionText

        question.options.forEachIndexed { index, option ->
            optionButtons[index].text = option
            optionButtons[index].isEnabled = true
            optionButtons[index].setBackgroundColor(R.color.purple_200)
        }

        // Mostrar colores si ya respondió
        val answer = userAnswers[currentQuestionIndex]
        if(answer != null){
            showAnswerColors(answer)
        }

        // Controlar visibilidad de los botones
        btnPrevious.isEnabled = currentQuestionIndex > 0
        btnNext.text = if(currentQuestionIndex == questions.size - 1) "Finalizar" else "Siguiente"
    }


    private fun handleAnswer(selectedIndex: Int){
        val question = questions[currentQuestionIndex]
        userAnswers[currentQuestionIndex] = selectedIndex

        showAnswerColors(selectedIndex)
        if(selectedIndex == question.correctAnswerIndex) correctAnswers++
    }

    @SuppressLint("ResourceAsColor")
    private fun showAnswerColors(selectedIndex: Int){
        val correctIndex = questions[currentQuestionIndex].correctAnswerIndex

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
        val total = questions.size
        val resultado = "Has acertado $correctAnswers de $total preguntas.\nTiempo: ${tvTiempo.text}"
        val dialog = AlertDialog.Builder(this)
            .setTitle("Resultados")
            .setMessage(resultado)
            .setPositiveButton("Aceptar"){_,_ -> finish()}
            .create()
        dialog.show()
    }

    private fun savePreferences(){
        prefs.edit().apply {
            putString("nombre", etNombreUsuario.text.toString())
            putInt("categoría", spCategoria.selectedItemPosition)
            putInt("dificultad", spDificultad.selectedItemPosition)
            putInt("numPreguntas", seekPreguntas.progress + 1)
            putBoolean("modoOscuro", switchTema.isChecked)
            apply()
        }
    }

    private fun loadPreferences(){
        etNombreUsuario.setText(prefs.getString("nombre", "Invitado"))
        spCategoria.setSelection(prefs.getInt("categoria", 0))
        spDificultad.setSelection(prefs.getInt("dificultad", 0))
        val num = prefs.getInt("numPreguntas", 10)
        seekPreguntas.progress = num - 1
        tvNumPreguntas.text = "Preguntas: $num"
        switchTema.isChecked = prefs.getBoolean("modoOscuro", false)
    }

    // Funciones auxiliares
    private fun initListeners(){
        tvUsuario = findViewById(R.id.tvUsuario)
        tvTitulo = findViewById(R.id.tvTitulo)
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
        etNombreUsuario = findViewById(R.id.etNombreUsuario)
        spCategoria = findViewById(R.id.spCategoria)
        spDificultad = findViewById(R.id.spDificultad)
        seekPreguntas = findViewById(R.id.seekPreguntas)
        tvNumPreguntas = findViewById(R.id.tvNumPreguntas)
        switchTema = findViewById(R.id.switchTema)
        btnGuardarAjustes = findViewById(R.id.btnGuardarAjustes)
    }
}