package com.example.prm392dictionaryapp.entities;

public class VocabularyItem extends ListItem {
        private Vocabulary vocab;

        public VocabularyItem(Vocabulary vocab) {
            this.vocab = vocab;
        }

        public Vocabulary getVocab() {
            return vocab;
        }

        @Override
        public int getType() {
            return TYPE_VOCAB;
        }
    }
