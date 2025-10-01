package com.example.saludmovil;

import static com.example.saludmovil.Validaciones.esValido;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;

public class RegistrarActivity extends AppCompatActivity {

    // 1. Declaramos las variables para los nuevos campos de texto
    TextInputEditText edDNI, edNombre, edApellido, edFechaNacimiento, edClave, edConfirmarClave;
    Button btnRegistrar;
    TextView tvPacienteExistente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar); // Esto carga tu nuevo diseño

        // 2. Vinculamos las variables con los nuevos IDs del diseño
        edDNI = findViewById(R.id.editTextRegDNI);
        edNombre = findViewById(R.id.editTextRegNombre);
        edApellido = findViewById(R.id.editTextRegApellido);
        edFechaNacimiento = findViewById(R.id.editTextRegFechaNacimiento);
        edClave = findViewById(R.id.editTextRegClave);
        edConfirmarClave = findViewById(R.id.editTextRegConfirmarClave);
        btnRegistrar = findViewById(R.id.buttonRegistrar);
        tvPacienteExistente = findViewById(R.id.textViewPacienteExistente);

        // La lógica para ir al Login sigue igual
        tvPacienteExistente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrarActivity.this, LoginActivity.class));
            }
        });

        // Lógica para mostrar el calendario al hacer clic en el campo de fecha
        edFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int anio = c.get(Calendar.YEAR);
                int mes = c.get(Calendar.MONTH);
                int dia = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(RegistrarActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        edFechaNacimiento.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, anio, mes, dia);
                datePickerDialog.show();
            }
        });


        // 3. Lógica actualizada para el botón de "Registrarse"
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtenemos el texto de los nuevos campos
                String dni = edDNI.getText().toString();
                String nombre = edNombre.getText().toString();
                String apellido = edApellido.getText().toString();
                String fechaNacimiento = edFechaNacimiento.getText().toString();
                String clave = edClave.getText().toString();
                String confirmarClave = edConfirmarClave.getText().toString();
                BaseDeDatos bd = new BaseDeDatos(getApplicationContext());

                // Validamos que los nuevos campos no estén vacíos
                if (dni.length() == 0 || nombre.length() == 0 || apellido.length() == 0 || fechaNacimiento.length() == 0 || clave.length() == 0 || confirmarClave.length() == 0){
                    Toast.makeText(getApplicationContext(), "Llene todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    if (clave.equals(confirmarClave)){
                        if (esValido(clave)){
                            // Llamamos al método registrar con los nuevos parámetros
                            bd.registrar(dni, nombre, apellido, fechaNacimiento, clave);
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
            }
        });
    }
}