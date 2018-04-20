package com.example.smarroma.asteroides;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import java.util.Vector;

/**
 * Created by smarroma on 12/04/2018.
 */

public class VistaJuego extends View {

    //// AUDIO ////
    //Clase que maneja y reproduce audios de forma rapida
    SoundPool soundPool;
    int mpDisparo, mpExplosion;

    //// CONTROL DE VIDAS / VIDAS / PUNTUACION ////
    private int vidas;
    private int score;
    private int currentColision = -1;

    /// MISIL /////
    private Grafico misil;
    private static int PASO_VELOCIDAD_MISIL = 12;
    private boolean misilActivo=false;
    private int tiempoMisil;

    ////// THREAD Y TIEMPO //////
    // Thread encargado de procesar el juego
    private ThreadJuego thread = new ThreadJuego();
    // Cada cuanto queremos procesar cambios (ms)
    private static int PERIODO_PROCESO = 50;
    // Cuando se realizó el último proceso
    private long ultimoProceso = 0;

    ////// NAVE //////
    private Grafico nave;// Gráfico de la nave
    private int giroNave; // Incremento de dirección
    private float aceleracionNave; // aumento de velocidad
    // Incremento estándar de giro y aceleración
    private static final int PASO_GIRO_NAVE = 5;
    private static final float PASO_ACELERACION_NAVE = 0.5f;

    // //// ASTEROIDES //////
    private Vector<Grafico> Asteroides; // Vector con los Asteroides
    private int numAsteroides = 3; // Número inicial de asteroides
    private int numFragmentos = 3; // Fragmentos en que se divide

    // Per aconseguir fragmentar els asteroides quan un missil colisioni Punt 17
    private Drawable drawableAsteroide[] = new Drawable[3];

    private Drawable drawableNave, drawableNaveFuego, drawableMisil;

    public VistaJuego(Context context, AttributeSet attrs) {
        super(context, attrs);
        //Drawable drawableNave, drawableMisil;

        //Coloquem els Settings modificats per l'usuari
        getPreferenceValues();
        // Obtenemos referencia al recurso asteroide1.png guardado en carpeta Res
        /*drawableAsteroide = context.getResources().getDrawable(
                R.drawable.asteroide1);*/
        //AL DESTRUIRSE EL ASTEROIDE SE FRAGMENTA EN VARIOS PEQUEÑOS
        drawableAsteroide[0] = context.getResources().
                getDrawable(R.drawable.asteroide1);
        drawableAsteroide[1] = context.getResources().
                getDrawable(R.drawable.asteroide2);
        drawableAsteroide[2] = context.getResources().
                getDrawable(R.drawable.asteroide3);

        //Tendremos 3 vidas
        this.vidas = 3;
        this.score = 0;

        //Audio
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        mpDisparo = soundPool.load(context, R.raw.disparo, 0);
        mpExplosion = soundPool.load(context,R.raw.explosion, 0);


        drawableNave = context.getResources().getDrawable(
                R.drawable.nave);

        drawableMisil = context.getResources().getDrawable(
                R.drawable.misil1);

        misil = new Grafico(this, drawableMisil);
        nave = new Grafico(this, drawableNave);
        //creamos los asteroides e inicializamos su velocidad, ángulo y rotación.
        //La posición inicial no la podemos obtener hasta conocer ancho y alto pantalla
        Asteroides = new Vector<Grafico>();
        //Petit cambi al index per el IndexOutOfBounds al numAsteroides
        for (int i = 0; i < numAsteroides; i++) {
            Grafico asteroide = new Grafico(this, drawableAsteroide[i]);
            asteroide.setIncY(Math.random() * 4 - 2);
            asteroide.setIncX(Math.random() * 4 - 2);
            asteroide.setAngulo((int) (Math.random() * 360));
            asteroide.setRotacion((int) (Math.random() * 8 - 4));
            Asteroides.add(asteroide);
        }
    }


    //////// GET & SET VALORES PREFERENCIAS /////////////

    private void getPreferenceValues(){

        String keyFragments = PreferenceFragment.getKeyNumFragmentsChk();
        int fragmentsUserChoice = Integer.valueOf(PreferenceFragment.getString(getContext(), keyFragments));
        this.numFragmentos = fragmentsUserChoice;

    }



    //////// FIN GET & SET VALORES PREFERENCIAS //////////////


    //metodo que nos da ancho y alto pantalla
    @Override
    protected void onSizeChanged(int ancho, int alto,
                                 int ancho_anter, int alto_anter) {
        super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);

        //Aixi provoquem que es cridi al metode run(), que sera un bucle infinit
        ultimoProceso = System.currentTimeMillis();
        thread.start();
        // Una vez que conocemos nuestro ancho y alto situamos asteroides de forma
        //aleatoria
        for (Grafico asteroide : Asteroides) {
            do {
                asteroide.setPosX(Math.random() * (ancho - asteroide.getAncho()));
                asteroide.setPosY(Math.random() * (alto - asteroide.getAlto()));
            } while(asteroide.distancia(nave) < (ancho+alto)/5);
        }

