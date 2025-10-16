package com.example.saludmovil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class AgendarCitaActivity extends AppCompatActivity {

    // Declaramos los componentes de la UI para acceder a ellos fácilmente
    private AutoCompleteTextView autoCompleteEspecialidad;
    private TextView tvFechaSeleccionada;
    private ChipGroup chipGroupHorarios;
    private TextInputEditText etSintomas;

    // Usaremos una variable para verificar si se ha seleccionado una fecha
    private long fechaSeleccionadaTimestamp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_cita);

        // --- Inicialización de Vistas ---
        autoCompleteEspecialidad = findViewById(R.id.autoCompleteEspecialidad);
        tvFechaSeleccionada = findViewById(R.id.tvFechaSeleccionada);
        chipGroupHorarios = findViewById(R.id.chipGroupHorarios);
        etSintomas = findViewById(R.id.etSintomas);
        Button btnConfirmarCita = findViewById(R.id.btnConfirmarCita);
        Button btnSeleccionarFecha = findViewById(R.id.btnSeleccionarFecha);
        MaterialToolbar toolbar = findViewById(R.id.toolbarAgendar);

        // --- Configuración de la Toolbar ---
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // --- Configuración del Dropdown de Especialidades ---
        String[] especialidades = {"Medicina General", "Cardiología", "Pediatría", "Dermatología", "Ginecología"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, especialidades);
        autoCompleteEspecialidad.setAdapter(adapter);

        // --- Configuración del Selector de Fecha ---
        btnSeleccionarFecha.setOnClickListener(v -> {
            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
            builder.setTitleText("Selecciona una fecha");
            final MaterialDatePicker<Long> datePicker = builder.build();
            datePicker.show(getSupportFragmentManager(), "DATE_PICKER");

            datePicker.addOnPositiveButtonClickListener(selection -> {
                fechaSeleccionadaTimestamp = selection; // Guardamos la fecha seleccionada
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                String fechaFormateada = sdf.format(selection);
                tvFechaSeleccionada.setText(fechaFormateada);
            });
        });

        // --- Configuración del botón de Confirmación con Validación ---
        btnConfirmarCita.setOnClickListener(v -> {
            // Solo si la validación es exitosa, procedemos a confirmar la cita
            if (validarCampos()) {
                confirmarCita();
            }
        });
    }

    /**
     * Este método verifica que todos los campos necesarios estén llenos.
     * @return 'true' si todo es correcto, 'false' si falta algún campo.
     */
    private boolean validarCampos() {
        // 1. Validar Especialidad
        if (TextUtils.isEmpty(autoCompleteEspecialidad.getText().toString())) {
            Toast.makeText(this, "Por favor, selecciona una especialidad", Toast.LENGTH_SHORT).show();
            autoCompleteEspecialidad.setError("Campo requerido");
            return false;
        }

        // 2. Validar Fecha
        if (fechaSeleccionadaTimestamp == 0) {
            Toast.makeText(this, "Por favor, selecciona una fecha para tu cita", Toast.LENGTH_SHORT).show();
            return false;
        }

        // 3. Validar Horario
        if (chipGroupHorarios.getCheckedChipId() == -1) { // -1 significa que ningún chip está seleccionado
            Toast.makeText(this, "Por favor, elige un horario disponible", Toast.LENGTH_SHORT).show();
            return false;
        }

        // 4. Validar Síntomas
        if (TextUtils.isEmpty(etSintomas.getText().toString().trim())) {
            Toast.makeText(this, "Por favor, describe tus síntomas", Toast.LENGTH_SHORT).show();
            etSintomas.setError("Campo requerido");
            return false;
        }

        // Si todas las validaciones anteriores pasaron, devolvemos true
        return true;
    }

    /**
     * Este método se ejecuta cuando la cita es válida y se va a registrar.
     */
    private void confirmarCita() {
        Toast.makeText(this, "Su cita médica se ha registrado con éxito", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(AgendarCitaActivity.this, InicioActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}