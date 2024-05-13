package com.example.pokards.modelo.apimodels;

import android.widget.TextView;

import java.util.ArrayList;


public class Pokemon {

    private String name;
    private ArrayList<Stat> stats;
    private ArrayList<Type> types;
    private Sprite sprites;


    private transient TextView estadisticas;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Stat> getStats() {
        return stats;
    }

    public void setStats(ArrayList<Stat> stats) {
        this.stats = stats;
    }

    public ArrayList<Type> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<Type> types) {
        this.types = types;
    }

    public Sprite getSprites() {
        return sprites;
    }

    public void setSprites(Sprite sprites) {
        this.sprites = sprites;
    }

    public void setEstadisticas(TextView estadisticas) {
        this.estadisticas = estadisticas;
    }

    public boolean actualizarEstadisticas() {
        if(this.getStat(0)<=0){
            this.estadisticas.setText("muerto");
            return false;
        }
        else {
            this.estadisticas.setText(this.getStat(1) + " " + this.getStat(0));
            return true;
        }
    }
    public int getStat(int index){
        return stats.get(index).getBase_stat();
    }
    public void setStat(int index, int newValue){
        stats.get(index).setBase_stat(newValue);
    }
}
