package com.example.saludmovil;

import android.content.Intent;
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

public class RegistrarDoctorPaso2Activity extends AppCompatActivity {

    // Declaramos las variables para los componentes de esta interfaz
    TextInputEditText edEspecialidad, edCMP, edUniversidad;
    Button btnFinalizar;
    // Ahora tenemos que guardar las variables que vienen del paso 1

    private String nombrePaso1, dniPaso1, fechaNacimientoPaso1, telefonoPaso1, correoPaso1, clavePaso1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar_doctor_paso2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Vinculamos componentes del XML
        edCMP = findViewById(R.id.editTextRegDocCMP);
        edEspecialidad = findViewById(R.id.editTextRegDocEspecialidad);
        edUniversidad = findViewById(R.id.editTextRegDocUniversidad);
        btnFinalizar = findViewById(R.id.buttonFinalizarRegistro);

        // Recibimos el paquete (Bundle) que viene con el Intent del paso 1
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            // Sacamos los datos del Bundle
            nombrePaso1 = extras.getString("nombre");
            dniPaso1 = extras.getString("dni");
            fechaNacimientoPaso1 = extras.getString("fechaNacimiento");
            telefonoPaso1 = extras.getString("telefono");
            correoPaso1 = extras.getString("correo");
            clavePaso1 = extras.getString("clave");
        }

        // Configuramos el listener para finalizar el registro

        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenemos los datos de esta pantalla
                String cmp = edCMP.getText().toString();
                String especialidad = edEspecialidad.getText().toString();
                String universidad = edUniversidad.getText().toString();

                // Validamos que los campos del Paso 2 no estén vacíos

                if (cmp.length() == 0 || especialidad.length()==0|| universidad.length()==0){
                    Toast.makeText(RegistrarDoctorPaso2Activity.this, "Llene todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Si esta ok guardamos los datos en la base de datos

                BaseDeDatos bd = new BaseDeDatos(getApplicationContext());

                // Aca escribiremos el metodo registrarDoctor pero saldra error porque aun no lo creamos en la clase BaseDeDatos

                bd.registrarDoctor(cmp, clavePaso1, nombrePaso1, dniPaso1, fechaNacimientoPaso1, telefonoPaso1, correoPaso1, especialidad, universidad);

                // Mostramos un mensaje de éxito y redirigimos al login doctor

                Toast.makeText(getApplicationContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegistrarDoctorPaso2Activity.this, LoginDoctorActivity.class);
                startActivity(intent);
            }
        });

    }
}