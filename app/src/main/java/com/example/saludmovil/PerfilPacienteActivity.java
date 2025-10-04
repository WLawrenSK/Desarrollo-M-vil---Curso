package com.example.saludmovil;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class PerfilPacienteActivity extends AppCompatActivity {

    // 1. Declaramos todas las vistas y variables necesarias
    TextInputEditText edEstatura, edPeso, edSangre, edSexo, edAlergias, edEnfermedades, edMedicamentos, edContactoNombre, edContactoTelefono;
    Button btnGuardar;
    ImageButton btnAtras;
    private String usuario_dni;
    private boolean hayCambiosSinGuardar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_paciente);

        // 2. Vinculamos todas las vistas del XML
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
        btnAtras = findViewById(R.id.buttonAtrasPerfil); // Asumiendo que le pondrás este ID al botón de atrás en el XML

        // 3. Recibimos el DNI que nos envió la InicioActivity
        usuario_dni = getIntent().getStringExtra("usuario_dni");

        // 4. Al iniciar, cargamos los datos que ya puedan existir
        cargarDatosDelPerfil();

        // 5. Lógica para detectar cambios en los campos de texto
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hayCambiosSinGuardar = true;
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };
        // Aplicamos el "oyente" a todos los campos
        edEstatura.addTextChangedListener(textWatcher);
        edPeso.addTextChangedListener(textWatcher);
        edSangre.addTextChangedListener(textWatcher);
        edSexo.addTextChangedListener(textWatcher);
        edAlergias.addTextChangedListener(textWatcher);
        edEnfermedades.addTextChangedListener(textWatcher);
        edMedicamentos.addTextChangedListener(textWatcher);
        edContactoNombre.addTextChangedListener(textWatcher);
        edContactoTelefono.addTextChangedListener(textWatcher);

        // 6. Lógica de retroceso con confirmación
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (hayCambiosSinGuardar) {
                    new AlertDialog.Builder(PerfilPacienteActivity.this)
                            .setTitle("Descartar Cambios")
                            .setMessage("¿Estás seguro de que quieres salir? Los cambios que realizaste no se guardarán.")
                            .setPositiveButton("Descartar", (dialog, which) -> finish())
                            .setNegativeButton("Cancelar", null)
                            .show();
                } else {
                    setEnabled(false);
                    onBackPressed();
                }
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);

        btnAtras.setOnClickListener(v -> onBackPressed());

        // 7. Lógica del botón "Guardar Perfil"
        btnGuardar.setOnClickListener(v -> {
            String estatura = edEstatura.getText().toString();
            String peso = edPeso.getText().toString();
            String sangre = edSangre.getText().toString();
            String sexo = edSexo.getText().toString();
            String alergias = edAlergias.getText().toString();
            String enfermedades = edEnfermedades.getText().toString();
            String medicamentos = edMedicamentos.getText().toString();
            String contactoNombre = edContactoNombre.getText().toString();
            String contactoTelefono = edContactoTelefono.getText().toString();

            if (estatura.isEmpty() || peso.isEmpty() || sangre.isEmpty() || sexo.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Por favor, complete los campos obligatorios (estatura, peso, tipo de sangre y sexo)", Toast.LENGTH_LONG).show();
                return;
            }

            BaseDeDatos bd = new BaseDeDatos(getApplicationContext());
            bd.actualizarPerfilPaciente(usuario_dni, estatura, peso, sangre, sexo, alergias, enfermedades, medicamentos, contactoNombre, contactoTelefono);

            hayCambiosSinGuardar = false;
            Toast.makeText(getApplicationContext(), "Perfil guardado exitosamente", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void cargarDatosDelPerfil() {
        BaseDeDatos bd = new BaseDeDatos(getApplicationContext());
        Cursor cursor = bd.getPerfilPaciente(usuario_dni);

        if (cursor != null && cursor.moveToFirst()) {
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
            // Después de cargar los datos, reseteamos la bandera para que el usuario tenga que cambiar algo para que se active
            hayCambiosSinGuardar = false;
        }
    }
}