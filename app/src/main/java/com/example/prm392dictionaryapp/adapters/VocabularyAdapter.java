package com.example.prm392dictionaryapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392dictionaryapp.R;
import com.example.prm392dictionaryapp.activities.VocabDetailActivity;
import com.example.prm392dictionaryapp.entities.Vocabulary;

import java.util.ArrayList;

public class VocabularyAdapter extends RecyclerView.Adapter<VocabularyAdapter.ViewHolder> {

    ArrayList<Vocabulary> arrayList = new ArrayList<>();
    Context context;

    public VocabularyAdapter(ArrayList<Vocabulary> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public VocabularyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.vocab_list_card,parent,false));

    }


    @Override
    public void onBindViewHolder(@NonNull VocabularyAdapter.ViewHolder holder, int position) {
        Vocabulary vocab = arrayList.get(position);

        holder.tvWord.setText(vocab.getWord());
        holder.tvMeaning.setText(vocab.getMeaning());
        holder.tvCategory.setText(vocab.getCategoryName() != null ? vocab.getCategoryName() : "No Category");

        // Sự kiện click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VocabDetailActivity.class);
            intent.putExtra("wordId", vocab.getId());
            ((Activity) context).startActivityForResult(intent, 1);
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView tvWord,tvCategory,tvMeaning;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvWord = itemView.findViewById(R.id.tvWord);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvMeaning = itemView.findViewById(R.id.Meaning);

        }
    }
}
