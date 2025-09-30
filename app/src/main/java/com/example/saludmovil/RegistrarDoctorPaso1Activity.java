package com.example.saludmovil;

import static com.example.saludmovil.Validaciones.esValido;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class RegistrarDoctorPaso1Activity extends AppCompatActivity {

    // 1. Declaramos las variables
    TextInputEditText edNombre, edDNI, edFechaNacimiento, edTelefono, edCorreo, edClave, edConfirmarClave;
    Button btnSiguiente;
    private int anio, mes, dia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_doctor_paso1);

        // 2. Vinculamos las variables
        edNombre = findViewById(R.id.editTextRegDocNombre);
        edDNI = findViewById(R.id.editTextRegDocDNI);
        edFechaNacimiento = findViewById(R.id.editTextRegDocFechaNacimiento);
        edTelefono = findViewById(R.id.editTextRegDocTelefono);
        edCorreo = findViewById(R.id.editTextRegDocCorreo);
        edClave = findViewById(R.id.editTextRegDocClave);
        edConfirmarClave = findViewById(R.id.editTextRegDocConfirmarClave);
        btnSiguiente = findViewById(R.id.buttonRegDocSiguiente);

        // --- CÓDIGO NUEVO PARA EL CALENDARIO ---
        edFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenemos la fecha actual para mostrarla en el calendario
                final Calendar c = Calendar.getInstance();
                anio = c.get(Calendar.YEAR);
                mes = c.get(Calendar.MONTH);
                dia = c.get(Calendar.DAY_OF_MONTH);

                // Creamos el diálogo del calendario
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegistrarDoctorPaso1Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Cuando el usuario selecciona una fecha, la ponemos en el campo de texto
                        edFechaNacimiento.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, anio, mes, dia);

                // Mostramos el calendario
                datePickerDialog.show();
            }
        });
        // --- FIN DEL CÓDIGO NUEVO ---


        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenemos el texto de cada campo
                String nombre = edNombre.getText().toString();
                String dni = edDNI.getText().toString();
                String fechaNacimiento = edFechaNacimiento.getText().toString(); // No olvides obtener el nuevo dato
                String telefono = edTelefono.getText().toString();
                String correo = edCorreo.getText().toString();
                String clave = edClave.getText().toString();
                String confirmarClave = edConfirmarClave.getText().toString();

                if (nombre.length() == 0 || dni.length() == 0 || fechaNacimiento.length() == 0 || telefono.length() == 0 || correo.length() == 0 || clave.length() == 0 || confirmarClave.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!clave.equals(confirmarClave)) {
                    Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!esValido(clave)) {
                    Toast.makeText(getApplicationContext(), "La contraseña debe tener mínimo 8 caracteres, una letra, un número y un caracter especial", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(RegistrarDoctorPaso1Activity.this, RegistrarDoctorPaso2Activity.class);

                intent.putExtra("nombre", nombre);
                intent.putExtra("dni", dni);
                intent.putExtra("fechaNacimiento", fechaNacimiento);
                intent.putExtra("telefono", telefono);
                intent.putExtra("correo", correo);
                intent.putExtra("clave", clave);

                startActivity(intent);
            }
        });
    }
}