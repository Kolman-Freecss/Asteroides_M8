package com.example.smarroma.asteroides;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Vector;

/**
 * Created by smarroma on 12/04/2018.
 */

public class VistaJuego extends View {

    ////// NAVE //////
    private Grafico nave;// Gráfico de la nave
    private int giroNave; // Incremento de dirección
    private float aceleracionNave; // aumento de velocidad
    // Incremento estándar de giro y aceleración
    private static final int PASO_GIRO_NAVE = 5;
    private static final float PASO_ACELERACION_NAVE = 0.5f;

    // //// ASTEROIDES //////
    private Vector<Grafico> Asteroides; // Vector con los Asteroides
    private int numAsteroides = 5; // Número inicial de asteroides
    private int numFragmentos = 3; // Fragmentos en que se divide

    public VistaJuego(Context context, AttributeSet attrs) {
        super(context, attrs);
        Drawable drawableNave, drawableAsteroide, drawableMisil;
        // Obtenemos referencia al recurso asteroide1.png guardado en carpeta Res
        drawableAsteroide = context.getResources().getDrawable(
                R.drawable.asteroide1);

        drawableNave = context.getResources().getDrawable(
                R.drawable.nave);

        nave = new Grafico(this, drawableNave);
        //creamos los asteroides e inicializamos su velocidad, ángulo y rotación.
        //La posición inicial no la podemos obtener hasta conocer ancho y alto pantalla
        Asteroides = new Vector<Grafico>();
        for (int i = 0; i < numAsteroides; i++) {
            Grafico asteroide = new Grafico(this, drawableAsteroide);
            asteroide.setIncY(Math.random() * 4 - 2);
            asteroide.setIncX(Math.random() * 4 - 2);
            asteroide.setAngulo((int) (Math.random() * 360));
            asteroide.setRotacion((int) (Math.random() * 8 - 4));
            Asteroides.add(asteroide);
        }
    }


    //metodo que nos da ancho y alto pantalla
    @Override
    protected void onSizeChanged(int ancho, int alto,
                                 int ancho_anter, int alto_anter) {
        super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);
        // Una vez que conocemos nuestro ancho y alto situamos asteroides de forma
        //aleatoria
        for (Grafico asteroide : Asteroides) {
            asteroide.setPosX(Math.random() * (ancho - asteroide.getAncho()));
            asteroide.setPosY(Math.random() * (alto - asteroide.getAlto()));
        }

        //Colocamos la nave en el centro
        nave.setPosX(ancho / 2 - nave.getAncho() / 2);
        nave.setPosY(alto / 2 - nave.getAlto() / 2);
    }


    //metodo que dibuja la vista
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Grafico asteroide : Asteroides) {
            asteroide.dibujaGrafico(canvas);
        }
s
        nave.dibujaGrafico(canvas);

    }
}
