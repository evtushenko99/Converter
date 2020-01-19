package com.example.converter.presentation;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.converter.data.CurrenciesRepository;
import com.example.converter.data.CurrencyConverter;
import com.example.converter.domain.ConversionInteractor;
import com.example.converter.domain.CurrenciesInteractor;
import com.example.converter.domain.ICurrenciesRepository;
import com.example.converter.presentation.Utils.ResourceWrapper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CurrencyViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    private final Context mApplicationContext;


    public CurrencyViewModelFactory(@NonNull Context applicationContext) {
        mApplicationContext = applicationContext.getApplicationContext();
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (CurrencyConverterViewModel.class.equals(modelClass)) {
            ICurrenciesRepository currenciesRepository = new CurrenciesRepository(new CurrencyConverter());
            CurrenciesInteractor interactor = new CurrenciesInteractor(currenciesRepository);
            Executor executor = Executors.newSingleThreadExecutor();
            ResourceWrapper resourceWrapper = new ResourceWrapper(mApplicationContext.getResources());
            // noinspection unchecked
            return (T) new CurrencyConverterViewModel(
                    interactor,
                    executor,
                    resourceWrapper,
                    new ConversionInteractor(resourceWrapper));
        } else {
            return super.create(modelClass);
        }
    }
}
