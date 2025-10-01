package com.example.saludmovil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BaseDeDatos extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "saludmovil";
    private static final int DATABASE_VERSION = 2;
    public BaseDeDatos(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabla para pacientes, ahora con los nuevos campos y nombre correcto.
        String crearTablaPacientes = "create table pacientes(dni text primary key, nombre text, apellido text, fecha_nacimiento text, clave text)";
        db.execSQL(crearTablaPacientes);

        // Tabla para los doctores.
        String crearTablaDoctores = "create table doctores(cmp text, clave text, nombre_completo text, dni text, fecha_nacimiento text, telefono text, correo text, especialidad text, universidad text)";
        db.execSQL(crearTablaDoctores);

        // Tabla para el perfil medico de los pacientes

        String crearTablaPerfilPaciente = "create table perfil_paciente(id integer primary key autoincrement, usuario_dni text, estatura text, peso text, tipo_sangre text, sexo text, alergias text, enfermedades_cronicas text, medicamentos_actuales text, nombre_contacto_emergencia text, telefono_contacto_emergencia text)";
        db.execSQL(crearTablaPerfilPaciente);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Para desarrollo, una forma simple de manejar cambios es borrar y recrear las tablas.
        // ADVERTENCIA: Esto borrará todos los datos existentes.
        db.execSQL("DROP TABLE IF EXISTS pacientes");
        db.execSQL("DROP TABLE IF EXISTS doctores");
        onCreate(db);
    }

    // --- MÉTODOS PARA PACIENTES ---

    // MÉTODO 'registrar' ACTUALIZADO para Pacientes
    public void registrar(String dni, String nombre, String apellido, String fecha_nacimiento, String clave){
        ContentValues cv = new ContentValues();
        cv.put("dni", dni);
        cv.put("nombre", nombre);
        cv.put("apellido", apellido);
        cv.put("fecha_nacimiento", fecha_nacimiento);
        cv.put("clave", clave);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("pacientes", null, cv); // Insertamos en la tabla 'pacientes'
        db.close();
    }

    // MÉTODO 'login' ACTUALIZADO para Pacientes
    public int login(String dni, String clave){
        int resultado = 0;
        String str[] = {dni, clave};
        SQLiteDatabase bd = getReadableDatabase();
        // Buscamos en la tabla 'pacientes' usando el 'dni'
        Cursor c = bd.rawQuery("select * from pacientes where dni = ? and clave = ?", str);
        if (c.moveToFirst()){
            resultado = 1;
        }
        bd.close(); // Buena práctica cerrar la conexión
        return resultado;
    }


    // --- MÉTODOS PARA DOCTORES ---

    public int loginDoctor(String cmp, String clave){
        int resultado = 0;
        String[] parametros = {cmp, clave};
        SQLiteDatabase bd = getReadableDatabase();
        Cursor c = bd.rawQuery("select * from doctores where cmp = ? and clave = ?", parametros);
        if(c.moveToFirst()){
            resultado = 1;
        }
        bd.close();
        return resultado;
    }

    public void registrarDoctor(String cmp, String clave, String nombre_completo, String dni, String fecha_nacimiento, String telefono, String correo, String especialidad, String universidad){
        ContentValues cv = new ContentValues();
        cv.put("cmp", cmp);
        cv.put("clave", clave);
        cv.put("nombre_completo", nombre_completo);
        cv.put("dni", dni);
        cv.put("fecha_nacimiento", fecha_nacimiento);
        cv.put("telefono", telefono);
        cv.put("correo", correo);
        cv.put("especialidad", especialidad);
        cv.put("universidad", universidad);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("doctores", null, cv);
        db.close();
    }

    // Metodo para verificar si un paciente ya ha completado su perfil medico

    public boolean isPerfilCompleto(String dni){
        boolean perfilCompleto = false;
        String[] parametros = {dni};
        SQLiteDatabase bd = getReadableDatabase();

        // Buscamos si existe una fila "perfil paciente" con el DNI del usuario

        Cursor c = bd.rawQuery("SELECT * FROM perfil_paciente WHERE usuario_dni = ?", parametros);

        // Si el cursor lo encuentra al menos una fila, significa que el perfil ya se lleno con los datos necesarios

        if (c.moveToFirst()){
            perfilCompleto = true;
        }

        c.close();
        bd.close();
        return perfilCompleto;
    }

    // Metodo para ACTUALIZAR la informacion del paciente

    public void actualizarPerfilPaciente(String dni, String estatura, String peso, String tipo_sangre, String sexo, String alergias, String enfermedades, String medicamentos, String contactoNombre, String contactoTelefono){
        ContentValues cv = new ContentValues();
        cv.put("usuario_dni", dni);
        cv.put("estatura", estatura);
        cv.put("peso", peso);
        cv.put("tipo_sangre", tipo_sangre);
        cv.put("sexo", sexo);
        cv.put("alergias", alergias);
        cv.put("enfermedades_cronicas", enfermedades);
        cv.put("medicamentos_actuales", medicamentos);
        cv.put("nombre_contacto_emergencia", contactoNombre);
        cv.put("telefono_contacto_emergencia", contactoTelefono);

        SQLiteDatabase db = getWritableDatabase();

        // Verificamos si ya existe un perfil para este DNI

        Cursor cursor = db.rawQuery("SELECT * FROM perfil_paciente WHERE usuario_dni = ?", new String[]{dni});
        if (cursor.getCount() > 0){
            // Si existe, actualizamos los datos

            db.update("perfil_paciente", cv, "usuario_dni= ?", new String[]{dni});
        } else {
            // Si no existe, insertamos un nuevo registro
            db.insert("perfil_paciente", null, cv);
        }
        cursor.close();
        db.close();
    }

    public Cursor getPerfilPaciente(String dni){
        SQLiteDatabase bd = getReadableDatabase();
        // Buscamos la fila correspondiente al DNI

        Cursor cursor = bd.rawQuery("SELECT * FROM perfil_paciente WHERE usuario_dni = ?", new String[]{dni});
        return cursor;
    }

}