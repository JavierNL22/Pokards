package com.example.pokards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pokards.modelo.apimodels.Pokemon;
import com.example.pokards.modelo.interfaces.PokeapiService;

import java.util.Random;
import java.util.concurrent.Semaphore;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Button[] atacantes;
    Button[] defensores;
    TextView[] tvAtacantes;
    TextView[] tvDefensores;
    Button seleccionadoAbajo;
    Button seleccionadoArriba;
    private Jugador jugadorAbajo;
    private Jugador jugadorArriba;
    TextView tvVidasArriba;
    TextView tvVidasAbajo;
    private Retrofit retrofit;

    private Semaphore semaphore = new Semaphore(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        jugadorArriba=new Jugador();
        jugadorAbajo=new Jugador();
        this.rellenarMazos();
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        (tvAtacantes[0] = (TextView) findViewById(R.id.tv1)).setVisibility(View.INVISIBLE);
        (atacantes[0] = (Button) findViewById(R.id.btnPok1Abajo)).setVisibility(View.INVISIBLE);

        tvAtacantes[1] = (TextView) findViewById(R.id.tv2);
        atacantes[1] = (Button) findViewById(R.id.btnPok2Abajo);

        (tvAtacantes[2] = (TextView) findViewById(R.id.tv3)).setVisibility(View.INVISIBLE);
        (atacantes[2] = (Button) findViewById(R.id.btnPok3Abajo)).setVisibility(View.INVISIBLE);

        (tvDefensores[0] = (TextView) findViewById(R.id.tv4)).setVisibility(View.INVISIBLE);
        (defensores[0] = (Button) findViewById(R.id.btnPok1Arriba)).setVisibility(View.INVISIBLE);

        tvDefensores[1] = (TextView) findViewById(R.id.tv5);
        defensores[1] = (Button) findViewById(R.id.btnPok2Arriba);

        (tvDefensores[2] = (TextView) findViewById(R.id.tv6)).setVisibility(View.INVISIBLE);
        (defensores[2] = (Button) findViewById(R.id.btnPok3Arriba)).setVisibility(View.INVISIBLE);
        this.roboInicial();
        this.actualizarMesa();


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
    public void obtenerPokemonApi() {

        PokeapiService service= retrofit.create(PokeapiService.class);
        Call<Pokemon> pokemonCall=service.obtenerPokemon();

        pokemonCall.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                Pokemon pokemon = response.body();

                jugadorAbajo.mazo.add(pokemon);
                jugadorArriba.mazo.add(pokemon);

            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable t) {

            }
        });
    }
    public void rellenarMazos(){
        for (int i=0; i<9; i++){
            this.obtenerPokemonApi();
            this.obtenerPokemonApi();
        }
        semaphore.release();
    }
    public void roboInicial(){

        jugadorArriba.mazo.remove(jugadorArriba.mesa[1]=jugadorArriba.mazo.get(new Random().nextInt(jugadorArriba.mazo.size())));
        jugadorAbajo.mazo.remove(jugadorAbajo.mesa[1]=jugadorAbajo.mazo.get(new Random().nextInt(jugadorAbajo.mazo.size())));

    }
    public int comprobarGanador(){
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

    public void actualizarMesa() {

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

        //hay que ver como se hace esto
        if (this.comprobarGanador() != 0) {

            Intent intent = new Intent(this, PantallaFin.class);
            intent.putExtra("ganador", this.comprobarGanador());
            startActivity(intent);
        }
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
