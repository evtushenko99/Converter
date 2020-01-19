package com.example.converter.data;

import androidx.annotation.NonNull;

import com.example.converter.data.model.CurrenciesData;
import com.example.converter.data.model.CurrencyData;
import com.example.converter.domain.ICurrenciesRepository;
import com.example.converter.domain.model.Currency;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class CurrenciesRepository implements ICurrenciesRepository {
    private static final String BASE_URL = "http://www.cbr.ru";
    private final IRateService mRatesApi;
    private final CurrencyConverter mCurrencyConverter;

    public CurrenciesRepository(@NonNull CurrencyConverter currencyConverter) {
        mCurrencyConverter = currencyConverter;
        Strategy strategy = new AnnotationStrategy();
        Serializer serializer = new Persister(strategy);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create(serializer))
                .build();
        mRatesApi = retrofit.create(IRateService.class);
    }

    @NonNull
    @Override
    public List<Currency> loadCurrencies() throws IOException {
        Response<CurrenciesData> response = mRatesApi.loadCurrencies().execute();
        if (response.body() == null || response.errorBody() != null) {
            throw new IOException("Не удалось загрузить список валют");
        }
        List<CurrencyData> currencies = response.body().getCurrencies();
        return mCurrencyConverter.convert(currencies);
    }
}
