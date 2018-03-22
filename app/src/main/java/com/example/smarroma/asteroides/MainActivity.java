package com.example.smarroma.asteroides;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView tv1;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        tv1 = (TextView)findViewById(R.id.tv1);

        //Musica de fons
        mp = MediaPlayer.create(MainActivity.this, R.raw.audio);
        mp.start();
        mp.setLooping(true);


    }

    public void clickPuntuacions(View view){
        Intent i = new Intent(this, Puntuacions.class );
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
        if (id==R.id.it1) {

        }
        if (id==R.id.it2) {
            Toast.makeText(MainActivity.this, "El programador es Sergio", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
