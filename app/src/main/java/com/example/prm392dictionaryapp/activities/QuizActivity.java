package com.example.prm392dictionaryapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.prm392dictionaryapp.R;
import com.example.prm392dictionaryapp.adapters.ViewPagerAdapter;
import com.example.prm392dictionaryapp.utils.MyHelper;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class QuizActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;
    private final int[] tabIcons = {
            R.drawable.ic_list,
            R.drawable.ic_create,
            R.drawable.ic_quiz,
            R.drawable.ic_history
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_main);

        viewPager2 = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        adapter.addFragment(new QuizListFragment());
        adapter.addFragment(new QuizCreateFragment());
//        adapter.addFragment(new TakeQuizFragment());
//        adapter.addFragment(new QuizHistoryFragment());

        viewPager2.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            tab.setIcon(tabIcons[position]);
        }).attach();

    }
}