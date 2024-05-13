package com.example.pokards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PantallaFin extends AppCompatActivity {

    TextView tvGanador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_fin);
        tvGanador=(TextView) findViewById(R.id.tvGanador);

        if(getIntent().getIntExtra( "ganador",0 )==1){
            tvGanador.setText("Gana jugador Arriba");
        }
        else {
            tvGanador.setText("Gana jugador Abajo");
        }
    }

    public void irMenu(View view) {
        Intent intent = new Intent(this, MenuInicial.class);
        startActivity(intent);

    }

    public void reiniciar(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}