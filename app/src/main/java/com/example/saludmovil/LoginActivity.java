package com.example.saludmovil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    // Cambiamos el nombre de la variable para mayor claridad
    EditText edDni, edClave;
    Button btn;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ajustamos el ID al que corresponde en tu XML. Asumo que sigue siendo el mismo.
        edDni = findViewById(R.id.editTextLoginDni);
        edClave = findViewById(R.id.editTextLoginClave);
        btn = findViewById(R.id.buttonLogin);
        tv = findViewById(R.id.textViewNuevoUsuario);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtenemos el DNI y la clave
                String dni = edDni.getText().toString();
                String clave = edClave.getText().toString();
                BaseDeDatos bd = new BaseDeDatos(getApplicationContext());

                if (dni.length() == 0 || clave.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Llene todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    // Llamamos al método 'login' actualizado con el DNI
                    if (bd.login(dni, clave) == 1) {
                        Toast.makeText(getApplicationContext(), "Bienvenido", Toast.LENGTH_SHORT).show();

                        // Guardamos el DNI del paciente para usarlo en otras pantallas
                        SharedPreferences sp = getSharedPreferences("datos", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("usuario_dni", dni); // Guardamos el DNI
                        editor.apply();

                        startActivity(new Intent(LoginActivity.this, InicioActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "DNI o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrarActivity.class));
            }
        });
    }
}