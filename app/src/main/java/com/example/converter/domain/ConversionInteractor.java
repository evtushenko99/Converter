package com.example.converter.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.converter.R;
import com.example.converter.domain.model.Currency;
import com.example.converter.presentation.Utils.IResourceWrapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;


/**
 * Интерактор, отвечающий за операции, связанные с конвертацией валют
 **/
public class ConversionInteractor {


     private static final int INTERNAL_SCALE = 5;

    private static final int PUBLIC_SCALE = 2;
    private final IResourceWrapper mResourceWrapper;
    private final NumberFormat mNumberFormat = new DecimalFormat("#.##");
    private final NumberFormat mCurrencyFormat = new DecimalFormat("#.#####");

    public ConversionInteractor(@NonNull IResourceWrapper resourceWrapper) {
        mResourceWrapper = resourceWrapper;
    }


    @Nullable
    public String formatConversionRate(@Nullable List<Currency> currencies, int fromCurrencyWithIndex, int toCurrencyWithIndex) {
        if (currencies == null ||
                currencies.isEmpty() ||
                currencies.size() <= Math.max(fromCurrencyWithIndex, toCurrencyWithIndex)) {
            return null;
        }
        Currency base = currencies.get(fromCurrencyWithIndex);
        Currency quoted = currencies.get(toCurrencyWithIndex);
        BigDecimal rate = base.getValue()
                .multiply(new BigDecimal(quoted.getNominal()))
                .divide(quoted.getValue(), INTERNAL_SCALE, RoundingMode.HALF_UP)
                .divide(new BigDecimal(base.getNominal()), INTERNAL_SCALE, RoundingMode.HALF_UP);
        String formattedRate = mCurrencyFormat.format(rate);
        return mResourceWrapper.getString(R.string.conversion_rate, formattedRate, base.getCharCode(), quoted.getCharCode());
    }


    @Nullable
    public String convert(@Nullable List<Currency> currencies,
                          int fromCurrencyWithIndex,
                          int toCurrencyWithIndex,
                          @Nullable String amount) {
        BigDecimal parsedAmount = tryParseAmount(amount);
        if (currencies == null ||
                currencies.isEmpty() ||
                currencies.size() <= Math.max(fromCurrencyWithIndex, toCurrencyWithIndex) ||
                parsedAmount == null) {
            return null;
        }
        Currency base = currencies.get(fromCurrencyWithIndex);
        Currency quoted = currencies.get(toCurrencyWithIndex);
        BigDecimal result = parsedAmount
                .multiply(base.getValue())
                .multiply(new BigDecimal(quoted.getNominal()))
                .divide(quoted.getValue(), INTERNAL_SCALE, RoundingMode.HALF_UP)
                .divide(new BigDecimal(base.getNominal()), INTERNAL_SCALE, RoundingMode.HALF_UP);
        try {
            String formattedResult = mNumberFormat.format(result.setScale(PUBLIC_SCALE, RoundingMode.HALF_UP));
            return mResourceWrapper.getString(R.string.you_will_get, formattedResult, quoted.getCharCode());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }


    @Nullable
    private BigDecimal tryParseAmount(@Nullable String amount) {
        BigDecimal result;
        if (amount == null) {
            result = null;
        } else {
            try {
                result = new BigDecimal(amount);
            } catch (NumberFormatException e) {
                result = null;
            }
        }
        return result;
    }
}
