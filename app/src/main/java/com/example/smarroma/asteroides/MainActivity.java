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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements AlertInputNameFragment.AlertInputNameFragmentListener{

    private final int NUM_ASTEROIDES = 6;

    private TextView tv1;
    private static MediaPlayer mp;
    private static String nombre = null;
    //Interfaz de la comunicación con el DialogFragment
    AlertInputNameFragment listenerDialogFragment;

    //Per les animacions dels asteroides
    private ImageView[] imageViewArray = new ImageView[NUM_ASTEROIDES];

    private static boolean musicOrNot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getPreferenceValues();

        tv1 = (TextView)findViewById(R.id.tv1);
        Animation animacion
                = AnimationUtils.loadAnimation(this,R.anim.animacion);
        tv1.startAnimation(animacion);

        /**
         * Animem les imatges d'asteroides
         */
        Animation animacionAsteroides
                = AnimationUtils.loadAnimation(this,R.anim.animationasteroids);
        for(int i = 0; i < NUM_ASTEROIDES; i++){

            int res = getResources().getIdentifier("imageView" + i, "id", getPackageName());
            imageViewArray[i] = (ImageView) findViewById(res);
            imageViewArray[i].startAnimation(animacionAsteroides);

        }
    }

    /**
     * Per quan tanques l'aplicació agafi la configuració desada
     */
    @Override
    protected void onResume() {
        super.onResume();
        this.getPreferenceValues();
        //Musica de fons
        if(this.musicOrNot) {
            mp = MediaPlayer.create(MainActivity.this, R.raw.audio);
            mp.start();
            mp.setLooping(true);
        }else{
            if(mp != null) {
                mp.stop();
                mp.setLooping(false);
            }
        }
    }
    //-----------Input Text Name ------------------

    public void alertEditTextFragment(View view){

        AlertInputNameFragment alerta = new AlertInputNameFragment();
        alerta.show(getSupportFragmentManager(), "inputNameDialog");

    }

    //Al confirmar el button del dialog s'agafa el name rebut
    @Override
    public void onConfirmarButtonClick(String name) {
        nombre = name;
    }

    //-----------FIN Input Text Name ------------------

    public void clickPuntuacions(View view){
        Intent i = new Intent(this, Puntuacions.class );
        startActivity(i);
    }

    public void clickSortir(View view){
        finishAffinity();
    }

    //////// GET & SET VALORES PREFERENCIAS /////////////

    private void getPreferenceValues(){

        String keyMusic = PreferenceFragment.getKeyReproducirMusicaChk();
        this.musicOrNot = PreferenceFragment.getBoolean(this.getApplicationContext(), keyMusic);

    }

//-------------------------- Menu -----------------------------
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuasteroides, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //Settings
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


    public static MediaPlayer getMp() {
        return mp;
    }

    public static String getNombre() {
        return nombre;
    }

    public static void setMusicOrNot(boolean musicOrNot) {
        MainActivity.musicOrNot = musicOrNot;
    }

    public static void setMp(MediaPlayer mp) {
        MainActivity.mp = mp;
    }
}
