package com.example.prm392dictionaryapp.entities;

public class CategoryHeader extends ListItem {
        private String categoryName;

        public CategoryHeader(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getCategoryName() {
            return categoryName;
        }

        @Override
        public int getType() {
            return TYPE_HEADER;
        }
    }
