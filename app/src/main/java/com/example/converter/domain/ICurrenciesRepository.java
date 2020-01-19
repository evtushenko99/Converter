package com.example.converter.domain;

import androidx.annotation.NonNull;

import com.example.converter.domain.model.Currency;

import java.io.IOException;
import java.util.List;

public interface ICurrenciesRepository {
    @NonNull
    List<Currency> loadCurrencies() throws IOException;
}
