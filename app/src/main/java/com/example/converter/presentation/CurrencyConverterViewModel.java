package com.example.converter.presentation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.converter.R;
import com.example.converter.domain.ConversionInteractor;
import com.example.converter.domain.CurrenciesInteractor;
import com.example.converter.domain.LoadCurrenciesException;
import com.example.converter.domain.SingleLiveEvent;
import com.example.converter.domain.model.Currency;
import com.example.converter.presentation.Utils.IResourceWrapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Executor;

public class CurrencyConverterViewModel extends ViewModel {
    private final CurrenciesInteractor mCurrenciesInteractor;
    private final Executor mExecutor;
    private final MutableLiveData<List<Currency>> mCurrencies = new MutableLiveData<>();
    private final MutableLiveData<String> mConvertedText = new MutableLiveData<>();
    private final MutableLiveData<String> mConversionRate = new MutableLiveData<>();
    private final SingleLiveEvent<String> mErrors = new SingleLiveEvent<>();
    private final MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>();
    private final IResourceWrapper mResourceWrapper;
    private final ConversionInteractor mConversionInteractor;
    private final Currency mRub;

    CurrencyConverterViewModel(
            @NonNull CurrenciesInteractor currenciesInteractor,
            @NonNull Executor executor,
            @NonNull IResourceWrapper resourceWrapper,
            @NonNull ConversionInteractor conversionInteractor) {
        mCurrenciesInteractor = currenciesInteractor;
        mExecutor = executor;
        mResourceWrapper = resourceWrapper;
        mConversionInteractor = conversionInteractor;
        mIsLoading.setValue(false);
        mRub = new Currency(
                "rub_id",
                "RUB",
                1,
                mResourceWrapper.getString(R.string.russian_ruble),
                BigDecimal.ONE
        );
    }

    /**
     * Загружает список валют
     */
    void loadCurrencies() {
        mIsLoading.setValue(true);
        mExecutor.execute(() -> {
            try {
                List<Currency> currencies = mCurrenciesInteractor.loadCurrencies();
                if (!currencies.contains(mRub)) {
                    currencies.add(0, mRub);
                }
                mCurrencies.postValue(currencies);
            } catch (LoadCurrenciesException e) {
                mErrors.postValue(mResourceWrapper.getString(R.string.error_loading_currencies));
            }
            mIsLoading.postValue(false);
        });
    }

    /**
     * Список валют
     */
    @NonNull
    LiveData<List<Currency>> getCurrencies() {
        return mCurrencies;
    }

    /**
     * Курс конверсии
     */
    @NonNull
    LiveData<String> getConversionRate() {
        return mConversionRate;
    }

    /**
     * Строка с информацией о конвертации (когда нажали "конвертировать")
     */
    @NonNull
    LiveData<String> getConvertedText() {
        return mConvertedText;
    }

    /**
     * Идёт ли загрузка
     */
    @NonNull
    LiveData<Boolean> isLoading() {
        return mIsLoading;
    }

    /**
     * Ошибки
     */
    @NonNull
    LiveData<String> getErrors() {
        return mErrors;
    }

    /**
     * Обновляет информацию о курсе валют
     *
     * @param fromCurrencyWithIndex индекс базовой валюты
     * @param toCurrencyWithIndex   индекс котируемой валюты
     */
    void updateConversionRate(int fromCurrencyWithIndex, int toCurrencyWithIndex) {
        String updatedConversionRate = mConversionInteractor
                .formatConversionRate(mCurrencies.getValue(), fromCurrencyWithIndex, toCurrencyWithIndex);
        if (updatedConversionRate != null) {
            mConversionRate.setValue(updatedConversionRate);
        }
    }

    /**
     * Выполняет конвертацию между выбранными валютами и введённой суммой
     *
     * @param fromCurrencyWithIndex индекс базовой валюты
     * @param toCurrencyWithIndex   индекс котируемой валюты
     * @param amount                сумма, введённая пользователем as is (ввод может быть некорректным)
     */
    void convert(int fromCurrencyWithIndex, int toCurrencyWithIndex, @Nullable String amount) {
        List<Currency> currencies = mCurrencies.getValue();
        String converted = mConversionInteractor.convert(currencies, fromCurrencyWithIndex, toCurrencyWithIndex, amount);
        if (converted == null) {
            mErrors.setValue(mResourceWrapper.getString(R.string.conversion_error));
        } else {
            mConvertedText.setValue(converted);
        }
    }
}
