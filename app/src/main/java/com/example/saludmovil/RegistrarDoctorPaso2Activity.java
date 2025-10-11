package com.example.saludmovil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

public class RegistrarDoctorPaso2Activity extends AppCompatActivity {

    TextInputEditText edCMP;
    AutoCompleteTextView autoCompleteEspecialidad; // Componente correcto para el menú
    Button btnAdjuntarTitulo, btnFinalizar;
    TextView tvArchivoSeleccionado;
    MaterialToolbar toolbar;
    private boolean hayCambiosSinGuardar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_doctor_paso2);

        // Enlazar componentes del nuevo layout
        toolbar = findViewById(R.id.toolbarRegDoc2);
        edCMP = findViewById(R.id.editTextRegDocCMP);
        autoCompleteEspecialidad = findViewById(R.id.autoCompleteEspecialidad);
        btnAdjuntarTitulo = findViewById(R.id.btnAdjuntarTitulo);
        tvArchivoSeleccionado = findViewById(R.id.tvArchivoSeleccionado);
        btnFinalizar = findViewById(R.id.buttonFinalizarRegistro);

        // Configurar la Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Configurar la lógica de "Atrás"
        setupBackButton();
        setupChangeListeners();

        // --- Cargar las especialidades en el menú desplegable ---
        setupEspecialidades();

        // --- Lógica de botones SIN BASE DE DATOS ---
        btnAdjuntarTitulo.setOnClickListener(v -> {
            // Aquí iría la lógica para abrir el selector de archivos.
            // Por ahora, simulamos la selección.
            tvArchivoSeleccionado.setText("titulo_medico.pdf");
            hayCambiosSinGuardar = true;
            Toast.makeText(this, "Archivo seleccionado (simulación)", Toast.LENGTH_SHORT).show();
        });

        btnFinalizar.setOnClickListener(v -> {
            String cmp = edCMP.getText().toString().trim();
            String especialidad = autoCompleteEspecialidad.getText().toString().trim();
            String archivo = tvArchivoSeleccionado.getText().toString();

            if (cmp.isEmpty() || especialidad.isEmpty() || archivo.equals("Ningún archivo seleccionado")){
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Navegación simulada
            hayCambiosSinGuardar = false;
            Toast.makeText(getApplicationContext(), "¡Registro de doctor completado con éxito!", Toast.LENGTH_LONG).show();

            // Llevamos al doctor a la pantalla de login para que inicie sesión
            Intent intent = new Intent(RegistrarDoctorPaso2Activity.this, LoginDoctorActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish(); // Cierra esta actividad
        });
    }

    private void setupEspecialidades() {
        String[] especialidades = new String[] {
                "Medicina General", "Pediatría", "Cardiología", "Dermatología",
                "Ginecología y Obstetricia", "Odontología", "Psicología", "Nutrición"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, especialidades
        );
        autoCompleteEspecialidad.setAdapter(adapter);
    }

    private void setupChangeListeners() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { hayCambiosSinGuardar = true; }
            @Override public void afterTextChanged(Editable s) {}
        };
        edCMP.addTextChangedListener(textWatcher);
        autoCompleteEspecialidad.addTextChangedListener(textWatcher);
    }

    private void setupBackButton() {
        toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (hayCambiosSinGuardar) {
                    new AlertDialog.Builder(RegistrarDoctorPaso2Activity.this)
                            .setTitle("Descartar Cambios")
                            .setMessage("¿Estás seguro de que quieres salir? La información profesional se perderá.")
                            .setPositiveButton("Salir", (dialog, which) -> finish())
                            .setNegativeButton("Cancelar", null)
                            .show();
                } else {
                    finish();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}