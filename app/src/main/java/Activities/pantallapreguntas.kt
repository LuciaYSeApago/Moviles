package Activities

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R

class pantallapreguntas : ComponentActivity() {

    data class Question(
        val questionText: String,
        val options: List<String>,
        val correctAnswerIndex:Int
    )

    private lateinit var tvQuestion: TextView
    private lateinit var optionButtons: List<Button>
    private lateinit var btnNext: Button
    private lateinit var btnPrevious: Button

    private val questions = listOf(
        Question("¿Cuál es el planeta más grande?", listOf("Tierra", "Marte", "Júpiter", "Saturno"), 2),
        Question("¿Cuál es el río más largo?", listOf("Nilo", "Amazonas", "Yangtsé", "Misisipi"), 1),
        Question("¿Qué idioma se habla en Brasil?", listOf("Español", "Portugués", "Inglés", "Francés"), 1)
    )

    private var currentQuestionIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_test_aitor)

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        tvQuestion = findViewById(R.id.tvQuestion)
        optionButtons = listOf(
            findViewById(R.id.btnOption1),
            findViewById(R.id.btnOption2),
            findViewById(R.id.btnOption3),
            findViewById(R.id.btnOption4)
        )
        btnNext = findViewById(R.id.btnNext)
        btnPrevious = findViewById(R.id.btnPrevious)

        showQuestion()

        // Configurar listeners de los botones
        optionButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                checkAnswer(index)
            }
        }

        // Boton siguiente
        btnNext.setOnClickListener {
            if(currentQuestionIndex < questions.size-1){
                currentQuestionIndex++
                showQuestion()
            }else{
                Toast.makeText(this, "!Has terminado el quiz!", Toast.LENGTH_SHORT).show()
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

    private fun showQuestion(){
        val question = questions[currentQuestionIndex]
        tvQuestion.text = question.questionText

        question.options.forEachIndexed { index, option ->
            optionButtons[index].text = option
        }

        // Controlar visibilidad de los botones
        btnPrevious.isEnabled = currentQuestionIndex > 0
        btnNext.text = if(currentQuestionIndex == questions.size - 1) "Finalizar" else "Siguiente"
    }

    private fun checkAnswer(selectedIndex: Int){
        val question = questions[currentQuestionIndex]

        if(selectedIndex == question.correctAnswerIndex){
            Toast.makeText(this, "Correcto", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show()
        }

        // Avanzar a la siguiente pregunta
        if(currentQuestionIndex < questions.size - 1){
            currentQuestionIndex++;
            showQuestion()
        }else{
            Toast.makeText(this, "!Has terminado el quiz", Toast.LENGTH_SHORT).show()
        }
    }
}