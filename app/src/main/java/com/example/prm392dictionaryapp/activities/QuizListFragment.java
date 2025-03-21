package com.example.prm392dictionaryapp.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm392dictionaryapp.R;
import com.example.prm392dictionaryapp.adapters.QuizListAdapter;
import com.example.prm392dictionaryapp.entities.QuizSet;
import com.example.prm392dictionaryapp.utils.MyHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class QuizListFragment extends Fragment {
    private View view;
    private RecyclerView rvQuizList;
    private QuizListAdapter adapter;
    private ArrayList<QuizSet> quizList;
    private MyHelper quizHelper;
    private TextView tvNoQuiz;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public QuizListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_quiz_list, container, false);

        rvQuizList = view.findViewById(R.id.rv_quiz_list);
        rvQuizList.setLayoutManager(new LinearLayoutManager(getActivity()));
        quizList = new ArrayList<>();
        adapter = new QuizListAdapter(quizList);
        rvQuizList.setAdapter(adapter);

        adapter.setOnItemClickListener(new QuizListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(QuizSet quiz) {
                QuizSetDetailFragment detailFragment = new QuizSetDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putLong("quizSetId", quiz.getId());
                detailFragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, detailFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        quizHelper = new MyHelper(getActivity(), "quiz_database.db", null, 1);
        loadQuizList();

        tvNoQuiz = view.findViewById(R.id.tv_no_quiz);

        if (quizList.isEmpty()) {
            tvNoQuiz.setVisibility(View.VISIBLE);
            rvQuizList.setVisibility(View.GONE);
        } else {
            tvNoQuiz.setVisibility(View.GONE);
            rvQuizList.setVisibility(View.VISIBLE);
        }
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        quizList.clear();
        loadQuizList();
        //adapter.notifyDataSetChanged();
    }
    private void loadQuizList() {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = quizHelper.getReadableDatabase();

            String[] columns = {"id", "title", "description", "totalQuestion", "quizTime", "createdAt"};
            cursor = db.query(MyHelper.TABLE_QUIZ_SET, columns, null, null, null, null, "createdAt DESC");
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                        int totalQuestion = cursor.getInt(cursor.getColumnIndexOrThrow("totalQuestion"));
                        int time = cursor.getInt(cursor.getColumnIndexOrThrow("quizTime"));
                        String createdAtStr = cursor.getString(cursor.getColumnIndexOrThrow("createdAt"));

                        Date createdAt = null;
                        try {
                            createdAt = SDF.parse(createdAtStr);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        QuizSet quiz = QuizSet.builder().id(id).title(title).description(description).totalQuestion(totalQuestion).quizTime(time).createdAt(createdAt).build();
                        quizList.add(quiz);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error while list quiz: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

    }

}