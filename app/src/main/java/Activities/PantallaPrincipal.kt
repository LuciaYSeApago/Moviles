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
import android.widget.Button
import android.widget.EditText
import com.example.myapplication.R

class PantallaPrincipal : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.pantallaprincipalbonito)

        // Variables a utilizar
        val si_soy: Button = findViewById(R.id.button_soy)
        val nombreEditText = findViewById<EditText>(R.id.nombre_u)

        si_soy.setOnClickListener {
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

