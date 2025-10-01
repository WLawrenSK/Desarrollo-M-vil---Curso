package com.example.saludmovil;

// Esta clase será nuestro "cajón de herramientas" para funciones de validación.
public class Validaciones {


    private Validaciones() {}

    public static boolean esValido(String miClave){
        // Banderas para verificar si la contraseña contiene al menos una letra, un número y un caracter especial.
        int f1=0, f2=0, f3=0;
        // Verifica si la longitud de la contraseña es menor a 8 caracteres.
        if(miClave.length() < 8){
            return false;
        } else {
            // Recorre la contraseña para buscar una letra.
            for (int i = 0; i < miClave.length(); i++){
                if (Character.isLetter(miClave.charAt(i))){
                    f1 = 1; // Si encuentra una letra, cambia la bandera a 1.
                }
            }
            // Recorre la contraseña para buscar un número.
            for (int r = 0; r < miClave.length(); r++){
                if (Character.isDigit(miClave.charAt(r))){
                    f2 = 1; // Si encuentra un número, cambia la bandera a 1.
                }
            }
            // Recorre la contraseña para buscar un caracter especial.
            for (int p = 0; p < miClave.length(); p++){
                char c = miClave.charAt(p);
                // Comprueba si el caracter es un caracter especial usando su código ASCII.
                if (c >= 33 && c <= 46 || c == 64){
                    f3 = 1; // Si encuentra un caracter especial, cambia la bandera a 1.
                }
            }
            // Si las tres banderas son 1, significa que la contraseña es válida.
            if (f1 == 1 && f2 == 1 && f3 == 1)
                return true;
            return false; // Si alguna bandera no es 1, la contraseña no es válida.
        }
    }

}