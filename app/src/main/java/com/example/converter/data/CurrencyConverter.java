package com.example.converter.data;

import androidx.annotation.NonNull;

import com.example.converter.data.model.CurrencyData;
import com.example.converter.domain.IConverter;
import com.example.converter.domain.model.Currency;

import java.util.ArrayList;

import java.util.List;

public class CurrencyConverter implements IConverter<List<CurrencyData>, List<Currency>> {



    @NonNull
    @Override
    public List<Currency> convert(@NonNull List<CurrencyData> currencies) {
        List<Currency> result = new ArrayList<>();
        for (CurrencyData currency : currencies) {
            result.add(new Currency(
                    currency.getId(),
                    currency.getCharCode(),
                    currency.getNominal(),
                    currency.getName(),
                    currency.getValue()
            ));
        }

        return result;
    }
}
