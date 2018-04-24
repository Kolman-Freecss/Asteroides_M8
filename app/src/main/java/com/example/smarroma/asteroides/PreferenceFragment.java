package com.example.smarroma.asteroides;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by smarroma on 12/04/2018.
 */

public class PreferenceFragment extends android.preference.PreferenceFragment {

    private static final String KEY_REPRODUCIR_MUSICA_CHK = "reproducir_musica";
    private static final String KEY_TIPO_GRAFICOS = "tipo_graficos";
    private static final String KEY_NUM_FRAGMENTS_CHK = "num_fragments";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundColor(Color.WHITE);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * CARGAR LAYOUT
         */

        addPreferencesFromResource(R.xml.preferencias);

        Preference button = (Preference)getPreferenceManager().findPreference("exitButton");
        if(button!=null){
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    //Removent el fragment de preferences
                    getActivity().getFragmentManager().beginTransaction().remove(PreferenceFragment.this).commit();
                    return true;
                }
            });
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        String keyMusic = PreferenceFragment.getKeyReproducirMusicaChk();
        if(PreferenceFragment.getBoolean(getActivity(), keyMusic)){
            MainActivity.setMp(MediaPlayer.create(getActivity(), R.raw.audio));
            MainActivity.setMusicOrNot(true);
            MainActivity.getMp().start();
            MainActivity.getMp().setLooping(true);
        }else{
            if(MainActivity.getMp() != null) {
                MainActivity.setMusicOrNot(false);
                MainActivity.getMp().stop();
                MainActivity.getMp().setLooping(false);
            }
        }

    }

    /**
     * MODIFICAR PREFERENCIAS
     */

    /**
     * String Preferences
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, final String key){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(key, "");
    }

    public static void setString(Context context, final String key, final String value){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }


    /**
     * Boolean Preferences  (Para los checkbox por ejemplo)
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, final String key){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getBoolean(key, false);
    }

    public static void setBoolean(Context context, final String key, final boolean value){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }



    /**
     * Get & Set Keys
     * @return
     */
    public static String getKeyReproducirMusicaChk() {
        return KEY_REPRODUCIR_MUSICA_CHK;
    }

    public static String getKeyTipoGraficos() {
        return KEY_TIPO_GRAFICOS;
    }

    public static String getKeyNumFragmentsChk() {
        return KEY_NUM_FRAGMENTS_CHK;
    }
}
