package com.example.saludmovil;

import static com.example.saludmovil.Validaciones.esValido;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class RegistrarDoctorPaso1Activity extends AppCompatActivity {

    // 1. Declaramos todas las variables, incluyendo el botón de atrás y la bandera de cambios
    TextInputEditText edNombre, edDNI, edFechaNacimiento, edTelefono, edCorreo, edClave, edConfirmarClave;
    Button btnSiguiente;
    ImageButton btnAtras;
    private boolean hayCambiosSinGuardar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_doctor_paso1);

        // 2. Vinculamos todas las variables
        edNombre = findViewById(R.id.editTextRegDocNombre);
        edDNI = findViewById(R.id.editTextRegDocDNI);
        edFechaNacimiento = findViewById(R.id.editTextRegDocFechaNacimiento);
        edTelefono = findViewById(R.id.editTextRegDocTelefono);
        edCorreo = findViewById(R.id.editTextRegDocCorreo);
        edClave = findViewById(R.id.editTextRegDocClave);
        edConfirmarClave = findViewById(R.id.editTextRegDocConfirmarClave);
        btnSiguiente = findViewById(R.id.buttonRegDocSiguiente);
        btnAtras = findViewById(R.id.buttonAtrasRegDoc1);

        // --- LÓGICA PARA DETECTAR CAMBIOS ---
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hayCambiosSinGuardar = true; // Si el texto cambia, activamos la bandera
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };

        // Aplicamos el "oyente" a todos los campos del formulario
        edNombre.addTextChangedListener(textWatcher);
        edDNI.addTextChangedListener(textWatcher);
        edFechaNacimiento.addTextChangedListener(textWatcher);
        edTelefono.addTextChangedListener(textWatcher);
        edCorreo.addTextChangedListener(textWatcher);
        edClave.addTextChangedListener(textWatcher);
        edConfirmarClave.addTextChangedListener(textWatcher);
        // ------------------------------------

        // --- LÓGICA DEL BOTÓN "ATRÁS" (MÉTODO MODERNO) ---
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!hayCambiosSinGuardar) {
                    setEnabled(false);
                    onBackPressed();
                } else {
                    new AlertDialog.Builder(RegistrarDoctorPaso1Activity.this)
                            .setTitle("Descartar Cambios")
                            .setMessage("¿Estás seguro de que quieres salir? Los cambios que realizaste no se guardarán.")
                            .setPositiveButton("Descartar", (dialog, which) -> finish())
                            .setNegativeButton("Cancelar", null)
                            .show();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        btnAtras.setOnClickListener(v -> onBackPressed()); // El botón de la flecha ahora llama a esta nueva lógica
        // ------------------------------------------------

        // La lógica del calendario no cambia
        edFechaNacimiento.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int anio = c.get(Calendar.YEAR);
            int mes = c.get(Calendar.MONTH);
            int dia = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(RegistrarDoctorPaso1Activity.this, (view, year, monthOfYear, dayOfMonth) -> edFechaNacimiento.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year), anio, mes, dia);
            datePickerDialog.show();
        });


        btnSiguiente.setOnClickListener(v -> {
            String nombre = edNombre.getText().toString();
            String dni = edDNI.getText().toString();
            String fechaNacimiento = edFechaNacimiento.getText().toString();
            String telefono = edTelefono.getText().toString();
            String correo = edCorreo.getText().toString();
            String clave = edClave.getText().toString();
            String confirmarClave = edConfirmarClave.getText().toString();

            if (nombre.isEmpty() || dni.isEmpty() || fechaNacimiento.isEmpty() || telefono.isEmpty() || correo.isEmpty() || clave.isEmpty() || confirmarClave.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!clave.equals(confirmarClave)) {
                Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!esValido(clave)) {
                Toast.makeText(getApplicationContext(), "La contraseña debe tener mínimo 8 caracteres, una letra, un numero y un caracter especial", Toast.LENGTH_LONG).show();
                return;
            }

            // IMPORTANTE: Al pasar al siguiente paso, ya no hay cambios sin guardar
            hayCambiosSinGuardar = false;

            Intent intent = new Intent(RegistrarDoctorPaso1Activity.this, RegistrarDoctorPaso2Activity.class);
            intent.putExtra("nombre", nombre);
            intent.putExtra("dni", dni);
            intent.putExtra("fechaNacimiento", fechaNacimiento);
            intent.putExtra("telefono", telefono);
            intent.putExtra("correo", correo);
            intent.putExtra("clave", clave);
            startActivity(intent);
        });
    }
}