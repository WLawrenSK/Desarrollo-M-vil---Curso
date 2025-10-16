package com.example.saludmovil;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class InicioActivity extends AppCompatActivity {

    TextView saludoUsuario;
    ImageButton btnPerfil;
    ImageButton btnRitmoCardiaco;
    MaterialCardView cardAgendarCita, cardUbicanos, cardEspecialidades, cardRecetasMedicas, cardMisCitas, cardSalir;

    private int idUsuario;
    private String rolUsuario;

    // --- INICIO DE MODIFICACIONES PARA NOTIFICACIONES ---

    // 1. ID único para nuestro canal de notificaciones.
    private static final String CHANNEL_ID = "citas_channel";

    // 2. Launcher para solicitar el permiso de notificaciones.
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Si el permiso es concedido, enviamos la notificación.
                    enviarNotificacionDeCita();
                } else {
                    // Opcional: Informar al usuario que no recibirá notificaciones.
                    Toast.makeText(this, "No se mostrarán notificaciones de citas.", Toast.LENGTH_SHORT).show();
                }
            });

    // --- FIN DE MODIFICACIONES PARA NOTIFICACIONES ---


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        // ... (tu código existente para inicializar vistas)
        saludoUsuario = findViewById(R.id.saludoUsuario);
        btnPerfil = findViewById(R.id.buttonMiPerfil);
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
            // Ya no mostramos el diálogo aquí, lo haremos con la notificación.
            // mostrarNotificacionCitaPendiente();
        }

        configurarListeners();

        // --- INICIO DE LLAMADAS PARA NOTIFICACIONES ---
        crearCanalDeNotificacion(); // Creamos el canal
        solicitarPermisoYEnviarNotificacion(); // Solicitamos permiso y enviamos
        // --- FIN DE LLAMADAS PARA NOTIFICACIONES ---
    }


    // --- INICIO DE NUEVOS MÉTODOS PARA NOTIFICACIONES ---

    private void solicitarPermisoYEnviarNotificacion() {
        // Solo para Android 13 (API 33) y versiones superiores
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // Si ya tenemos el permiso, enviamos la notificación.
                enviarNotificacionDeCita();
            } else {
                // Si no, lo solicitamos.
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        } else {
            // Para versiones anteriores a Android 13, no se necesita permiso.
            enviarNotificacionDeCita();
        }
    }

    private void crearCanalDeNotificacion() {
        // Los canales solo son necesarios para Android 8.0 (API 26) y superiores.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Recordatorio de Citas";
            String description = "Canal para notificar sobre próximas citas médicas";
            int importance = NotificationManager.IMPORTANCE_HIGH; // Importancia alta para que aparezca
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Registrar el canal en el sistema
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void enviarNotificacionDeCita() {
        // --- Contenido simulado de la cita ---
        String fechaCita = "Lun, 20 Oct - 10:30 AM";
        String doctorCita = "Dr. Alberto García (Cardiología)";
        String titulo = "Recordatorio de Cita";
        String texto = "Tu próxima cita es con el " + doctorCita + " a las " + fechaCita;

        // --- Acción al tocar la notificación ---
        // Creamos un Intent para que al tocar la notificación, se abra la InicioActivity de nuevo.
        // En un futuro, podría abrir la pantalla de "Mis Citas".
        Intent intent = new Intent(this, InicioActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // --- Construir la notificación ---
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.health_cross_24px) // Un ícono pequeño es obligatorio
                .setContentTitle(titulo)
                .setContentText(texto)
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Prioridad alta
                .setContentIntent(pendingIntent) // La acción que se ejecuta al tocar
                .setAutoCancel(true); // La notificación desaparece al tocarla

        // --- Enviar la notificación ---
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // El ID (en este caso 1) es único para esta notificación. Si envías otra con el mismo ID, la actualizará.
        try {
            notificationManager.notify(1, builder.build());
        } catch (SecurityException e) {
            // Este catch es una salvaguarda por si el permiso no se concede correctamente.
            e.printStackTrace();
        }
    }

    // --- FIN DE NUEVOS MÉTODOS PARA NOTIFICACIONES ---


    // ... (El resto de tus métodos: configurarListeners, mostrarSaludoPersonalizado, etc. se mantienen igual)
    private void configurarListeners() {
        btnPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(InicioActivity.this, PerfilPacienteActivity.class);
            intent.putExtra("id_usuario", idUsuario);
            startActivity(intent);
        });
        btnRitmoCardiaco.setOnClickListener(v -> {
            Intent intent = new Intent(InicioActivity.this, RitmoCardiacoActivity.class);
            startActivity(intent);
        });
        cardAgendarCita.setOnClickListener(v -> {
            Intent intent = new Intent(InicioActivity.this, AgendarCitaActivity.class);
            startActivity(intent);
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
        Intent intent = new Intent(InicioActivity.this, RolesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void mostrarDialogoCompletarPerfil() {
        new MaterialAlertDialogBuilder(this)
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