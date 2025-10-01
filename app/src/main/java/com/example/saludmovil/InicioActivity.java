package com.example.saludmovil;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class InicioActivity extends AppCompatActivity {

    ImageButton btnPerfil; // Variable para el boton de perfil


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        SharedPreferences sp = getSharedPreferences("datos", MODE_PRIVATE);
        // Usamos la llave "usuario_dni" que definimos en el LoginActivity
        String dni = sp.getString("usuario_dni", "").toString();
        btnPerfil = findViewById(R.id.buttonMiPerfil);
        // --- CÓDIGO NUEVO PARA VERIFICAR PERFIL ---
        BaseDeDatos bd = new BaseDeDatos(getApplicationContext());
        if (!bd.isPerfilCompleto(dni)) {
            // Si el perfil NO está completo, mostramos el diálogo
            mostrarDialogoCompletarPerfil(dni);
        }

        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioActivity.this, PerfilPacienteActivity.class);
                intent.putExtra("usuario_dni", dni);
                startActivity(intent);
            }
        });


        // El resto de tu código para los botones
        CardView salir = findViewById(R.id.cardSalir);
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.apply();
                startActivity(new Intent(InicioActivity.this, LoginActivity.class));
            }
        });

        CardView especialidades = findViewById(R.id.cardEspecialidades);
        especialidades.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InicioActivity.this, EspecialidadesActivity.class));
            }
        });
    }

    // --- MÉTODO NUEVO PARA MOSTRAR EL DIÁLOGO ---
    private void mostrarDialogoCompletarPerfil(String dni) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Perfil Médico Incompleto");
        builder.setMessage("Para brindarte un mejor servicio y que los doctores puedan atenderte adecuadamente, por favor completa tu perfil médico.");

        // Botón para ir a completar el perfil
        builder.setPositiveButton("Completar Perfil", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Creamos un Intent para ir a PerfilPacienteActivity
                Intent intent = new Intent(InicioActivity.this, PerfilPacienteActivity.class);
                // Le pasamos el DNI a la siguiente pantalla para que sepa de quién es el perfil
                intent.putExtra("usuario_dni", dni);
                startActivity(intent);
            }
        });

        // Botón para hacerlo más tarde
        builder.setNegativeButton("Más Tarde", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false); // Impide que el usuario cierre el diálogo tocando fuera de él
        dialog.show();
    }
}