        //Colocamos la nave en el centro
        nave.setPosX(ancho / 2 - nave.getAncho() / 2);
        nave.setPosY(alto / 2 - nave.getAlto() / 2);
    }


    //metodo que dibuja la vista
    //Canvas es donde queremos dibujar
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(misilActivo){
            misil.dibujaGrafico(canvas);
        }

        for (Grafico asteroide : Asteroides) {
            asteroide.dibujaGrafico(canvas);
        }

        nave.dibujaGrafico(canvas);
        canvas.save();
        //Paint es el estilo de como vamos a querer el objeto dibujado en el canvas
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#DEE92F"));
        paint.setTextSize(70);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        canvas.drawPaint(paint);



        canvas.drawText(String.valueOf(this.score), 30, 100, paint);
        canvas.restore();

    }

    /**
     * L'animació del joc
     * Es el metode que es va executant a intervals regulars definits per PERIODO_PROCESO
     */
    protected void actualizaFisica() {
        long ahora = System.currentTimeMillis();
        // No hagas nada si el período de proceso no se ha cumplido.
        if(ultimoProceso + PERIODO_PROCESO > ahora) {
            return;
        }
        // Para una ejecución en tiempo real calculamos retardo
        double retardo = (ahora - ultimoProceso) / PERIODO_PROCESO;
        ultimoProceso = ahora; // Para la próxima vez
        // Actualizamos velocidad y dirección de la nave a partir de
        // giroNave y aceleracionNave (según la entrada del jugador)
        nave.setAngulo((int) (nave.getAngulo() + giroNave * retardo));
        double nIncX = nave.getIncX() + aceleracionNave *
                Math.cos(Math.toRadians(nave.getAngulo())) * retardo;
        double nIncY = nave.getIncY() + aceleracionNave *
                Math.sin(Math.toRadians(nave.getAngulo())) * retardo;
        // Actualizamos si el módulo de la velocidad no excede el máximo
        if (Math.hypot(nIncX,nIncY) <= Grafico.getMaxVelocidad()){
            nave.setIncX(nIncX);
            nave.setIncY(nIncY);
        }
        // Actualizamos posiciones X e Y
        nave.incrementaPos(retardo);
        for(Grafico asteroide : Asteroides) {
            asteroide.incrementaPos(retardo);
        }

        //Colisión nave - asteroide
        /**
         * Este for verifica las colisiones, si colisiona guarda la posicion del vector de este asteroide para la siguiente
         * actualizacion de la fisica haga una verificación de si sigue en el radio de este asteroide, en cuanto sale del radio
         * le da el valor -1 por si luego si vuelve a haber una colision casualmente contra el mismo asteroide u otro generado
         * a partir de la destruccion de uno, no lo detecte como contra el que habia chocado
         */
        for (int i = 0; i < Asteroides.size(); i++) {
            //Comprueba todos los asteroides que hay en la pantalla (Asteroides es un vector)
            if (nave.verificaColision(Asteroides.elementAt(i))) {
                //Guardamos el asteroide contra el que ha chocado actualmente y comprobamos si la proxima vuelta
                //sigue en el radio de este asteroide
                if(currentColision != i || currentColision == -1) {
                    this.currentColision = i;
                    this.vidas--;
                }
                break;
            }else if(this.currentColision != -1) {
                if (!nave.verificaColision(Asteroides.elementAt(this.currentColision))) {
                    this.currentColision = -1;
                }
            }
        }



        // Actualizamos posición de misil
        if (misilActivo) {
            misil.incrementaPos(retardo);
            tiempoMisil-=retardo;
            if (tiempoMisil < 0) {
                misilActivo = false;
            } else {
                for (int i = 0; i < Asteroides.size(); i++)
                    //Comprueba todos los asteroides que hay en la pantalla (Asteroides es un vector)
                    if (misil.verificaColision(Asteroides.elementAt(i))) {
                        destruyeAsteroide(i);
                        break;
                    }
            }
        }



    }

    /**
     * ---------------- EXPLICACIO DETALLADA AL PUNT 16.5 ---------------------
     */

    private void destruyeAsteroide(int i) {
        int tam;
        //Priority IMPORTANT (El tercer 1...)
        //prioridad le damos 1 por si volvemos a lanzar un misil que impacta contra otro asteroide
        // El 0 porque solo queramos que lo haga una vez el sonido
        soundPool.play(this.mpExplosion, 1 ,1, 1, 0 , 1);

        if(Asteroides.get(i).getDrawable()!=drawableAsteroide[2]){
            if(Asteroides.get(i).getDrawable()==drawableAsteroide[1]){
                tam=2;
            } else {
                tam=1;
            }
            for(int n=0;n<numFragmentos;n++){
                Grafico asteroide = new Grafico(this,drawableAsteroide[tam]);
                asteroide.setPosX(Asteroides.get(i).getPosX());
                asteroide.setPosY(Asteroides.get(i).getPosY());
                asteroide.setIncX(Math.random()*7-2-tam);
                asteroide.setIncY(Math.random()*7-2-tam);
                asteroide.setAngulo((int)(Math.random()*360));
                asteroide.setRotacion((int)(Math.random()*8-4));
                Asteroides.add(asteroide);
            }
        }

        Asteroides.remove(i);
        misilActivo=false;
        this.score+=10;
    }
    private void ActivaMisil() {
        //El 0 porque solo queramos que lo haga una vez el sonido
        soundPool.play(this.mpDisparo, 1 ,1, 1 , 0 , 1);
        misil.setPosX(nave.getPosX()+nave.getAncho()/2-misil.getAncho()/2);
        misil.setPosY(nave.getPosY()+nave.getAlto()/2-misil.getAlto()/2);
        misil.setAngulo(nave.getAngulo());
        misil.setIncX(Math.cos(Math.toRadians(misil.getAngulo())) *
                PASO_VELOCIDAD_MISIL);
        misil.setIncY(Math.sin(Math.toRadians(misil.getAngulo())) *
                PASO_VELOCIDAD_MISIL);
        tiempoMisil = (int) Math.min(this.getWidth() / Math.abs(
                misil.getIncX()), this.getHeight() / Math.abs(misil.getIncY())) - 2;
        misilActivo = true;
    }


    /**
     * Per executar el metode actualitzaFisica continuament
     */
    class ThreadJuego extends Thread{
        @Override
        public void run() {
            while(vidas > 0){
                actualizaFisica();
            }
            gameOver();
        }
    }

    /**
     * El joc finalitza
     */
    private void gameOver(){

        FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //Le pasamos el score al Game Over
        Bundle bundle = new Bundle();
        bundle.putInt("score", this.score);

        GameOver gameOver = new GameOver();
        gameOver.setArguments(bundle);

        transaction.add(R.id.container, gameOver);
        transaction.commit();

    }


    /**
     * Per al gestor de moviments de la pantalla tactil
     */
    private float mX=0, mY=0;
    private boolean disparo=false;
    @Override
    public boolean onTouchEvent (MotionEvent event) {
        super.onTouchEvent(event);
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                disparo=true;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - mX);
                float dy = Math.abs(y - mY);
                if (dy<6 && dx>6){
                    giroNave = Math.round((x - mX) / 2);
                    disparo = false;
                } else if (dx<6 && dy>6){
                    aceleracionNave = Math.round((mY - y) / 25);
                    disparo = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                giroNave = 0;
                aceleracionNave = 0;
                if (disparo){
                    ActivaMisil();
                }
                break;
        }
        mX=x; mY=y;
        return true;
    }


    /**
     * ------------------------- Maneig de la nau amb el teclat ----------------------
     */

    @Override
    public boolean onKeyDown(int codigoTecla, KeyEvent evento) {
        super.onKeyDown(codigoTecla, evento);
        // Suponemos que vamos a procesar la pulsación
        drawableNave = getContext().getResources().getDrawable(
                R.drawable.nave);
        drawableNaveFuego = getContext().getResources().getDrawable(
                R.drawable.nave_fuego);
        boolean procesada = true;
        switch (codigoTecla) {
            case KeyEvent.KEYCODE_DPAD_UP:
                aceleracionNave = +PASO_ACELERACION_NAVE;
                nave.setDrawable(drawableNaveFuego);
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                giroNave = -PASO_GIRO_NAVE;
                nave.setDrawable(drawableNaveFuego);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                giroNave = +PASO_GIRO_NAVE;
                nave.setDrawable(drawableNaveFuego);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                nave.setDrawable(drawableNave);
                aceleracionNave = -PASO_ACELERACION_NAVE;
                break;
            case KeyEvent.KEYCODE_ENTER:
                ActivaMisil();
                break;
            default: // Si estamos aquí, no hay pulsación que nos interese
                procesada = false;
                break;
        }
        return procesada;
    }
    @Override
    public boolean onKeyUp(int codigoTecla, KeyEvent evento) {
        super.onKeyUp(codigoTecla, evento);
        drawableNave = getContext().getResources().getDrawable(
                R.drawable.nave);
        nave.setDrawable(drawableNave);
        // Suponemos que vamos a procesar la pulsación
        boolean procesada = true;
        switch (codigoTecla) {
            case KeyEvent.KEYCODE_DPAD_UP:
                aceleracionNave = 0;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                giroNave = 0;
                break;
            default:
                // Si estamos aquí, no hay pulsación que nos interese
                procesada = false;
                break;
        }
        return procesada;
    }


    /**
     * GET & SET
     */
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
