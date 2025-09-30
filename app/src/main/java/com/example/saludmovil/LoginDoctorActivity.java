package com.example.saludmovil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class LoginDoctorActivity extends AppCompatActivity {
    // Vamos a declarar las variables de los componentes de la interfaz
    TextInputEditText edCamp, edClave;
    Button btnLogin;
    Button btnRegistrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_doctor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        // Vinculamos lo que declaramos con los id de los componentes de la interfaz
        edCamp = findViewById(R.id.editTextDoctorCmp);
        edClave = findViewById(R.id.editTextDoctorClave);
        btnLogin = findViewById(R.id.buttonLoginDoctor);
        btnRegistrar = findViewById(R.id.textViewNuevoDoctor);

        // Configuramos los listeners

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginDoctorActivity.this, RegistrarDoctorPaso1Activity.class);
                startActivity(intent);

            }
        });

        // Configuramos el listener para el botón de inicio de sesión

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cmp = edCamp.getText().toString();
                String clave = edClave.getText().toString();
                BaseDeDatos bd = new BaseDeDatos(getApplicationContext(), "saludmovil", null, 1);
                if (cmp.length() == 0 || clave.length()==0){
                    Toast.makeText(getApplicationContext(), "Llene todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    if (bd.loginDoctor(cmp, clave) == 1){
                        Toast.makeText(getApplicationContext(), "Bienvenido, Doctor(a)", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginDoctorActivity.this, InicioDoctorActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "CMP o clave incorrecta", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}