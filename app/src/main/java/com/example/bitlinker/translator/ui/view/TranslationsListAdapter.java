package com.example.bitlinker.translator.ui.view;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bitlinker.translator.R;
import com.example.bitlinker.translator.model.TranslatedText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bitlinker on 29.08.2016.
 */
public class TranslationsListAdapter extends RecyclerView.Adapter<TranslationsListAdapter.TranslationViewHolder> {
    private List<TranslatedText> mItems = new ArrayList<>();
    private View.OnLongClickListener mLongClickListener;

    public TranslationsListAdapter(View.OnLongClickListener longClickListener) {
        mLongClickListener = longClickListener;
    }

    void updateList(List<TranslatedText> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public TranslationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.translation_item, parent, false);
        TranslationViewHolder holder = new TranslationViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(TranslationViewHolder holder, int position) {
        TranslatedText text = mItems.get(position);
        holder.applyData(text);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class TranslationViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cardView)
        CardView mCardView;
        @BindView(R.id.txtOriginalText)
        TextView mOriginalText;
        @BindView(R.id.txtTranslatedText)
        TextView mTranslatedText;
        @BindView(R.id.txtLanguage)
        TextView mLanguage;

        public TranslationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void applyData(TranslatedText text) {
            mOriginalText.setText(text.getOriginalText());
            mTranslatedText.setText(text.getTranslatedText());
            mLanguage.setText(text.getLanguage());
            mCardView.setOnLongClickListener(mLongClickListener);
            mCardView.setTag(text);
        }
    }
}
