package com.example.saludmovil;

import android.database.Cursor;
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

public class PerfilPacienteActivity extends AppCompatActivity {
    // Declaramos las variables para todos los campos y el DNI
    TextInputEditText edEstatura, edPeso, edSangre, edSexo, edAlergias, edEnfermedades, edMedicamentos, edContactoNombre, edContactoTelefono;
    Button btnGuardar;
    private String usuario_dni;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil_paciente);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Vinculamos componentes del XML
        edEstatura = findViewById(R.id.editTextPerfilEstatura);
        edPeso = findViewById(R.id.editTextPerfilPeso);
        edSangre = findViewById(R.id.editTextPerfilSangre);
        edSexo = findViewById(R.id.editTextPerfilSexo);
        edAlergias = findViewById(R.id.editTextPerfilAlergias);
        edEnfermedades = findViewById(R.id.editTextPerfilEnfermedades);
        edMedicamentos = findViewById(R.id.editTextPerfilMedicamentos);
        edContactoNombre = findViewById(R.id.editTextPerfilContactoNombre);
        edContactoTelefono = findViewById(R.id.editTextPerfilContactoTelefono);
        btnGuardar = findViewById(R.id.buttonGuardarPerfil);

        // Recibimos el DNI que nos envio el inicio de sesión
        usuario_dni = getIntent().getStringExtra("usuario_dni");
        cargarDatosDelPerfil();

        // Configuramos la logica para el boton de Guardar Perfil

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Obtenemos los textos de cada campo
                String estatura = edEstatura.getText().toString();
                String peso = edPeso.getText().toString();
                String sangre = edSangre.getText().toString();
                String sexo = edSexo.getText().toString();
                String alergias = edAlergias.getText().toString();
                String enfermedades = edEnfermedades.getText().toString();
                String medicamentos = edMedicamentos.getText().toString();
                String contactoNombre = edContactoNombre.getText().toString();
                String contactoTelefono = edContactoTelefono.getText().toString();

                // Validamos solo los campos obligatorios

                if (estatura.length() == 0 || peso.length() == 0 || sangre.length() == 0 || sexo.length() == 0){
                    Toast.makeText(getApplicationContext(), "Rellena los campos obligatorios", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Si la validacion esta bien, guardamos los datos en la base de datos
                BaseDeDatos bd = new BaseDeDatos(getApplicationContext());
                bd.actualizarPerfilPaciente(usuario_dni, estatura, peso, sangre, sexo, alergias, enfermedades, medicamentos, contactoNombre, contactoTelefono);

                // Ahora mostramos un mensaje de exito y cerramos la pantalla para volver al inicio

                Toast.makeText(getApplicationContext(), "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show();
                finish(); // Cerramos esta actividad y regresamos a la anterior
            }
        });

    }

    private void cargarDatosDelPerfil(){
        BaseDeDatos bd = new BaseDeDatos(getApplicationContext());
        Cursor cursor = bd.getPerfilPaciente(usuario_dni);

        // Si el cursor encuentra datos para este DNI...
        if (cursor != null && cursor.moveToFirst()) {
            // ...obtenemos los datos de cada columna y los ponemos en los campos de texto.
            edEstatura.setText(cursor.getString(cursor.getColumnIndexOrThrow("estatura")));
            edPeso.setText(cursor.getString(cursor.getColumnIndexOrThrow("peso")));
            edSangre.setText(cursor.getString(cursor.getColumnIndexOrThrow("tipo_sangre")));
            edSexo.setText(cursor.getString(cursor.getColumnIndexOrThrow("sexo")));
            edAlergias.setText(cursor.getString(cursor.getColumnIndexOrThrow("alergias")));
            edEnfermedades.setText(cursor.getString(cursor.getColumnIndexOrThrow("enfermedades_cronicas")));
            edMedicamentos.setText(cursor.getString(cursor.getColumnIndexOrThrow("medicamentos_actuales")));
            edContactoNombre.setText(cursor.getString(cursor.getColumnIndexOrThrow("nombre_contacto_emergencia")));
            edContactoTelefono.setText(cursor.getString(cursor.getColumnIndexOrThrow("telefono_contacto_emergencia")));

            cursor.close();
        }
        // Si no hay se encuentran datos, pues se mostraran vacios

    }
}