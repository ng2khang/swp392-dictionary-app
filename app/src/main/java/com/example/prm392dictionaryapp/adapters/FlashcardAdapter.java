package com.example.prm392dictionaryapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392dictionaryapp.R;
import com.example.prm392dictionaryapp.entities.Flashcard;

import java.util.List;

public class FlashcardAdapter extends RecyclerView.Adapter<FlashcardAdapter.FlashcardViewHolder> {
    private Context context;
    private List<Flashcard> flashcards;

    public FlashcardAdapter(Context context, List<Flashcard> flashcards) {
        this.context = context;
        this.flashcards = flashcards;
    }
    @NonNull
    @Override
    public FlashcardAdapter.FlashcardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.flashcard, parent, false);
        return new FlashcardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardAdapter.FlashcardViewHolder holder, int position) {
        Flashcard flashcard = flashcards.get(position);
        holder.textFlashcard.setText(flashcard.getTerm());

        holder.cardContainer.setOnClickListener(v -> {
            if (holder.isFlipped) {
                holder.textFlashcard.setText(flashcard.getTerm());
            } else {
                holder.textFlashcard.setText(flashcard.getDefinition());
            }
            holder.isFlipped = !holder.isFlipped;
        });
    }

    @Override
    public int getItemCount() {
        return flashcards.size();
    }

    public static class FlashcardViewHolder extends RecyclerView.ViewHolder {
        TextView textFlashcard;
        LinearLayout cardContainer;
        boolean isFlipped = false;

        public FlashcardViewHolder(@NonNull View itemView) {
            super(itemView);
            textFlashcard = itemView.findViewById(R.id.textFlashcard);
            cardContainer = itemView.findViewById(R.id.cardContainer);
        }
    }
}
