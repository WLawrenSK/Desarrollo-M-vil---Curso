package com.example.saludmovil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginDoctorActivity extends AppCompatActivity {

    TextInputEditText edCorreo, edClave;
    MaterialButton btnLogin, btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_doctor);

        // Vinculamos con los IDs del layout
        edCorreo = findViewById(R.id.editTextDoctorCorreo);
        edClave = findViewById(R.id.editTextDoctorClave);
        btnLogin = findViewById(R.id.buttonLoginDoctor);
        btnRegistrar = findViewById(R.id.textViewNuevoDoctor);

        // El botón de registrar sigue funcionando igual
        btnRegistrar.setOnClickListener(v -> {
            Intent intent = new Intent(LoginDoctorActivity.this, RegistrarDoctorPaso1Activity.class);
            startActivity(intent);
        });

        // --- LÓGICA DE LOGIN SIMPLIFICADA ---
        btnLogin.setOnClickListener(v -> {
            String correo = edCorreo.getText().toString().trim();
            String clave = edClave.getText().toString().trim();

            // 1. Verificar únicamente que los campos no estén vacíos
            if (correo.isEmpty() || clave.isEmpty()){
                Toast.makeText(getApplicationContext(), "Llene todos los campos", Toast.LENGTH_SHORT).show();
                return; // Detiene la ejecución si están vacíos
            }

            // 2. Si no están vacíos, permitir el ingreso directamente
            Toast.makeText(getApplicationContext(), "Bienvenido, Doctor(a)", Toast.LENGTH_SHORT).show();

            // Navegar a la pantalla principal del doctor
            Intent intent = new Intent(LoginDoctorActivity.this, InicioDoctorActivity.class);
            startActivity(intent);
            finish(); // Cierra la actividad de login para que no pueda volver
        });
    }
}