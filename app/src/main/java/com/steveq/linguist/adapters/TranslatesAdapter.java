package com.steveq.linguist.adapters;

import android.animation.ObjectAnimator;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Spinner;
import android.widget.TextView;

import com.steveq.linguist.R;
import com.steveq.linguist.model.response.Phrase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TranslatesAdapter extends RecyclerView.Adapter<TranslatesAdapter.ViewHolder> {

    private ArrayList<Phrase> mOutputs;

    public TranslatesAdapter() {
        mOutputs = new ArrayList<>();
        mOutputs.add(new Phrase());
    }

    public ArrayList<Phrase> getOutputs() {
        return mOutputs;
    }

    public void setOutputs(ArrayList<Phrase> outputs) {
        mOutputs = outputs;
    }
    public void addOutputOption(Phrase option){
        mOutputs.add(option);
    }

    @Override
    public TranslatesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.translate_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TranslatesAdapter.ViewHolder holder, int position) {

        Phrase phrase = mOutputs.get(position);
        holder.outputWord.setText(phrase.getText());
        if(holder != null){
            if(position % 4 < 4){
                holder.outputLanguage.setSelection(position % 4);
            }
        }

        if(position == mOutputs.size()-1) {
            holder.cardView.setVisibility(View.INVISIBLE);
            swipeAnimation(holder.cardView, 1000, -1).setDuration(1000).start();
        }
    }

    @Override
    public int getItemCount() {
        return mOutputs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView outputWord;
        Spinner outputLanguage;

        public ViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.translateCardView);
            outputWord = (TextView) itemView.findViewById(R.id.outputWordTextView);
            outputLanguage = (Spinner) itemView.findViewById(R.id.outputLanguageSpinner);
        }
    }

    private ObjectAnimator swipeAnimation(View v, int offset, int direction){
        int off;

        if(direction == -1){
            off = -offset;
        } else {
            off = offset;
        }

        float startValue = v.getX();

        v.setVisibility(View.VISIBLE);

        ObjectAnimator anim = ObjectAnimator.ofFloat(v, "x", startValue + off, startValue);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(300);
        return anim;
    }
}
