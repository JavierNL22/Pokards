package com.example.pokards.modelo.interfaces;


import com.example.pokards.modelo.apimodels.Pokemon;
import com.example.pokards.modelo.apimodels.Pokemon;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PokeapiService {

    @GET("pokemon/ditto")
    Call<Pokemon> obtenerPokemon();
}
