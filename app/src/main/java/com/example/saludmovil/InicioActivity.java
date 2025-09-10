package com.example.saludmovil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// Esta es la pantalla de inicio de nuestra aplicación.
public class InicioActivity extends AppCompatActivity {

    @Override
    // Esta función se ejecuta cuando se crea la pantalla.
    protected void onCreate(Bundle savedInstanceState) {
        // Llama a la función original para que la pantalla se inicie correctamente.
        super.onCreate(savedInstanceState);
        // Esto hace que la aplicación use todo el espacio de la pantalla, incluyendo las barras del sistema (donde están la hora y la batería).
        EdgeToEdge.enable(this);
        // Aquí le decimos a la aplicación qué diseño de pantalla debe usar (el archivo activity_inicio.xml).
        setContentView(R.layout.activity_inicio);
        // Esto se encarga de ajustar el contenido para que no se superponga con las barras del sistema, como la barra de notificaciones arriba.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Obtiene la información sobre las barras del sistema.
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Ajusta el espacio alrededor de la vista principal para que el contenido se vea bien.
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            // Devuelve los ajustes aplicados.
            return insets;
        });


        // Aquí guardamos datos simples, como un nombre de usuario, para poder usarlos en otras partes de la app.
        SharedPreferences sp = getSharedPreferences("datos", MODE_PRIVATE);
        // Obtiene el nombre del usuario guardado; si no hay, usa un espacio vacío.
        String usuario = sp.getString("usuario", "").toString();
        // Muestra un pequeño mensaje de bienvenida en la parte de abajo de la pantalla.
        Toast.makeText(getApplicationContext(), "Bienvenido " + usuario, Toast.LENGTH_SHORT).show();


        // Busca el botón "Salir" en el diseño de la pantalla.
        CardView salir = findViewById(R.id.cardSalir);
        // Configura qué hacer cuando el usuario toque el botón "Salir".
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            // Lo que pasa cuando se toca el botón.
            public void onClick(View view) {
                // Prepara una herramienta para borrar los datos guardados.
                SharedPreferences.Editor editor = sp.edit();
                // Borra toda la información guardada, como el nombre de usuario.
                editor.clear();
                // Confirma el borrado de la información.
                editor.apply();
                // Envía al usuario de vuelta a la pantalla de inicio de sesión.
                startActivity(new Intent(InicioActivity.this, LoginActivity.class));
            }
        });

        // Busca el botón de "Especialidades" en el diseño de la pantalla.
        CardView especialidades = findViewById(R.id.cardEspecialidades);
        // Configura qué hacer cuando el usuario toque el botón "Especialidades".
        especialidades.setOnClickListener(new View.OnClickListener(){
            @Override
            // Lo que pasa cuando se toca el botón.
            public void onClick(View view) {
                // Envía al usuario a la pantalla donde puede ver las especialidades médicas.
                startActivity(new Intent(InicioActivity.this, EspecialidadesActivity.class));
            }
        });
    }
}