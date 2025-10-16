package com.example.saludmovil;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout; // Importamos TextInputLayout
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class RegistrarActivity extends AppCompatActivity {

    // Vistas de UI
    TextInputEditText edDNI, edNombre, edApellido, edFechaNacimiento, edCorreo, edClave, edConfirmarClave;
    Button btnRegistrar;
    MaterialButton btnPacienteExistente, btnVerificarDNI;
    MaterialToolbar toolbar;

    // Layouts para poder habilitarlos/deshabilitarlos
    TextInputLayout layoutNombre, layoutApellido, layoutFecha, layoutCorreo, layoutClave, layoutConfirmar;

    private RequestQueue colaPeticiones;
    private boolean hayCambiosSinGuardar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        vincularVistas(); // Usamos un método para mantener onCreate limpio

        colaPeticiones = Volley.newRequestQueue(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        configurarEstadoInicialFormulario();

        setupChangeListeners();
        setupBackButton();
        setupClickListeners();
    }

    private void vincularVistas() {
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
        btnVerificarDNI = findViewById(R.id.buttonVerificarDNI);

        // Vinculamos los Layouts para poder deshabilitarlos
        layoutNombre = findViewById(R.id.textInputLayoutRegNombre);
        layoutApellido = findViewById(R.id.textInputLayoutRegApellido);
        layoutFecha = findViewById(R.id.textInputLayoutRegFecha);
        layoutCorreo = findViewById(R.id.textInputLayoutRegCorreo);
        layoutClave = findViewById(R.id.textInputLayoutRegClave);
        layoutConfirmar = findViewById(R.id.textInputLayoutRegConfirmarClave);
    }

    private void configurarEstadoInicialFormulario() {
        // Deshabilitamos todo excepto el DNI y el botón de verificar
        layoutNombre.setEnabled(false);
        layoutApellido.setEnabled(false);
        layoutFecha.setEnabled(false);
        layoutCorreo.setEnabled(false);
        layoutClave.setEnabled(false);
        layoutConfirmar.setEnabled(false);
        btnRegistrar.setEnabled(false);
    }

    private void habilitarFormularioPostVerificacion() {
        // Habilitamos el resto del formulario
        layoutFecha.setEnabled(true);
        layoutCorreo.setEnabled(true);
        layoutClave.setEnabled(true);
        layoutConfirmar.setEnabled(true);
        btnRegistrar.setEnabled(true);

        // Bloqueamos los campos ya verificados
        edDNI.setEnabled(false);
        btnVerificarDNI.setEnabled(false);
        layoutNombre.setEnabled(false); // Mantenemos el nombre bloqueado
        layoutApellido.setEnabled(false); // Mantenemos el apellido bloqueado
    }

    private void setupClickListeners(){
        btnPacienteExistente.setOnClickListener(view -> {
            Intent intent = new Intent(RegistrarActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        edFechaNacimiento.setOnClickListener(v -> mostrarCalendario());

        btnVerificarDNI.setOnClickListener(v -> {
            String dni = edDNI.getText().toString().trim();
            if (dni.length() == 8) {
                verificarDNIconAPI(dni);
            } else {
                Toast.makeText(this, "Por favor, ingrese un DNI de 8 dígitos.", Toast.LENGTH_SHORT).show();
            }
        });

        btnRegistrar.setOnClickListener(view -> {
            // Recolectamos todos los datos
            String dni = edDNI.getText().toString().trim();
            String nombre = edNombre.getText().toString().trim();
            String apellido = edApellido.getText().toString().trim();
            String fechaNacimiento = edFechaNacimiento.getText().toString().trim();
            String correo = edCorreo.getText().toString().trim();
            String clave = edClave.getText().toString().trim();
            String confirmarClave = edConfirmarClave.getText().toString().trim();

            // Validaciones
            if (fechaNacimiento.isEmpty() || correo.isEmpty() || clave.isEmpty() || confirmarClave.isEmpty()){
                Toast.makeText(getApplicationContext(), "Por favor, llene todos los campos restantes", Toast.LENGTH_SHORT).show();
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

            if (!Validaciones.esFechaNacimientoValida(fechaNacimiento)) {
                // Ahora la Activity se encarga de mostrar el mensaje
                Toast.makeText(getApplicationContext(), "La fecha de nacimiento no es válida. Debes ser mayor de 18 años.", Toast.LENGTH_LONG).show();
                return;
            }

            // Si todo es válido, registramos
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

    private void verificarDNIconAPI(String dni) {
        Toast.makeText(this, "Verificando DNI...", Toast.LENGTH_SHORT).show();
        btnVerificarDNI.setEnabled(false); // Deshabilitamos el botón mientras verifica


        String url = "https://api.decolecta.com/v1/reniec/dni?numero=" + dni;
        final String token = "Bearer sk_10867.J8lZpYyBORPYrrUvtI14fwpxgKC8JWN9";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {

                    try {
                        String nombres = response.getString("first_name");
                        String apellidoPaterno = response.getString("first_last_name");
                        String apellidoMaterno = response.getString("second_last_name");

                        // Capitalizamos los nombres para que se vean mejor (ej. ROXANA -> Roxana)
                        edNombre.setText(Validaciones.capitalizarPalabras(nombres));
                        edApellido.setText(Validaciones.capitalizarPalabras(apellidoPaterno + " " + apellidoMaterno));

                        habilitarFormularioPostVerificacion(); // Desbloqueamos el resto del formulario

                        Toast.makeText(this, "DNI verificado. Por favor, complete el resto de sus datos.", Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error al procesar la respuesta del DNI.", Toast.LENGTH_SHORT).show();
                        btnVerificarDNI.setEnabled(true);
                    }
                },
                error -> {
                    // Si Decolecta da un error (DNI no encontrado, token inválido, etc.)
                    Toast.makeText(this, "El DNI ingresado no existe o no pudo ser verificado.", Toast.LENGTH_LONG).show();
                    btnVerificarDNI.setEnabled(true);
                }
        ) {
            //  VOLVEMOS A USAR LA CABECERA DE AUTORIZACIÓN
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("Content-Type", "application/json"); // Como indica la documentación
                headers.put("Authorization", token);
                return headers;
            }
        };
        colaPeticiones.add(request);
    }

    private void mostrarCalendario() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                RegistrarActivity.this,
                (view, year, monthOfYear, dayOfMonth) -> {
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
                    finish();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}