package com.example.saludmovil;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class RegistrarDoctorPaso2Activity extends AppCompatActivity {

    TextInputEditText edEspecialidad, edCMP, edUniversidad;
    Button btnFinalizar;
    ImageButton btnAtras;
    private boolean hayCambiosSinGuardar = false;

    private String nombrePaso1, dniPaso1, fechaNacimientoPaso1, telefonoPaso1, correoPaso1, clavePaso1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_doctor_paso2);

        edCMP = findViewById(R.id.editTextRegDocCMP);
        edEspecialidad = findViewById(R.id.editTextRegDocEspecialidad);
        edUniversidad = findViewById(R.id.editTextRegDocUniversidad);
        btnFinalizar = findViewById(R.id.buttonFinalizarRegistro);
        btnAtras = findViewById(R.id.buttonAtrasRegDoc2);

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

        edCMP.addTextChangedListener(textWatcher);
        edEspecialidad.addTextChangedListener(textWatcher);
        edUniversidad.addTextChangedListener(textWatcher);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!hayCambiosSinGuardar) {
                    setEnabled(false);
                    onBackPressed();
                } else {
                    new AlertDialog.Builder(RegistrarDoctorPaso2Activity.this)
                            .setTitle("Descartar Cambios")
                            .setMessage("¿Estás seguro de que quieres salir? Los cambios que realizaste no se guardarán.")
                            .setPositiveButton("Descartar", (dialog, which) -> finish())
                            .setNegativeButton("Cancelar", null)
                            .show();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        btnAtras.setOnClickListener(v -> onBackPressed());

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            nombrePaso1 = extras.getString("nombre");
            dniPaso1 = extras.getString("dni");
            fechaNacimientoPaso1 = extras.getString("fechaNacimiento");
            telefonoPaso1 = extras.getString("telefono");
            correoPaso1 = extras.getString("correo");
            clavePaso1 = extras.getString("clave");
        }

        btnFinalizar.setOnClickListener(v -> {
            String cmp = edCMP.getText().toString();
            String especialidad = edEspecialidad.getText().toString();
            String universidad = edUniversidad.getText().toString();

            if (cmp.isEmpty() || especialidad.isEmpty() || universidad.isEmpty()){
                Toast.makeText(RegistrarDoctorPaso2Activity.this, "Llene todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            BaseDeDatos bd = new BaseDeDatos(getApplicationContext());
            bd.registrarDoctor(cmp, clavePaso1, nombrePaso1, dniPaso1, fechaNacimientoPaso1, telefonoPaso1, correoPaso1, especialidad, universidad);

            hayCambiosSinGuardar = false;

            Toast.makeText(getApplicationContext(), "Registro de doctor exitoso", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegistrarDoctorPaso2Activity.this, LoginDoctorActivity.class);
            startActivity(intent);
        });
    }
}