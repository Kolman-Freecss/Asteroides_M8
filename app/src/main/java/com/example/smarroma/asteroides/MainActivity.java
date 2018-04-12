package com.example.smarroma.asteroides;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AlertInputNameFragment.AlertInputNameFragmentListener{

    private TextView tv1;
    private MediaPlayer mp;
    private String nombre = null;

    //Interfaz de la comunicación con el DialogFragment
    AlertInputNameFragment listenerDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        tv1 = (TextView)findViewById(R.id.tv1);
        Animation animacion
                = AnimationUtils.loadAnimation(this,R.anim.animacion);
        tv1.startAnimation(animacion);



        //Musica de fons
        mp = MediaPlayer.create(MainActivity.this, R.raw.audio);
        mp.start();
        mp.setLooping(true);


    }

    //-----------Input Text Name ------------------

    public void alertEditTextFragment(View view){

        new AlertInputNameFragment().show(getSupportFragmentManager(), "inputNameDialog");

    }

    @Override
    public void onConfirmarButtonClick(String name) {
        this.nombre = name;
        //Aqui iria el comienzo del juego


        //Simple comprobación
        //Toast.makeText(MainActivity.this, this.nombre, Toast.LENGTH_SHORT).show();
    }

    //-----------FIN Input Text Name ------------------


    public void clickGame(View view){
        Intent i = new Intent(this, Juego.class );
        startActivity(i);
    }

    //-------------------------------- Metodo que miraremos para utilizar luego para guardar usuario y puntuaciones
    //ya que jugar tiene que llamar a la otra activity
    public void clickPuntuacions(View view){
        Intent i = new Intent(this, Puntuacions.class );
        if(this.nombre != null) {
            i.putExtra("nomJugador", this.nombre);
        }
        startActivity(i);
    }

    public void clickSortir(View view){
        finish();
    }



//-------------------------- Menu -----------------------------
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuasteroides, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //Configuración
        if (id==R.id.it1) {
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new PreferenceFragment())
                    .commit();
        }
        //Info
        if (id==R.id.it2) {
            Toast.makeText(MainActivity.this, "El programador es Sergio", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }


}
