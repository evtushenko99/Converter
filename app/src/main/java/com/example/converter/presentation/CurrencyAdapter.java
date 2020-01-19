package com.example.converter.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.converter.R;
import com.example.converter.domain.model.Currency;

import java.util.ArrayList;
import java.util.List;

public class CurrencyAdapter extends BaseAdapter {
    private final List<Currency> mCurrencies;

    public CurrencyAdapter(@NonNull List<Currency> currencies) {
        mCurrencies = new ArrayList<>(currencies);
    }

    @Override
    public int getCount() {
        return mCurrencies.size();
    }

    @Override
    public Object getItem(int position) {
        return mCurrencies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.support_simple_spinner_dropdown_item, parent, false);
            Holder holder = new Holder(convertView);
            convertView.setTag(holder);
        }
        Currency currency = (Currency) getItem(position);
        Holder holder = (Holder) convertView.getTag();
        if (currency != null){
            String text = currency.getName();
            holder.mTextView.setText(text);
        }
        return convertView;
    }

    private static class Holder {
        private TextView mTextView;

        public Holder(View convertView) {
            mTextView = convertView.findViewById(android.R.id.text1);
        }
    }
}
