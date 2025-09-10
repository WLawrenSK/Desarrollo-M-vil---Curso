package com.example.saludmovil;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// Esta es la pantalla que muestra las especialidades médicas.
public class EspecialidadesActivity extends AppCompatActivity {

    @Override
    // Esta función se ejecuta cuando se crea la pantalla.
    protected void onCreate(Bundle savedInstanceState) {
        // Llama a la función original de Android para que la actividad se inicie correctamente.
        super.onCreate(savedInstanceState);
        // Permite que la aplicación use todo el espacio de la pantalla, incluyendo las áreas del sistema como la barra de estado.
        EdgeToEdge.enable(this);
        // Asigna el diseño de la pantalla (el archivo activity_especialidades.xml) a esta actividad.
        setContentView(R.layout.activity_especialidades);
        // Ajusta el diseño para que el contenido no quede oculto debajo de las barras del sistema.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Obtiene la información sobre el tamaño de las barras del sistema (superior, inferior, izquierda, derecha).
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Aplica un relleno al diseño principal para que no se superponga con las barras del sistema.
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            // Devuelve los ajustes de insets.
            return insets;
        });
    }
}