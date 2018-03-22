package com.example.smarroma.asteroides;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.LinkedList;

public class Puntuacions extends AppCompatActivity {


    private ListView lt1;
    final String[] datos = new String[]{"Provant", "El", "Adapter"};
    private LinkedList<String> puntuaciones = new LinkedList<String>();

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntuacions);

        lt1 = (ListView) findViewById(R.id.lt1);

        //Hay mas modos como MODE_APPEND
        //Cojemos la sharedPreferences que tenemos
        pref = getSharedPreferences("Puntuaciones", Context.MODE_PRIVATE);
        editor = pref.edit();

        guardarPuntuacio();
        cargarPuntuaciones();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,puntuaciones);
        lt1.setAdapter(adapter);


    }

    //Provant a guardar la puntuacio
    public void guardarPuntuacio(){

        //Le ponemos un key distinto a cada dato
        editor.putString("provantKey", datos[0]);
        editor.commit();
        editor.putString("provantKey1", datos[1]);
        editor.commit();
        editor.putString("provantKey2", datos[2]);
        editor.commit();
    }

    public void cargarPuntuaciones(){
        //Key - Valor por defecto
        puntuaciones.add(pref.getString("provantKey", "No esta"));
        puntuaciones.add(pref.getString("provantKey1", "No esta1"));
        puntuaciones.add(pref.getString("provantKey2", "No esta2"));

    }

}
