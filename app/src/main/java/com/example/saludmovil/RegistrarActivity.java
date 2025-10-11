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

// Se importa MaterialToolbar y MaterialButton
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Locale;

public class RegistrarActivity extends AppCompatActivity {

    TextInputEditText edDNI, edNombre, edApellido, edFechaNacimiento, edCorreo, edClave, edConfirmarClave;
    Button btnRegistrar;

    // --- CORRECCIÓN: Se cambian los tipos de los componentes ---
    MaterialButton btnPacienteExistente;
    MaterialToolbar toolbar;

    private boolean hayCambiosSinGuardar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        // --- Vinculación con los IDs del layout mejorado ---
        toolbar = findViewById(R.id.toolbarRegistro);
        edDNI = findViewById(R.id.editTextRegDNI);
        edNombre = findViewById(R.id.editTextRegNombre);
        edApellido = findViewById(R.id.editTextRegApellido);
        edFechaNacimiento = findViewById(R.id.editTextRegFechaNacimiento);
        edCorreo = findViewById(R.id.editTextRegCorreo);
        edClave = findViewById(R.id.editTextRegClave);
        edConfirmarClave = findViewById(R.id.editTextRegConfirmarClave);
        btnRegistrar = findViewById(R.id.buttonRegistrar);
        btnPacienteExistente = findViewById(R.id.textViewPacienteExistente);

        // Configurar la Toolbar para que funcione como la barra de acción
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupChangeListeners();
        setupBackButton();

        btnPacienteExistente.setOnClickListener(view -> {
            // Se añade un flag para limpiar la pila de actividades si el usuario navega al login
            Intent intent = new Intent(RegistrarActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        edFechaNacimiento.setOnClickListener(v -> mostrarCalendario());

        // --- Lógica del botón "Registrarse" (sin cambios, ya era correcta) ---
        btnRegistrar.setOnClickListener(view -> {
            String dni = edDNI.getText().toString().trim();
            String nombre = edNombre.getText().toString().trim();
            String apellido = edApellido.getText().toString().trim();
            String fechaNacimiento = edFechaNacimiento.getText().toString().trim();
            String correo = edCorreo.getText().toString().trim();
            String clave = edClave.getText().toString().trim();
            String confirmarClave = edConfirmarClave.getText().toString().trim();

            if (dni.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || fechaNacimiento.isEmpty() || correo.isEmpty() || clave.isEmpty() || confirmarClave.isEmpty()){
                Toast.makeText(getApplicationContext(), "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!clave.equals(confirmarClave)){
                Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Validaciones.esValido(clave)){
                Toast.makeText(getApplicationContext(),
                        "La contraseña debe tener mínimo 8 caracteres, una letra, un número y un caracter especial", Toast.LENGTH_LONG).show();
                return;
            }

            BaseDeDatos bd = new BaseDeDatos(getApplicationContext());
            long idUsuario = bd.registrarUsuario(correo, clave, "paciente");

            if (idUsuario == -1) {
                Toast.makeText(getApplicationContext(), "El correo electrónico ya está en uso", Toast.LENGTH_SHORT).show();
            } else {
                bd.registrarPaciente(idUsuario, dni, nombre, apellido, fechaNacimiento);
                hayCambiosSinGuardar = false;
                Toast.makeText(getApplicationContext(), "¡Registro exitoso!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegistrarActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void mostrarCalendario() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                RegistrarActivity.this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    // --- CORRECCIÓN: Formato de fecha mejorado ---
                    String fechaFormateada = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, (monthOfYear + 1), year);
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
        edDNI.addTextChangedListener(textWatcher);
        edNombre.addTextChangedListener(textWatcher);
        edApellido.addTextChangedListener(textWatcher);
        edFechaNacimiento.addTextChangedListener(textWatcher);
        edCorreo.addTextChangedListener(textWatcher);
        edClave.addTextChangedListener(textWatcher);
        edConfirmarClave.addTextChangedListener(textWatcher);
    }

    private void setupBackButton() {
        // --- CORRECCIÓN: La acción de clic se asigna a la Toolbar ---
        toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (hayCambiosSinGuardar) {
                    new AlertDialog.Builder(RegistrarActivity.this)
                            .setTitle("Descartar Cambios")
                            .setMessage("¿Estás seguro de que quieres salir? Los cambios no se guardarán.")
                            .setPositiveButton("Salir", (dialog, which) -> finish())
                            .setNegativeButton("Cancelar", null)
                            .show();
                } else {
                    // Si no hay cambios, simplemente cierra la actividad
                    finish();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}