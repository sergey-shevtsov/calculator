package com.example.android.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioButton;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setCheckedButton();
        setButtonsListeners();
    }

    private void setCheckedButton() {
        int themeMode = getIntent().getExtras().getInt(MainActivity.KEY_THEME);
        RadioButton checkedCheckBox;
        if (themeMode == MainActivity.THEME_DARK) {
            checkedCheckBox = findViewById(R.id.dark_theme_button);
        } else {
            checkedCheckBox = findViewById(R.id.default_theme_button);
        }
        checkedCheckBox.setChecked(true);
    }

    private void setButtonsListeners() {
        findViewById(R.id.default_theme_button).setOnClickListener(view -> {
            AppCompatDelegate.setDefaultNightMode(MainActivity.THEME_LIGHT);
            saveTheme(MainActivity.THEME_LIGHT);
        });
        findViewById(R.id.dark_theme_button).setOnClickListener(view -> {
            AppCompatDelegate.setDefaultNightMode(MainActivity.THEME_DARK);
            saveTheme(MainActivity.THEME_DARK);
        });
        findViewById(R.id.apply_settings_button).setOnClickListener(view -> {
            finish();
        });
    }

    private void saveTheme(int themeMode) {
        SharedPreferences sharedPref = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(MainActivity.KEY_THEME, themeMode);
        editor.apply();
    }
}