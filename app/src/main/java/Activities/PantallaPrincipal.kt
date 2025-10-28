package Activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import Activities.ui.theme.MyApplicationTheme
import android.content.Intent
import android.media.AudioAttributes
import android.media.SoundPool
import android.widget.Button
import android.widget.EditText
import com.example.myapplication.R

private lateinit var soundPool: SoundPool
private var sonidoClick = 0
class PantallaPrincipal : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.pantallaprincipalbonito)

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        sonidoClick = soundPool.load(this, R.raw.click, 1)

        // Variables a utilizar
        val si_soy: Button = findViewById(R.id.button_soy)
        val nombreEditText = findViewById<EditText>(R.id.nombre_u)

        si_soy.setOnClickListener {
            soundPool.play(sonidoClick, 0.5f, 0.5f, 1, 0, 1f)
            val nombre = nombreEditText.text.toString().trim()

            if(nombre.isEmpty()){
                nombreEditText.error = "Por favor, escribe un nombre"
                nombreEditText.requestFocus()
            }else{
                val intent: Intent = Intent(this, PantallaAjustes::class.java)
                intent.putExtra("nombreUsuario", nombre)
                startActivity(intent)
            }


        }
    }
}

