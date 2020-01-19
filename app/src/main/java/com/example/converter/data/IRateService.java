package com.example.converter.data;

import com.example.converter.data.model.CurrenciesData;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IRateService {

    @GET("scripts/XML_daily.asp")
    Call<CurrenciesData> loadCurrencies();
}
