package com.example.prm392dictionaryapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392dictionaryapp.R;
import com.example.prm392dictionaryapp.entities.FlashcardSet;

import java.util.List;

public class FlashCardSetAdapter extends RecyclerView.Adapter<FlashCardSetAdapter.SetViewHolder> {

    private Context context;
    private List<FlashcardSet> setList;
    private OnItemClickListener listener;

    private OnEditClickListener editClickListener;
    public interface OnEditClickListener {
        void onEditClick(FlashcardSet set);
    }
    public interface OnItemClickListener {
        void onItemClick(FlashcardSet flashcardSet);
    }


    public FlashCardSetAdapter(Context context, List<FlashcardSet> setList, OnItemClickListener listener, OnEditClickListener editClickListener) {
        this.context = context;
        this.setList = setList;
        this.listener = listener;
        this.editClickListener = editClickListener;
    }

    @NonNull
    @Override
    public SetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_set, parent, false);
        return new SetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SetViewHolder holder, int position) {
        FlashcardSet s = setList.get(position);
        holder.textTitle.setText(s.getTitle());
        holder.textCount.setText("Number of card: " + s.getFlashcardCount());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(s);
            }
        });
        // Khi click vào icon edit → chỉnh sửa flashcard set
        holder.iconMenu.setOnClickListener(v -> {
            if (editClickListener != null) editClickListener.onEditClick(s);
        });
    }

    @Override
    public int getItemCount() {
        return setList.size();
    }

    public static class SetViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textCount;
        ImageView iconMenu;
        public SetViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textSetTitle);
            textCount = itemView.findViewById(R.id.textFlashcardCount);
            iconMenu = itemView.findViewById(R.id.iconMenu);
        }
    }
}
