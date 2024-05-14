package com.example.pokards.modelo.interfaces;


import com.example.pokards.modelo.apimodels.Pokemon;
import com.example.pokards.modelo.apimodels.Pokemon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PokeapiService {

    @GET("pokemon/{numPokedex}")
    Call<Pokemon> obtenerPokemon(@Path("numPokedex")String numPokedex);
}
