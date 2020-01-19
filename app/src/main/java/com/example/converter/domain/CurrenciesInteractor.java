package com.example.converter.domain;

import androidx.annotation.NonNull;

import com.example.converter.domain.model.Currency;

import java.io.IOException;
import java.util.List;

/**
 * Интерактор для загрузки списка валют
 *
 **/
public class CurrenciesInteractor {
    private final ICurrenciesRepository mCurrenciesRepository;

    public CurrenciesInteractor(@NonNull ICurrenciesRepository currenciesRepository) {
        mCurrenciesRepository = currenciesRepository;
    }

    @NonNull
    public List<Currency> loadCurrencies() throws LoadCurrenciesException {
        try {
            return mCurrenciesRepository.loadCurrencies();
        } catch (IOException e) {
            throw new LoadCurrenciesException("Не удалось загрузить список валют", e);
        }
    }
}
