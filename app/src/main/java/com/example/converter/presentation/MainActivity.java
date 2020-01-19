package com.example.converter.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.converter.R;

public class MainActivity extends AppCompatActivity {
    private static final int SECOND_ITEM = 1;
    private CurrencyConverterViewModel mViewModel;
    private Spinner mSpinnerFrom;
    private Spinner mSpinnerTo;
    private EditText mFromAmount;
    private TextView mConvertedText;
    private TextView mConversionRate;
    private View mLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setupMvvm();
    }

    private void initViews() {
        mSpinnerFrom = findViewById(R.id.spinnerFrom);
        mSpinnerTo = findViewById(R.id.spinnerTo);
        findViewById(R.id.convert).setOnClickListener(v -> mViewModel.convert(
                mSpinnerFrom.getSelectedItemPosition(),
                mSpinnerTo.getSelectedItemPosition(),
                mFromAmount.getText().toString()
        ));
        mFromAmount = findViewById(R.id.fromAmount);
        mConvertedText = findViewById(R.id.convertedText);
        mLoadingView = findViewById(R.id.loading_view);
        mConversionRate = findViewById(R.id.conversionRate);
        mSpinnerFrom.setOnItemSelectedListener(new OnCurrencySelectedListener());
        mSpinnerTo.setOnItemSelectedListener(new OnCurrencySelectedListener());
    }

    private void setupMvvm() {
        mViewModel = ViewModelProviders.of(this, new CurrencyViewModelFactory(this))
                .get(CurrencyConverterViewModel.class);
        mViewModel.getCurrencies().observe(this, currencies -> {
            mSpinnerFrom.setAdapter(new CurrencyAdapter(currencies));
            mSpinnerTo.setAdapter(new CurrencyAdapter(currencies));
            mSpinnerTo.setSelection(SECOND_ITEM);
        });
        mViewModel.getConvertedText().observe(this, convertedText ->
                mConvertedText.setText(convertedText));
        mViewModel.isLoading().observe(this, isLoading ->
                mLoadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE));
        mViewModel.getConversionRate().observe(this, rate -> mConversionRate.setText(rate));
        mViewModel.getErrors().observe(this, error ->
                Toast.makeText(this, error, Toast.LENGTH_LONG).show());
        mViewModel.loadCurrencies();
    }

    private class OnCurrencySelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mViewModel.updateConversionRate(mSpinnerFrom.getSelectedItemPosition(),
                    mSpinnerTo.getSelectedItemPosition());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
