package com.example.prm392dictionaryapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.prm392dictionaryapp.R;
import com.example.prm392dictionaryapp.adapters.ViewPagerAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class QuizActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;
    private MaterialToolbar btnBackHomePage;
    private final int[] tabIcons = {
            R.drawable.ic_list,
            R.drawable.ic_create,
            R.drawable.ic_history
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_main);

        viewPager2 = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        btnBackHomePage = findViewById(R.id.btnBackHomePage);

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        adapter.addFragment(new QuizListFragment());
        adapter.addFragment(new QuizCreateFragment());
        adapter.addFragment(new QuizHistoryFragment());

        viewPager2.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            tab.setIcon(tabIcons[position]);
        }).attach();

        btnBackHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizActivity.this, HomepageActivity.class);
                startActivity(intent);
            }
        });
    }
}