package com.example.bitlinker.translator.ui.view;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    public void updateList(List<TranslatedText> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    public void removeItem(TranslatedText item) {
        // TODO: to presenter...
        int index = mItems.indexOf(item);
        if (index != -1) {
            mItems.remove(index);
            notifyItemRemoved(index);
        }
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

            // TODO: external listener
            mCardView.setOnLongClickListener(mLongClickListener);
            final SwipeDismissBehavior<CardView> swipeDismissBehavior = new SwipeDismissBehavior();
            swipeDismissBehavior.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_ANY);
            swipeDismissBehavior.setListener(
                    new SwipeDismissBehavior.OnDismissListener() {
                        @Override public void onDismiss(View view) {
                            if (mLongClickListener != null) {
                                mLongClickListener.onLongClick(mCardView);

                                // TODO: remove me...
                                int pos = getAdapterPosition();
                            }
                        }

                        @Override
                        public void onDragStateChanged(int state) {}
                    });

            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mCardView.getLayoutParams();
            params.setBehavior(swipeDismissBehavior);
        }

        public void applyData(TranslatedText text) {
            mOriginalText.setText(text.getOriginalText());
            mTranslatedText.setText(text.getTranslatedText());
            mLanguage.setText(text.getLanguage());
            mCardView.setTag(text);

            // TODO: Here is the layout data reset
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mCardView.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            mCardView.setLayoutParams(params);
        }
    }
}
