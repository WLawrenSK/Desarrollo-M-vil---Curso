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
}
