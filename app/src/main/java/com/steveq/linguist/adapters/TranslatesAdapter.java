package com.steveq.linguist.adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.steveq.linguist.R;
import com.steveq.linguist.model.response.Phrase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TranslatesAdapter extends RecyclerView.Adapter<TranslatesAdapter.ViewHolder> {

    private ArrayList<Phrase> mOutputs;
    Context context;

    public TranslatesAdapter(Context context) {
        mOutputs = new ArrayList<>();
        mOutputs.add(new Phrase());
        this.context = context;
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
    public void onBindViewHolder(final TranslatesAdapter.ViewHolder holder, int position) {

        final Phrase phrase = mOutputs.get(position);
        holder.outputWord.setText(phrase.getText());
        if(position % 4 < 4 && phrase.getLanguage() == null){
            holder.outputLanguage.setSelection(position % 4);
        } else {
            String[] langs = context.getResources().getStringArray(R.array.langs);
            int i = 0;
            for(String el : langs){
                if(el == phrase.getLanguage()){
                    holder.outputLanguage.setSelection(i);
                }
                i++;
            }

        }

        holder.outputLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                phrase.setLanguage(holder.outputLanguage.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if(phrase.getLanguage() != null) {
                    phrase.setLanguage(holder.outputLanguage.getSelectedItem().toString());
                }
            }
        });

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
