package com.example.prm392dictionaryapp.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392dictionaryapp.R;
import com.example.prm392dictionaryapp.activities.VocabDetailActivity;
import com.example.prm392dictionaryapp.entities.CategoryHeader;
import com.example.prm392dictionaryapp.entities.ListItem;
import com.example.prm392dictionaryapp.entities.Vocabulary;
import com.example.prm392dictionaryapp.entities.VocabularyItem;
import com.example.prm392dictionaryapp.utils.VocabularyDAO;

import java.util.List;

public class VocabularyCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<ListItem> items;
    private Context context;

    public VocabularyCategoryAdapter(List<ListItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == ListItem.TYPE_HEADER) {
            View view = inflater.inflate(R.layout.vocab_category_item, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.vocab_list_card, parent, false);
            return new VocabViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListItem item = items.get(position);

        if (item instanceof CategoryHeader) {
            CategoryHeader header = (CategoryHeader) item;
            ((HeaderViewHolder) holder).bind(header, categoryName -> {
                deleteCategory(categoryName);
            });
        } else if (item instanceof VocabularyItem) {
            VocabularyItem vocabItem = (VocabularyItem) item;
            Vocabulary vocab = vocabItem.getVocab();

            ((VocabViewHolder) holder).bind(vocabItem);

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, VocabDetailActivity.class);
                intent.putExtra("wordId", vocab.getId());
                ((Activity) context).startActivityForResult(intent, 1);
            });
        }

    }

    private void deleteCategory(String categoryName) {
        VocabularyDAO dao = new VocabularyDAO(context);
        boolean hasWords = dao.hasWordsInCategory(categoryName);

        if (hasWords) {
            Toast.makeText(context, "Không thể xóa danh mục vì vẫn còn từ vựng!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Nếu không có từ thì xóa category
        boolean success = dao.deleteCategory(categoryName);

        if (success) {
            items.removeIf(item -> item instanceof CategoryHeader &&
                    ((CategoryHeader) item).getCategoryName().equals(categoryName));

            notifyDataSetChanged();
            Toast.makeText(context, "Đã xóa danh mục: " + categoryName, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Lỗi khi xóa danh mục!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTitle;
        ImageView btnDelete;

        HeaderViewHolder(View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.tvCategory);
            btnDelete = itemView.findViewById(R.id.btndelete);
        }

        void bind(CategoryHeader categoryHeader, OnCategoryDeleteListener deleteListener) {
            categoryTitle.setText(categoryHeader.getCategoryName());
            btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(itemView.getContext())
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa danh mục \"" + categoryHeader.getCategoryName() + "\" không?")
                        .setPositiveButton("OK", (dialog, which) -> {
                            deleteListener.onDeleteCategory(categoryHeader.getCategoryName());
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            });
        }
    }
    public interface OnCategoryDeleteListener {
        void onDeleteCategory(String categoryName);
    }

    static class VocabViewHolder extends RecyclerView.ViewHolder {
        TextView tvWord,tvCategory,tvMeaning;

        VocabViewHolder(View itemView) {
            super(itemView);
            tvWord = itemView.findViewById(R.id.tvWord);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvMeaning = itemView.findViewById(R.id.Meaning);
        }

        void bind(VocabularyItem vocabItem) {
            Vocabulary vocab = vocabItem.getVocab();
            tvWord.setText(vocab.getWord());
            tvCategory.setText(vocab.getCategoryName());
            tvMeaning.setText(vocab.getMeaning());
        }
    }

    public void updateData(List<ListItem> newItems) {
        this.items.clear();
        this.items.addAll(newItems);
        notifyDataSetChanged();
    }
}
