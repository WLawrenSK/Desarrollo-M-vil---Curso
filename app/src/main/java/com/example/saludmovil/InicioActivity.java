package com.example.saludmovil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

public class InicioActivity extends AppCompatActivity {

    TextView saludoUsuario;
    ImageButton btnPerfil;

    // --- ✨ 1. DECLARAR EL NUEVO BOTÓN ✨ ---
    ImageButton btnRitmoCardiaco;

    MaterialCardView cardAgendarCita, cardUbicanos, cardEspecialidades, cardRecetasMedicas, cardMisCitas, cardSalir;

    private int idUsuario;
    private String rolUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        saludoUsuario = findViewById(R.id.saludoUsuario);
        btnPerfil = findViewById(R.id.buttonMiPerfil);

        // --- ✨ 2. VINCULAR EL NUEVO BOTÓN ✨ ---
        btnRitmoCardiaco = findViewById(R.id.buttonRitmoCardiaco);

        cardAgendarCita = findViewById(R.id.cardAgendarCita);
        cardUbicanos = findViewById(R.id.cardUbicanos);
        cardEspecialidades = findViewById(R.id.cardEspecialidades);
        cardRecetasMedicas = findViewById(R.id.cardRecetasMedicas);
        cardMisCitas = findViewById(R.id.cardMisCitas);
        cardSalir = findViewById(R.id.cardSalir);

        SharedPreferences sp = getSharedPreferences("datos_usuario", MODE_PRIVATE);
        idUsuario = sp.getInt("id_usuario", -1);
        rolUsuario = sp.getString("rol_usuario", "");

        if (idUsuario == -1 || rolUsuario.isEmpty()) {
            Toast.makeText(this, "Error de sesión, por favor inicie de nuevo", Toast.LENGTH_SHORT).show();
            irALogin();
            return;
        }

        mostrarSaludoPersonalizado();

        if (rolUsuario.equals("paciente")) {
            BaseDeDatos bd = new BaseDeDatos(getApplicationContext());
            if (!bd.isPerfilCompletoPorId(idUsuario)) {
                mostrarDialogoCompletarPerfil();
            }
        }

        configurarListeners();
    }

    private void configurarListeners() {
        btnPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(InicioActivity.this, PerfilPacienteActivity.class);
            intent.putExtra("id_usuario", idUsuario);
            startActivity(intent);
        });

        // --- ✨ 3. AÑADIR LA ACCIÓN DE CLIC PARA EL NUEVO BOTÓN ✨ ---
        btnRitmoCardiaco.setOnClickListener(v -> {
            Intent intent = new Intent(InicioActivity.this, RitmoCardiacoActivity.class);
            startActivity(intent);
        });

        // El resto de los listeners se mantienen igual
        cardAgendarCita.setOnClickListener(v -> {
            Toast.makeText(this, "Abriendo Agendar Cita...", Toast.LENGTH_SHORT).show();
        });

        cardUbicanos.setOnClickListener(v -> {
            Intent intent = new Intent(InicioActivity.this, UbicanosMapActivity.class);
            startActivity(intent);
        });

        cardEspecialidades.setOnClickListener(v -> {
            Intent intent = new Intent(InicioActivity.this, EspecialidadesActivity.class);
            startActivity(intent);
        });

        cardRecetasMedicas.setOnClickListener(v -> {
            Toast.makeText(this, "Abriendo recetas médicas...", Toast.LENGTH_SHORT).show();
        });

        cardMisCitas.setOnClickListener(v -> {
            Toast.makeText(this, "Abriendo mis citas...", Toast.LENGTH_SHORT).show();
        });

        cardSalir.setOnClickListener(v -> irALogin());
    }

    private void mostrarSaludoPersonalizado() {
        BaseDeDatos bd = new BaseDeDatos(getApplicationContext());
        String nombreUsuario = "Usuario";
        if (rolUsuario.equals("paciente")) {
            nombreUsuario = bd.getNombrePaciente(idUsuario);
        } else if (rolUsuario.equals("doctor")) {
            nombreUsuario = bd.getNombreDoctor(idUsuario);
        }
        saludoUsuario.setText("¡Hola, " + nombreUsuario + "!");
    }

    private void irALogin() {
        SharedPreferences sp = getSharedPreferences("datos_usuario", MODE_PRIVATE);
        sp.edit().clear().apply();
        Intent intent = new Intent(InicioActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void mostrarDialogoCompletarPerfil() {
        new AlertDialog.Builder(this)
                .setTitle("Perfil Médico Incompleto")
                .setMessage("Para brindarte un mejor servicio, por favor completa tu perfil médico.")
                .setPositiveButton("Completar Perfil", (dialog, which) -> {
                    Intent intent = new Intent(InicioActivity.this, PerfilPacienteActivity.class);
                    intent.putExtra("id_usuario", idUsuario);
                    startActivity(intent);
                })
                .setNegativeButton("Más Tarde", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }
}