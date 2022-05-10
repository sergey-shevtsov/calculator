package com.example.android.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private final static String keyCalculatorLogic = "CalculatorLogic";
    final static String PREFS_NAME = "theme_prefs";
    final static String KEY_THEME = "prefs.theme";
    final static int THEME_LIGHT = AppCompatDelegate.MODE_NIGHT_NO;
    final static int THEME_DARK = AppCompatDelegate.MODE_NIGHT_YES;

    private TextView mainTextView;
    private TextView supportingTextView;
    private CalculatorLogic calculatorLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme(getSavedThemeMode());
        setContentView(R.layout.activity_main);

        calculatorLogic = new CalculatorLogic(this);
        initTextViews();
        setCalculatorListeners();
        setSettingsButtonListener();
    }

    private void initTheme(int themeMode) {
        AppCompatDelegate.setDefaultNightMode(themeMode);
        saveTheme(themeMode);
    }

    private void saveTheme(int themeMode) {
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(KEY_THEME, themeMode);
        editor.apply();
    }

    private int getSavedThemeMode() {
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return sharedPref.getInt(KEY_THEME, THEME_LIGHT);
    }

    private void setSettingsButtonListener() {
        findViewById(R.id.open_settings_button).setOnClickListener(view -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra(KEY_THEME, AppCompatDelegate.getDefaultNightMode());
            startActivity(intent);
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(keyCalculatorLogic, calculatorLogic);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        calculatorLogic = savedInstanceState.getParcelable(keyCalculatorLogic);
        printData();
    }

    private void initTextViews() {
        mainTextView = findViewById(R.id.main_text_view);
        supportingTextView = findViewById(R.id.supporting_text_view);
    }

    private void setCalculatorListeners() {
        setNumberListeners();
        setFormatListeners();
        setOperationListeners();
    }

    private void setNumberListeners() {
        View.OnClickListener numberListener = view -> {
            Button button = (Button) view;
            String buttonValue = button.getText().toString();
            calculatorLogic.numKeyProcessing(buttonValue);
            printData();
        };
        findViewById(R.id.button_1).setOnClickListener(numberListener);
        findViewById(R.id.button_2).setOnClickListener(numberListener);
        findViewById(R.id.button_3).setOnClickListener(numberListener);
        findViewById(R.id.button_4).setOnClickListener(numberListener);
        findViewById(R.id.button_5).setOnClickListener(numberListener);
        findViewById(R.id.button_6).setOnClickListener(numberListener);
        findViewById(R.id.button_7).setOnClickListener(numberListener);
        findViewById(R.id.button_8).setOnClickListener(numberListener);
        findViewById(R.id.button_9).setOnClickListener(numberListener);
        findViewById(R.id.button_0).setOnClickListener(numberListener);
        findViewById(R.id.button_00).setOnClickListener(numberListener);
        findViewById(R.id.button_point).setOnClickListener(numberListener);
    }

    private void setFormatListeners() {
        findViewById(R.id.button_backspace).setOnClickListener(view -> {
            calculatorLogic.backspaceKeyProcessing();
            printData();
        });
        findViewById(R.id.button_reset).setOnClickListener(view -> {
            calculatorLogic.resetKeyProcessing();
            printData();
        });
    }

    private void setOperationListeners() {
        View.OnClickListener operandListener = view -> {
            Button button = (Button) view;
            char operand = button.getText().charAt(0);
            calculatorLogic.operandKeyProcessing(operand);
            printData();
        };
        findViewById(R.id.button_plus).setOnClickListener(operandListener);
        findViewById(R.id.button_minus).setOnClickListener(operandListener);
        findViewById(R.id.button_multiply).setOnClickListener(operandListener);
        findViewById(R.id.button_division).setOnClickListener(operandListener);
        findViewById(R.id.button_equally).setOnClickListener(view -> {
            calculatorLogic.equallyKeyProcessing();
            printData();
        });
        findViewById(R.id.button_percent).setOnClickListener(view -> {
            calculatorLogic.percentKeyProcessing();
            printData();
        });
    }

    private void printData() {
        mainTextView.setText(String.format(Locale.getDefault(), "%s", calculatorLogic.getResult()));
        supportingTextView.setText(String.format(Locale.getDefault(), "%s", calculatorLogic.getPreResult()));
    }
}