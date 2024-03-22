package com.example.acreditads2023;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface api_email {
    @GET("/v2/validate")
    Call<retorno_validacion> consulta_correo(@Query("api_key") String api_key, @Query("email") String email);
}
