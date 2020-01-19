package com.example.converter.data.model;

import com.example.converter.data.BigDecimalConverter;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

import java.math.BigDecimal;

@Root(name = "Valute")
public class CurrencyData {
    @Attribute(name = "ID")
    private String mId;
    @Element(name = "NumCode")
    private int mNumCode;
    @Element(name = "CharCode")
    private String mCharCode;
    @Element(name = "Nominal")
    private long mNominal;
    @Element(name = "Name")
    private String mName;
    @Element(name = "Value")
    @Convert(BigDecimalConverter.class)
    private BigDecimal mValue;
    /**
     *  Используется при чтении XML
     */
    public CurrencyData() {
    }

    /**
     * Для юнит тестов
     * @param id
     * @param numCode
     * @param charCode
     * @param nominal
     * @param name
     * @param value
     */
    public CurrencyData(String id, int numCode, String charCode, long nominal, String name, BigDecimal value) {
        mId = id;
        mNumCode = numCode;
        mCharCode = charCode;
        mNominal = nominal;
        mName = name;
        mValue = value;
    }

    public String getId() {
        return mId;
    }

    public int getNumCode() {
        return mNumCode;
    }

    public String getCharCode() {
        return mCharCode;
    }

    public long getNominal() {
        return mNominal;
    }

    public String getName() {
        return mName;
    }

    public BigDecimal getValue() {
        return mValue;
    }
}
