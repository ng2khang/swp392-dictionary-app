package com.example.prm392dictionaryapp.activities;
import com.example.prm392dictionaryapp.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingActivity extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState) {
        Switch themeSwitch;
//        RadioGroup readingModeGroup;
        Button btnBackToHomePage;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        themeSwitch = findViewById(R.id.theme_switch);
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        Boolean isDarkMode = preferences.getBoolean("dark_mode", false);
        themeSwitch.setChecked(isDarkMode);
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("dark_mode", isChecked);
            editor.apply();
        });
//        readingModeGroup = findViewById(R.id.reading_mode_group);
//        readingModeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (checkedId == R.id.radio_day) {
//
//                } else if (checkedId == R.id.radio_night) {
//
//                }
//            }
//        });
        btnBackToHomePage = findViewById(R.id.btnBackToHomePage);
        btnBackToHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, HomepageActivity.class);
                startActivity(intent);
            }
        });

    }
}
