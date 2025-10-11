package com.example.saludmovil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText edCorreo, edClave;
    MaterialButton btnLogin, btnNuevoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Vinculamos con los IDs del XML (usando los nuevos IDs)
        edCorreo = findViewById(R.id.editTextLoginCorreo);
        edClave = findViewById(R.id.editTextLoginClave);
        btnLogin = findViewById(R.id.buttonLogin);
        btnNuevoUsuario = findViewById(R.id.textViewNuevoUsuario);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = edCorreo.getText().toString();
                String clave = edClave.getText().toString();
                BaseDeDatos bd = new BaseDeDatos(getApplicationContext());

                if (correo.length() == 0 || clave.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                Cursor cursor = bd.login(correo, clave);

                if (cursor != null && cursor.moveToFirst()) { // Si el cursor tiene resultados, el login es exitoso
                    // Obtenemos los datos del usuario desde el cursor
                    int idIndex = cursor.getColumnIndex("id");
                    int rolIndex = cursor.getColumnIndex("rol");

                    int usuarioId = cursor.getInt(idIndex);
                    String rol = cursor.getString(rolIndex);

                    // Cerramos el cursor lo antes posible
                    cursor.close();

                    // Guardamos los datos importantes del usuario para usarlos en la app
                    SharedPreferences sp = getSharedPreferences("datos_usuario", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("id_usuario", usuarioId);
                    editor.putString("rol_usuario", rol);
                    editor.apply();

                    Toast.makeText(getApplicationContext(), "¡Bienvenido!", Toast.LENGTH_SHORT).show();

                    // Redirigimos al usuario a su pantalla de inicio según su rol
                    if (rol.equals("paciente")) {
                        startActivity(new Intent(LoginActivity.this, InicioActivity.class));
                    } else if (rol.equals("doctor")) {
                        startActivity(new Intent(LoginActivity.this, InicioDoctorActivity.class));
                    }
                    finish(); // Cerramos LoginActivity para que no pueda volver con el botón "atrás"

                } else {
                    // Si el cursor está vacío, las credenciales son incorrectas
                    Toast.makeText(getApplicationContext(), "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    if(cursor != null) {
                        cursor.close(); // Nos aseguramos de cerrar el cursor también en caso de error
                    }
                }
            }
        });

        btnNuevoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lo ideal es que este botón te lleve a la pantalla de Roles
                startActivity(new Intent(LoginActivity.this, RegistrarActivity.class));
            }
        });
    }
}