package com.example.converter.domain;

import android.content.Context;

import com.example.converter.R;
import com.example.converter.domain.model.Currency;
import com.example.converter.presentation.Utils.IResourceWrapper;
import com.example.converter.presentation.Utils.ResourceWrapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ConversionInteractorTest {
    private static final int INTERNAL_SCALE = 5;
    private static final int PUBLIC_SCALE = 2;
    @Mock
    private ResourceWrapper mResourceWrapper;
    @Mock
    private  Context mApplicationContext;
    private NumberFormat mNumberFormat = new DecimalFormat("#.##");
    private NumberFormat mCurrencyFormat = new DecimalFormat("#.#####");

    private ConversionInteractor mConversionInteractor;

    @Before
    public void setUp() throws Exception {
        mConversionInteractor = new ConversionInteractor(mResourceWrapper);
        mResourceWrapper = new ResourceWrapper(mApplicationContext.getResources());
    }

    @Test
    public void formatConversionRate() {

        List<Currency> currencies = new ArrayList<>(createCurrencyList());

        mConversionInteractor.formatConversionRate(currencies, 0, 1);

        Currency base = currencies.get(0);
        Currency quoted = currencies.get(1);
        BigDecimal rate = base.getValue()
                .multiply(new BigDecimal(quoted.getNominal()))
                .divide(quoted.getValue(), INTERNAL_SCALE, RoundingMode.HALF_UP)
                .divide(new BigDecimal(base.getNominal()), INTERNAL_SCALE, RoundingMode.HALF_UP);
        String formattedRate = mCurrencyFormat.format(rate);

        Mockito.when(mResourceWrapper.getString(R.string.conversion_rate, formattedRate, base.getCharCode(), quoted.getCharCode()))
                .thenReturn(formattedRate + " " + base.getCharCode() + " " + quoted.getCharCode());
        assertEquals(formattedRate + " " + base.getCharCode() + " " + quoted.getCharCode(),"0.01625 RUB/USD" );

    }

    private List<Currency> createCurrencyList() {
        List<Currency> currencies = new ArrayList<>();
        BigDecimal usd = new BigDecimal("30.9436");

        currencies.add(new Currency(
                "usd_id",
                "USD",
                1,
                "Доллар США",
                usd));
        currencies.add(new Currency(
                "rub_id",
                "RUB",
                1,
                "Российский рубль",
                BigDecimal.ONE
        ));

        return currencies;
    }

    @Test
    public void convert() {
    }
}