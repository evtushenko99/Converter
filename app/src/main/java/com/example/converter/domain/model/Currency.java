package com.example.converter.domain.model;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.util.Objects;

public class Currency {
    private final String mId;
    private final String mCharCode;
    private final long mNominal;
    private final String mName;
    private final BigDecimal mValue;

    public Currency(
            @NonNull String id,
            @NonNull String charCode,
            long nominal,
            @NonNull String name,
            @NonNull BigDecimal value
    ) {
        mId = id;
        mCharCode = charCode;
        mNominal = nominal;
        mName = name;
        mValue = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Currency currency = (Currency) o;
        return mNominal == currency.mNominal &&
                mId.equals(currency.mId) &&
                mCharCode.equals(currency.mCharCode) &&
                mName.equals(currency.mName) &&
                mValue.equals(currency.mValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId, mCharCode, mNominal, mName, mValue);
    }

    @NonNull
    public String getId() {
        return mId;
    }

    @NonNull
    public String getCharCode() {
        return mCharCode;
    }

    public long getNominal() {
        return mNominal;
    }

    @NonNull
    public String getName() {
        return mName;
    }

    @NonNull
    public BigDecimal getValue() {
        return mValue;
    }
}
