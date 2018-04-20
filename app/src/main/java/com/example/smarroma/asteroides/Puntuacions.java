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

    private final int MAX_VIEW_SCORES = 10;
    private ListView lt1;
    private LinkedList<String> puntuaciones = new LinkedList<String>();

    //----------------------Colocar Usuario - Numero(Puntuacion) Tenerlo preparado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntuacions);
        lt1 = (ListView) findViewById(R.id.lt1);

        cargarPuntuaciones();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,puntuaciones);
        lt1.setAdapter(adapter);


    }

    //Guardem la puntuacio i el usuari
    public static void guardarPuntuacio(Context context, String name, int score){

        SharedPreferences pref = Puntuacions.getSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        //Le ponemos un key distinto a cada dato
        editor.putString(name + score, score + " - " + name);

        editor.commit();
    }

    public void cargarPuntuaciones(){
        int i = 0;
        SharedPreferences pref = Puntuacions.getSharedPreferences(getApplicationContext());
        LinkedList<String> temporallyList = new LinkedList<String>();

        //Key - Valor por defecto
        Map<String, ?> puntuacion = pref.getAll();
        Iterator<?> it = puntuacion.entrySet().iterator();
        while(it.hasNext()){
            i++;
            Map.Entry pair = (Map.Entry) it.next();
            temporallyList.add(pair.getValue().toString());
        }

        /**
         * Coloquem les 10 puntuacions mes altes
         */
        int puntuacionValueInArray;
        puntuaciones.add("0 - Admin");
        for (String value: temporallyList) {
            boolean encontrado = false;
            int j = 0;
            //Ya que el value esta formado por el String "score - name" obtenemos solo el score
            int puntuacionValue = Integer.parseInt(value.substring(0, value.indexOf("-") - 1));
            while(j < puntuaciones.size() && !encontrado){
                puntuacionValueInArray = Integer.parseInt(puntuaciones.get(j).substring(0, puntuaciones.get(j).indexOf("-") - 1));
                if(puntuacionValue >= puntuacionValueInArray){
                    puntuaciones.add(j, value);
                    encontrado = true;
                }
                if(puntuaciones.size() > MAX_VIEW_SCORES){
                    puntuaciones.remove(MAX_VIEW_SCORES);
                }
                j++;
            }

        }


    }

    public static SharedPreferences getSharedPreferences (Context context){

        return context.getSharedPreferences("Puntuacions", context.MODE_PRIVATE);

    }

}
