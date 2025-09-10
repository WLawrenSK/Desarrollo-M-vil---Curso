package com.example.saludmovil;

import android.content.Intent;
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

// Esta es la pantalla donde los nuevos usuarios pueden registrarse.
public class RegistrarActivity extends AppCompatActivity {
    // Declaración de las variables para los campos de texto y los botones.
    EditText edUsuario, edClave, edCorreo, edConfirmarClave;
    Button btn;
    TextView tv;

    @Override
    // Función que se ejecuta al crear la pantalla.
    protected void onCreate(Bundle savedInstanceState) {
        // Llama a la función original de Android para la creación de la actividad.
        super.onCreate(savedInstanceState);
        // Habilita el modo para usar toda la pantalla, incluyendo la barra de notificaciones.
        EdgeToEdge.enable(this);
        // Establece el diseño de la pantalla (activity_registrar.xml) para esta actividad.
        setContentView(R.layout.activity_registrar);
        // Ajusta los márgenes para que el contenido no quede debajo de las barras del sistema (como la de notificaciones).
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Obtiene la información sobre el tamaño de las barras del sistema.
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Aplica el relleno para que el contenido se ajuste correctamente.
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            // Devuelve los ajustes.
            return insets;
        });
        // Vincula las variables con los elementos del diseño de la pantalla usando sus IDs.
        edUsuario = findViewById(R.id.editTextRegUsuario);
        edClave = findViewById(R.id.editTextRegClave);
        edCorreo = findViewById(R.id.editTextRegCorreo);
        edConfirmarClave = findViewById(R.id.editTextRegConfirmarClave);
        btn = findViewById(R.id.buttonRegistrar);
        tv = findViewById(R.id.textViewUsuarioExistente);

        // Configura la acción para el texto "Ya soy usuario".
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            // Lo que sucede al hacer clic.
            public void onClick(View view) {
                // Inicia la pantalla de inicio de sesión.
                startActivity(new Intent(RegistrarActivity.this, LoginActivity.class));
            }
        });

        // Configura la acción para el botón de "Registrarse".
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            // Lo que sucede al hacer clic.
            public void onClick(View view) {
                // Obtiene el texto ingresado por el usuario en cada campo.
                String usuario = edUsuario.getText().toString();
                String correo = edCorreo.getText().toString();
                String clave = edClave.getText().toString();
                String confirmarClave = edConfirmarClave.getText().toString();
                // Crea una instancia de la base de datos para guardar los datos.
                BaseDeDatos bd = new BaseDeDatos(getApplicationContext(), "saludmovil", null, 1);
                // Verifica si algún campo está vacío.
                if (usuario.length() == 0 || correo.length() == 0 || clave.length() == 0 || confirmarClave.length() == 0){
                    // Muestra un mensaje si hay campos vacíos.
                    Toast.makeText(getApplicationContext(), "Llene todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    // Verifica si la contraseña y la confirmación de la contraseña son iguales.
                    if (clave.compareTo(confirmarClave)==0){
                        // Verifica si la contraseña cumple con los requisitos de seguridad.
                        if (esValido(clave)){
                            // Si todo es correcto, registra al usuario en la base de datos.
                            bd.registrar(usuario, correo, clave);
                            // Muestra un mensaje de éxito.
                            Toast.makeText(getApplicationContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                            // Lleva al usuario a la pantalla de inicio de sesión.
                            startActivity(new Intent(RegistrarActivity.this, LoginActivity.class));
                        } else {
                            // Muestra un mensaje si la contraseña no cumple con los requisitos.
                            Toast.makeText(getApplicationContext(),
                                    "La contraseña debe tener minimo 8 caracteres, una letra, un numero y un caracter especial", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Muestra un mensaje si las contraseñas no coinciden.
                        Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    // Función para validar la complejidad de la contraseña.
    public static boolean esValido(String miClave){
        // Banderas para verificar si la contraseña contiene al menos una letra, un número y un caracter especial.
        int f1=0, f2=0, f3=0;
        // Verifica si la longitud de la contraseña es menor a 8 caracteres.
        if(miClave.length() < 8){
            return false;
        } else {
            // Recorre la contraseña para buscar una letra.
            for (int i = 0; i < miClave.length(); i++){
                if (Character.isLetter(miClave.charAt(i))){
                    f1 = 1; // Si encuentra una letra, cambia la bandera a 1.
                }
            }
            // Recorre la contraseña para buscar un número.
            for (int r = 0; r < miClave.length(); r++){
                if (Character.isDigit(miClave.charAt(r))){
                    f2 = 1; // Si encuentra un número, cambia la bandera a 1.
                }
            }
            // Recorre la contraseña para buscar un caracter especial.
            for (int p = 0; p < miClave.length(); p++){
                char c = miClave.charAt(p);
                // Comprueba si el caracter es un caracter especial usando su código ASCII.
                if (c >= 33 && c <= 46 || c == 64){
                    f3 = 1; // Si encuentra un caracter especial, cambia la bandera a 1.
                }
            }
            // Si las tres banderas son 1, significa que la contraseña es válida.
            if (f1 == 1 && f2 == 1 && f3 == 1)
                return true;
            return false; // Si alguna bandera no es 1, la contraseña no es válida.
        }
    }
}