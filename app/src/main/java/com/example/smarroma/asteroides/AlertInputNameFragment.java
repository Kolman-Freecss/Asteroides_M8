package com.example.smarroma.asteroides;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by smarroma on 11/04/2018.
 */

public class AlertInputNameFragment extends DialogFragment implements View.OnClickListener{

    Button btConfirmar, btVolver;
    EditText inputName;
    AlertInputNameFragmentListener inputNameListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        inputNameListener = (AlertInputNameFragmentListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.my_inputname_dialog, null);
        btConfirmar = (Button) view.findViewById(R.id.btConfirmar);
        btVolver = (Button) view.findViewById(R.id.btVolver);
        inputName = (EditText) view.findViewById(R.id.inputName);
        btConfirmar.setOnClickListener(this);
        btVolver.setOnClickListener(this);
        //Por si le da a algun lado de la pantalla no se cierre el dialogo
        setCancelable(false);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btConfirmar){
            if(MainActivity.getMp() != null) {
                MainActivity.getMp().stop();
            }
            this.inputNameListener.onConfirmarButtonClick(this.inputName.getText().toString());
            Intent i = new Intent(getActivity(), Juego.class );
            startActivity(i);

        }else if(v.getId()==R.id.btVolver){
            dismiss();
        }
    }

    public interface AlertInputNameFragmentListener{

        void onConfirmarButtonClick(String name);

    }

}
