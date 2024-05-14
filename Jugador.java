package com.example.pokards;

import android.widget.Button;
import android.widget.TextView;


import com.example.pokards.modelo.apimodels.Pokemon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Jugador {

    List<Pokemon> mazo = new ArrayList<>();//9cartas hay que recogerlas de la api o de la bbdd

    Pokemon[] mesa = new Pokemon[3];//3cartas max

    Button[] botones;

    TextView[] tvs;

    TextView tvVidas;

    int vidas;


    public Jugador(TextView tvVidas) {
        this.tvs=new TextView[3];
        this.botones=new Button[3];
        this.vidas = 6;
       this.tvVidas=tvVidas;
    }


    public boolean robar() {

        if (mesa[1] == null) {

            mazo.remove(mesa[1] = mazo.get(new Random().nextInt(mazo.size())));
            return true;

        } else if (mesa[0] == null) {

            mazo.remove(mesa[0] = mazo.get(new Random().nextInt(mazo.size())));
            return true;

        } else if (mesa[2] == null) {

            mazo.remove(mesa[2] = mazo.get(new Random().nextInt(mazo.size())));
            return true;

        } else {
            return false;
        }

    }

    public List<Pokemon> getMazo() {
        return mazo;
    }

    public void setMazo(List<Pokemon> mazo) {
        this.mazo = mazo;
    }

    public Pokemon[] getMesa() {
        return mesa;
    }

    public void setMesa(Pokemon[] mesa) {
        this.mesa = mesa;
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    public Button[] getBotones() {
        return botones;
    }

    public void setBotones(Button[] botones) {
        this.botones = botones;
    }

    public TextView[] getTvs() {
        return tvs;
    }

    public void setTvs(TextView[] tvs) {
        this.tvs = tvs;
    }

    public TextView getTvVidas() {
        return tvVidas;
    }

    public void setTvVidas(TextView tvVidas) {
        this.tvVidas = tvVidas;
    }


}