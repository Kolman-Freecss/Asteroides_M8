package com.example.smarroma.asteroides;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class Puntuacions extends AppCompatActivity {


    private ListView lt1;
    final String[] datos = new String[]{"Provant", "El", "Adapter"};
    private LinkedList<String> puntuaciones = new LinkedList<String>();

    private String currentUser = null;

    //----------------------Colocar Usuario - Numero(Puntuacion) Tenerlo preparado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntuacions);

        lt1 = (ListView) findViewById(R.id.lt1);

        //Recojemos variables que nos llegan del Main
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            this.currentUser = bundle.getString("nomJugador");
        }

        //Hay mas modos como MODE_APPEND
        //Cojemos la sharedPreferences que tenemos
        SharedPreferences pref = getSharedPreferences("Puntuaciones", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        guardarPuntuacio();
        cargarPuntuaciones();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,puntuaciones);
        lt1.setAdapter(adapter);


    }

    //Guardem la puntuacio i el usuari
    public void guardarPuntuacio(){

        SharedPreferences pref = getSharedPreferences("Puntuaciones", Context.MODE_APPEND);
        SharedPreferences.Editor editor = pref.edit();

        //Le ponemos un key distinto a cada dato
        if(this.currentUser != null) {
            editor.putString("provantKey", this.currentUser);
        }

        //De primeras podriamos guardar el nombre del usuario, y tal y como acaba la partida sobreescribimos la key
        //y le colocamos el valor a la key   "puntuacion" + "nombre" tal y como el siguiente ejemplo:
        //editor.putString("provantKey", "hola " + datos[0]);

        editor.commit();
    }

    public void cargarPuntuaciones(){

        SharedPreferences pref = getSharedPreferences("Puntuaciones", Context.MODE_PRIVATE);

        //Key - Valor por defecto
        Map<String, ?> puntuacion = pref.getAll();
        Iterator<?> it = puntuacion.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry) it.next();
            puntuaciones.add(pair.getValue().toString());
        }
        //Esto es para añadir de uno en uno
        /*puntuaciones.add(pref.getString("provantKey", "No esta"));*/

    }

}
