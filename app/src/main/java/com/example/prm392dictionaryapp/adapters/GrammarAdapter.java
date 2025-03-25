package com.example.prm392dictionaryapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392dictionaryapp.R;
import com.example.prm392dictionaryapp.activities.EditGrammarActivity;
import com.example.prm392dictionaryapp.utils.GrammarDatabaseManager;

public class GrammarAdapter extends RecyclerView.Adapter<GrammarAdapter.GrammarViewHolder> {

    private Context context;
    private Cursor cursor;
    private GrammarDatabaseManager dbManager;

    public GrammarAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
        this.dbManager = new GrammarDatabaseManager(context);
    }

    @Override
    public GrammarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_grammar, parent, false);
        return new GrammarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GrammarViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            final int id = cursor.getInt(cursor.getColumnIndex("id"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            String example = cursor.getString(cursor.getColumnIndex("example"));

            holder.titleTextView.setText(title);
            holder.descriptionTextView.setText(description);
            holder.exampleTextView.setText(example);

            // Sự kiện nút Edit
            holder.editButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, EditGrammarActivity.class);
                intent.putExtra("id", id); // Chuyển id của ngữ pháp cần chỉnh sửa
                context.startActivity(intent); // Mở màn hình EditGrammarActivity
            });

            // Sự kiện nút Delete
            holder.deleteButton.setOnClickListener(v -> {
                // Hiển thị hộp thoại xác nhận trước khi xóa
                new android.app.AlertDialog.Builder(context)
                        .setMessage("Bạn có chắc chắn muốn xóa ngữ pháp này?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            dbManager.deleteGrammar(id); // Xóa ngữ pháp
                            Toast.makeText(context, "Ngữ pháp đã được xóa", Toast.LENGTH_SHORT).show();
                            Cursor newCursor = dbManager.getAllGrammar(); // Cập nhật lại danh sách
                            changeCursor(newCursor);
                        })
                        .setNegativeButton("No", null) // Hủy bỏ
                        .show();
            });
        }
    }



    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public void changeCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        notifyDataSetChanged();
    }

    private void showDeleteConfirmationDialog(int id) {
        // Show confirmation dialog
        new android.app.AlertDialog.Builder(context)
                .setMessage("Are you sure you want to delete this grammar?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    dbManager.deleteGrammar(id);
                    Toast.makeText(context, "Grammar deleted successfully", Toast.LENGTH_SHORT).show();
                    Cursor newCursor = dbManager.getAllGrammar();
                    changeCursor(newCursor);
                })
                .setNegativeButton("No", null)
                .show();
    }

    public class GrammarViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView, exampleTextView;
        Button editButton, deleteButton;

        public GrammarViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.text_title);
            descriptionTextView = itemView.findViewById(R.id.text_description);
            exampleTextView = itemView.findViewById(R.id.text_example);
            editButton = itemView.findViewById(R.id.button_edit);
            deleteButton = itemView.findViewById(R.id.button_delete);
        }
    }
}
