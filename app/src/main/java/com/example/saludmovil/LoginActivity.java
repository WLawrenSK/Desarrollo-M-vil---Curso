package com.example.saludmovil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// Esta es la pantalla de inicio de sesión de la aplicación.
public class LoginActivity extends AppCompatActivity {
    // Declara las variables para los campos de texto, el botón y el texto para "Nuevo Usuario".
    EditText edUsuario, edClave;
    Button btn;
    TextView tv;
    @Override
    // Se ejecuta cuando se crea la pantalla de inicio de sesión.
    protected void onCreate(Bundle savedInstanceState) {
        // Llama a la función original para que la pantalla se inicie correctamente.
        super.onCreate(savedInstanceState);
        // Permite que la aplicación ocupe toda la pantalla, incluyendo la zona de la barra de notificaciones.
        EdgeToEdge.enable(this);
        // Asigna el diseño de la pantalla (activity_login.xml) a esta actividad.
        setContentView(R.layout.activity_login);
        // Ajusta el diseño para que el contenido no se superponga con las barras del sistema (como la de notificaciones).
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Obtiene las dimensiones de las barras del sistema.
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Aplica un relleno para que el contenido principal no quede debajo de las barras del sistema.
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            // Retorna los ajustes.
            return insets;
        });
        // Vincula las variables con los elementos del diseño de la pantalla.
        edUsuario = findViewById(R.id.editTextLoginUsuario);
        edClave = findViewById(R.id.editTextLoginClave);
        btn = findViewById(R.id.buttonLogin);
        tv = findViewById(R.id.textViewNuevoUsuario);

        // Configura qué sucede cuando se hace clic en el botón de inicio de sesión.
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            // La acción que se realiza al hacer clic.
            public void onClick(View view) {
                // Obtiene el texto que el usuario ingresó en los campos de usuario y clave.
                String usuario = edUsuario.getText().toString();
                String clave = edClave.getText().toString();
                // Crea una instancia de nuestra base de datos para poder verificar los datos.
                BaseDeDatos bd = new BaseDeDatos(getApplicationContext(), "saludmovil", null, 1);

                // Comprueba si el usuario o la clave están vacíos.
                if (usuario.length() == 0 || clave.length() == 0){
                    // Muestra un mensaje de advertencia si los campos están vacíos.
                    Toast.makeText(getApplicationContext(), "Llene todos los campos", Toast.LENGTH_SHORT).show();
                    return; // Detiene la ejecución para que no se intente iniciar sesión.
                } else {
                    // Si los campos están llenos, intenta iniciar sesión usando la base de datos.
                    if(bd.login(usuario, clave) == 1){
                        // Muestra un mensaje de bienvenida si el inicio de sesión es exitoso.
                        Toast.makeText(getApplicationContext(), "Bienvenido", Toast.LENGTH_SHORT).show();

                        // Guarda el nombre de usuario para recordarlo en otras pantallas de la app.
                        SharedPreferences sp = getSharedPreferences("datos", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("usuario", usuario);
                        editor.apply();

                        // Inicia la siguiente pantalla, que es la de inicio.
                        startActivity(new Intent(LoginActivity.this, InicioActivity.class));
                    } else {
                        // Muestra un mensaje de error si el usuario o la clave son incorrectos.
                        Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        // Configura qué sucede cuando se hace clic en el texto de "Nuevo Usuario".
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            // La acción que se realiza al hacer clic.
            public void onClick(View view) {
                // Inicia la pantalla de registro para que el usuario pueda crear una cuenta nueva.
                startActivity(new Intent(LoginActivity.this, RegistrarActivity.class));
            }
        });
    }
}