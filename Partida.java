package com.example.pokards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pokards.modelo.apimodels.Pokemon;
import com.example.pokards.modelo.interfaces.PokeapiService;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Partida extends AppCompatActivity {

    Button[] atacantes;
    Button[] defensores;
    TextView[] tvAtacantes;
    TextView[] tvDefensores;
    Button seleccionadoAbajo;
    Button seleccionadoArriba;
    private static Jugador jugadorAbajo;
    private static Jugador jugadorArriba;
    TextView tvVidasArriba;
    TextView tvVidasAbajo;
    private static Retrofit retrofit;

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        atacantes = new Button[3];
        defensores = new Button[3];
        tvAtacantes = new TextView[3];
        tvDefensores = new TextView[3];
        tvVidasAbajo = (TextView) findViewById(R.id.tvVidasAbajo);
        tvVidasArriba = (TextView) findViewById(R.id.tvVidasArriba);
        jugadorArriba=new Jugador(tvVidasArriba);
        jugadorAbajo=new Jugador(tvVidasAbajo);
        this.rellenarMazos();

        (jugadorAbajo.tvs[0] = (TextView) findViewById(R.id.tv1)).setVisibility(View.INVISIBLE);
        (jugadorAbajo.botones[0] = (Button) findViewById(R.id.btnPok1Abajo)).setVisibility(View.INVISIBLE);

        jugadorAbajo.tvs[1] = (TextView) findViewById(R.id.tv2);
        jugadorAbajo.botones[1] = (Button) findViewById(R.id.btnPok2Abajo);

        (jugadorAbajo.tvs[2] = (TextView) findViewById(R.id.tv3)).setVisibility(View.INVISIBLE);
        (jugadorAbajo.botones[2] = (Button) findViewById(R.id.btnPok3Abajo)).setVisibility(View.INVISIBLE);

        (jugadorArriba.tvs[0] = (TextView) findViewById(R.id.tv4)).setVisibility(View.INVISIBLE);
        (jugadorArriba.botones[0] = (Button) findViewById(R.id.btnPok1Arriba)).setVisibility(View.INVISIBLE);

        jugadorArriba.tvs[1] = (TextView) findViewById(R.id.tv5);
        jugadorArriba.botones[1] = (Button) findViewById(R.id.btnPok2Arriba);

        (jugadorArriba.tvs[2] = (TextView) findViewById(R.id.tv6)).setVisibility(View.INVISIBLE);
        (jugadorArriba.botones[2] = (Button) findViewById(R.id.btnPok3Arriba)).setVisibility(View.INVISIBLE);

    }



    public void seleccionadoArriba(View view) {
        this.seleccionadoArriba = (Button) view;
        this.realizarAtaque();
    }

    public void seleccionadoAbajo(View view) {
        this.seleccionadoAbajo = (Button) view;
        this.realizarAtaque();
    }
    public void robarArriba(View view) {
        this.robar(0);
        this.actualizarMesa();

    }
    public void robarAbajo(View view) {
        this.robar(1);
        this.actualizarMesa();
    }
    public static void obtenerPokemonApi(String pokedex, Jugador jugador) {

        PokeapiService service= retrofit.create(PokeapiService.class);
        Call<Pokemon> pokemonCall=service.obtenerPokemon(pokedex);

        pokemonCall.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                Pokemon pokemon = response.body();
                jugador.mazo.add(pokemon);

                Partida.rellenarMazos();
            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {

            }
        });
    }
    public static void rellenarMazos(){

    if(jugadorArriba.mazo.size()<9) {
        obtenerPokemonApi(String.valueOf(new Random().nextInt(20)), jugadorArriba);
    }
    else if(jugadorAbajo.mazo.size()<9) {
        obtenerPokemonApi(String.valueOf(new Random().nextInt(20)), jugadorAbajo);
    }
    else{
        roboInicial();
        actualizarMesa();
    }
    }

    public static void roboInicial(){

        jugadorArriba.mazo.remove(jugadorArriba.mesa[1]=jugadorArriba.mazo.get(new Random().nextInt(jugadorArriba.mazo.size())));
        jugadorAbajo.mazo.remove(jugadorAbajo.mesa[1]=jugadorAbajo.mazo.get(new Random().nextInt(jugadorAbajo.mazo.size())));

    }
    public static int comprobarGanador(){
        if(jugadorArriba.mesa[0]==null&&jugadorArriba.mesa[1]==null&&jugadorArriba.mesa[2]==null || jugadorArriba.vidas<=0){
            return 2;
        }
        else if(jugadorAbajo.mesa[0]==null&&jugadorAbajo.mesa[1]==null&&jugadorAbajo.mesa[2]==null || jugadorAbajo.vidas<=0){
            return 1;
        }
        else {
            return 0;
        }
    }

    //recarga todas las cartas asociadas a los botones y el ataque y la vida de estas cartas
    //si la carta muere hace que sea null para que se pueda robar una nueva y hace invisible su boton
    //si aparecen nuevas cartas en el array de la mesa hace sus botones visibles de nuevo
    //también actualiza el texto de los botones para que sea igual al nombre del pokemon correspondiente del array
    //
    //DARLE UNA VUELTA A ESTO ULTIMO PARA MAYOR EFICIENCIA
    //

    public static void actualizarMesa() {

        for (int i = 0; i < 3; i++) {
            if (jugadorAbajo.mesa[i] != null) {
                jugadorAbajo.getBotones()[i].setText(jugadorAbajo.mesa[i].getName());
                jugadorAbajo.getTvs()[i].setVisibility(View.VISIBLE);

                jugadorAbajo.mesa[i].setEstadisticas(jugadorAbajo.getTvs()[i]);
                jugadorAbajo.getBotones()[i].setTag(jugadorAbajo.mesa[i]);
                if (!(jugadorAbajo.mesa[i]).actualizarEstadisticas()) {

                    jugadorAbajo.mesa[i] = null;
                    jugadorAbajo.vidas=(jugadorAbajo.vidas-1);
                    jugadorAbajo.getBotones()[i].setVisibility(View.INVISIBLE);

                } else {
                    jugadorAbajo.getBotones()[i].setVisibility(View.VISIBLE);
                }
            }
            if (jugadorArriba.mesa[i] != null) {
                jugadorArriba.getBotones()[i].setText(jugadorArriba.mesa[i].getName());
                jugadorArriba.getTvs()[i].setVisibility(View.VISIBLE);

                jugadorArriba.mesa[i].setEstadisticas(jugadorArriba.getTvs()[i]);
                jugadorArriba.getBotones()[i].setTag(jugadorArriba.mesa[i]);
                if (!(jugadorArriba.mesa[i]).actualizarEstadisticas()) {

                    jugadorArriba.mesa[i] = null;
                    jugadorArriba.vidas=(jugadorArriba.vidas-1);
                    jugadorArriba.getBotones()[i].setVisibility(View.INVISIBLE);

                } else {
                    jugadorArriba.getBotones()[i].setVisibility(View.VISIBLE);
                }
            }
        }
        jugadorAbajo.tvVidas.setText(String.valueOf(jugadorAbajo.vidas));
        jugadorArriba.tvVidas.setText(String.valueOf(jugadorArriba.vidas));
        int result=comprobarGanador();
        if(result!=0){

            Intent intent = new Intent(context, PantallaFin.class);

            intent.putExtra("ganador", result);

            context.startActivity(intent);
        }

        //hay que ver como se hace esto

    }



    //cambiar para que solo actualice la salud del pokemon y llame a actualizar mesa
    public void realizarAtaque() {
        // Verifica que ambas cartas hayan sido seleccionados
        if (seleccionadoAbajo != null && seleccionadoArriba != null) {
            // Realiza el ataque entre las dos cartas seleccionadas
            String nombrePersonajeAbajo = seleccionadoAbajo.getText().toString();
            String nombrePersonajeArriba = seleccionadoArriba.getText().toString();

            this.actualizarSalud((Pokemon) seleccionadoAbajo.getTag(), (Pokemon) seleccionadoArriba.getTag());
            Toast.makeText(this, "Ataque entre " + nombrePersonajeAbajo + " y " + nombrePersonajeArriba, Toast.LENGTH_SHORT).show();

            seleccionadoAbajo = null;
            seleccionadoArriba = null;
        } else {
            Toast.makeText(this, "Primero selecciona un personaje de cada fila", Toast.LENGTH_SHORT).show();
        }
    }

    public void robar(int jugador){
        if(jugador==0){
            jugadorArriba.robar();
        }
        else {
            jugadorAbajo.robar();
        }
    }


    //falta añadir los tipos con la api
    //no debera llamar a actualizar mesa, que lo llamen desde los metodos que lo necesiten directamente seria mejor
    public void actualizarSalud(Pokemon ataca, Pokemon recibe) {


        ataca.setStat(0, (ataca.getStat(0) - recibe.getStat(1)));
        recibe.setStat(0, (recibe.getStat(0) - ataca.getStat(1)));

        this.actualizarMesa();

    }


}
