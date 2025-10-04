package com.example.saludmovil;

import static com.example.saludmovil.Validaciones.esValido;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class RegistrarActivity extends AppCompatActivity {

    // 1. Declaramos todas las vistas, incluyendo el nuevo ImageButton
    TextInputEditText edDNI, edNombre, edApellido, edFechaNacimiento, edClave, edConfirmarClave;
    Button btnRegistrar;
    TextView tvPacienteExistente;
    ImageButton btnAtras;

    // 2. Bandera para rastrear si el usuario ha hecho cambios
    private boolean hayCambiosSinGuardar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        // 3. Vinculamos todas las vistas del XML
        edDNI = findViewById(R.id.editTextRegDNI);
        edNombre = findViewById(R.id.editTextRegNombre);
        edApellido = findViewById(R.id.editTextRegApellido);
        edFechaNacimiento = findViewById(R.id.editTextRegFechaNacimiento);
        edClave = findViewById(R.id.editTextRegClave);
        edConfirmarClave = findViewById(R.id.editTextRegConfirmarClave);
        btnRegistrar = findViewById(R.id.buttonRegistrar);
        tvPacienteExistente = findViewById(R.id.textViewPacienteExistente);
        btnAtras = findViewById(R.id.buttonAtrasRegistro); // Vinculamos el nuevo botón

        // --- LÓGICA PARA DETECTAR CAMBIOS ---
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Si el texto cambia en cualquier campo, activamos la bandera
                hayCambiosSinGuardar = true;
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };

        // Aplicamos el "oyente" a todos los campos del formulario
        edDNI.addTextChangedListener(textWatcher);
        edNombre.addTextChangedListener(textWatcher);
        edApellido.addTextChangedListener(textWatcher);
        edFechaNacimiento.addTextChangedListener(textWatcher);
        edClave.addTextChangedListener(textWatcher);
        edConfirmarClave.addTextChangedListener(textWatcher);
        // ------------------------------------


        // --- LÓGICA DEL BOTÓN "ATRÁS" (MÉTODO MODERNO) ---
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!hayCambiosSinGuardar) {
                    // Si no hay cambios, deshabilita este callback y permite que el sistema
                    // maneje el retroceso de forma normal (cierra la actividad).
                    setEnabled(false);
                    onBackPressed();
                } else {
                    // Si hay cambios, muestra el diálogo
                    new AlertDialog.Builder(RegistrarActivity.this)
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


        // La lógica para ir al Login sigue igual
        tvPacienteExistente.setOnClickListener(view -> startActivity(new Intent(RegistrarActivity.this, LoginActivity.class)));

        // La lógica del calendario sigue igual
        edFechaNacimiento.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int anio = c.get(Calendar.YEAR);
            int mes = c.get(Calendar.MONTH);
            int dia = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(RegistrarActivity.this, (view, year, monthOfYear, dayOfMonth) -> edFechaNacimiento.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year), anio, mes, dia);
            datePickerDialog.show();
        });


        // Lógica del botón "Registrarse"
        btnRegistrar.setOnClickListener(view -> {
            String dni = edDNI.getText().toString();
            String nombre = edNombre.getText().toString();
            String apellido = edApellido.getText().toString();
            String fechaNacimiento = edFechaNacimiento.getText().toString();
            String clave = edClave.getText().toString();
            String confirmarClave = edConfirmarClave.getText().toString();
            BaseDeDatos bd = new BaseDeDatos(getApplicationContext());

            if (dni.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || fechaNacimiento.isEmpty() || clave.isEmpty() || confirmarClave.isEmpty()){
                Toast.makeText(getApplicationContext(), "Llene todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                if (clave.equals(confirmarClave)){
                    if (esValido(clave)){
                        bd.registrar(dni, nombre, apellido, fechaNacimiento, clave);

                        // IMPORTANTE: Al registrar, ya no hay cambios sin guardar
                        hayCambiosSinGuardar = false;

                        Toast.makeText(getApplicationContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegistrarActivity.this, LoginActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "La contraseña debe tener minimo 8 caracteres, una letra, un numero y un caracter especial", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}