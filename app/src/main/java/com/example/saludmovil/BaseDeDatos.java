package com.example.saludmovil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BaseDeDatos extends SQLiteOpenHelper {
    public BaseDeDatos(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String primeraConsulta = "create table usuarios(usuario text, correo text, clave text)";
        db.execSQL(primeraConsulta);

        // Agregamos la tabla de los doctores
        String crearTablaDoctores = "create table doctores(cmp text, clave text, nombre_completo text, dni text, fecha_nacimiento text, telefono text, correo text, especialidad text, universidad text)";
        db.execSQL(crearTablaDoctores);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void registrar(String usuario, String correo, String clave){
        ContentValues cv = new ContentValues();
        cv.put("usuario", usuario);
        cv.put("correo", correo);
        cv.put("clave", clave);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("usuarios", null, cv);
        db.close();
    }

    public int login(String usuario, String clave){
        int resultado = 0;
        String str[] = new String[2];
        str[0] = usuario;
        str[1] = clave;
        SQLiteDatabase bd = getReadableDatabase();
        Cursor c = bd.rawQuery("select * from usuarios where usuario = ? and clave = ?", str);
        if (c.moveToFirst()){
            resultado = 1;
        }
        return resultado;
    }

    public int loginDoctor(String cmp, String clave){
        int resultado = 0;
        // Creamos un arreglo de Strings para los parametros de la consulta
        String[] parametros = {cmp, clave};
        SQLiteDatabase bd = getReadableDatabase();

        // Ejecutamos la consulta en la tabla 'doctores' y vamos a buscar estos dos campos
        Cursor c = bd.rawQuery("select * from doctores where cmp = ? and clave = ?", parametros);

        // Si esta busqueda fue exitosa

        if(c.moveToFirst()){
            resultado = 1;
        }

        // Cerramos la conexion de base de datos
        bd.close();

        return resultado;
    }

    public void registrarDoctor(String cmp, String clave, String nombre_completo, String dni, String fecha_nacimiento, String telefono, String correo, String especialidad, String universidad){
        // 1. Preparamos un contenedor para los valores que vamos a insertar.
        ContentValues cv = new ContentValues();

        // 2. Ponemos cada dato en el contenedor, asociando cada valor con el nombre de su columna en la tabla 'doctores'.
        cv.put("cmp", cmp);
        cv.put("clave", clave);
        cv.put("nombre_completo", nombre_completo);
        cv.put("dni", dni);
        cv.put("fecha_nacimiento", fecha_nacimiento);
        cv.put("telefono", telefono);
        cv.put("correo", correo);
        cv.put("especialidad", especialidad);
        cv.put("universidad", universidad);

        // 3. Obtenemos una versión de la base de datos en la que podemos escribir.
        SQLiteDatabase db = getWritableDatabase();

        // 4. Insertamos la nueva fila de datos en la tabla 'doctores'.
        db.insert("doctores", null, cv);

        // 5. Cerramos la conexión a la base de datos para liberar recursos.
        db.close();
    }
}
