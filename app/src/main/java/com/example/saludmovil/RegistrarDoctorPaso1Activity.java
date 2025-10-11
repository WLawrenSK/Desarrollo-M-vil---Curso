package com.example.saludmovil;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class RegistrarDoctorPaso1Activity extends AppCompatActivity {

    TextInputEditText edNombre, edDNI, edFechaNacimiento, edTelefono, edCorreo, edClave, edConfirmarClave;
    Button btnSiguiente;
    MaterialToolbar toolbar; // Se usa la Toolbar del nuevo diseño
    private boolean hayCambiosSinGuardar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_doctor_paso1);

        // Enlazar componentes del nuevo layout
        toolbar = findViewById(R.id.toolbarRegDoc1);
        edNombre = findViewById(R.id.editTextRegDocNombre);
        edDNI = findViewById(R.id.editTextRegDocDNI);
        edFechaNacimiento = findViewById(R.id.editTextRegDocFechaNacimiento);
        edTelefono = findViewById(R.id.editTextRegDocTelefono);
        edCorreo = findViewById(R.id.editTextRegDocCorreo);
        edClave = findViewById(R.id.editTextRegDocClave);
        edConfirmarClave = findViewById(R.id.editTextRegDocConfirmarClave);
        btnSiguiente = findViewById(R.id.buttonRegDocSiguiente);

        // Configurar la Toolbar para que tenga el botón de "Atrás"
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupChangeListeners();
        setupBackButton();

        edFechaNacimiento.setOnClickListener(v -> mostrarCalendario());

        // Lógica del botón "Siguiente" SIN BASE DE DATOS
        btnSiguiente.setOnClickListener(v -> {
            String nombre = edNombre.getText().toString().trim();
            String dni = edDNI.getText().toString().trim();
            String fechaNacimiento = edFechaNacimiento.getText().toString().trim();
            String telefono = edTelefono.getText().toString().trim();
            String correo = edCorreo.getText().toString().trim();
            String clave = edClave.getText().toString().trim();
            String confirmarClave = edConfirmarClave.getText().toString().trim();

            if (nombre.isEmpty() || dni.isEmpty() || fechaNacimiento.isEmpty() || telefono.isEmpty() || correo.isEmpty() || clave.isEmpty() || confirmarClave.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!clave.equals(confirmarClave)) {
                Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }
            // Puedes mantener otras validaciones si quieres
            // if (!Validaciones.esValido(clave)) { ... }

            // Navegación simulada: solo vamos a la siguiente actividad
            Toast.makeText(getApplicationContext(), "Datos validados, pasando al siguiente paso...", Toast.LENGTH_SHORT).show();
            hayCambiosSinGuardar = false; // Marcamos como "guardado" para la alerta

            Intent intent = new Intent(RegistrarDoctorPaso1Activity.this, RegistrarDoctorPaso2Activity.class);
            startActivity(intent);
        });
    }

    private void mostrarCalendario() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                RegistrarDoctorPaso1Activity.this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    // Formato de fecha para que se vea bien
                    String fechaFormateada = String.format("%02d/%02d/%d", dayOfMonth, (monthOfYear + 1), year);
                    edFechaNacimiento.setText(fechaFormateada);
                },
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void setupChangeListeners() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { hayCambiosSinGuardar = true; }
            @Override public void afterTextChanged(Editable s) {}
        };
        edNombre.addTextChangedListener(textWatcher);
        edDNI.addTextChangedListener(textWatcher);
        edFechaNacimiento.addTextChangedListener(textWatcher);
        edTelefono.addTextChangedListener(textWatcher);
        edCorreo.addTextChangedListener(textWatcher);
        edClave.addTextChangedListener(textWatcher);
        edConfirmarClave.addTextChangedListener(textWatcher);
    }

    private void setupBackButton() {
        // Lógica para el botón de la Toolbar
        toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        // Lógica para el botón físico de "Atrás"
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (hayCambiosSinGuardar) {
                    new AlertDialog.Builder(RegistrarDoctorPaso1Activity.this)
                            .setTitle("Descartar Cambios")
                            .setMessage("¿Estás seguro de que quieres salir? Los datos ingresados se perderán.")
                            .setPositiveButton("Salir", (dialog, which) -> finish())
                            .setNegativeButton("Cancelar", null)
                            .show();
                } else {
                    finish(); // Si no hay cambios, simplemente cierra la actividad
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}