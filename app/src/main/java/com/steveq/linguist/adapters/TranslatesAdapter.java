package com.steveq.linguist.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.steveq.linguist.R;
import com.steveq.linguist.model.TranslateOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TranslatesAdapter extends RecyclerView.Adapter<TranslatesAdapter.ViewHolder> {

    private ArrayList<TranslateOutput> mOutputOptions;
    private List<String> languageOptions;

    public TranslatesAdapter() {
        mOutputOptions = new ArrayList<>();
        mOutputOptions.add(new TranslateOutput());
    }

    public ArrayList<TranslateOutput> getOutputOptions() {
        return mOutputOptions;
    }

    public void setOutputOptions(ArrayList<TranslateOutput> outputOptions) {
        mOutputOptions = outputOptions;
    }
    public void addOutputOption(TranslateOutput option){
        mOutputOptions.add(option);
    }

    @Override
    public TranslatesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.translate_card, parent, false);
        languageOptions = Arrays.asList(parent.getContext().getResources().getStringArray(R.array.langs));

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TranslatesAdapter.ViewHolder holder, int position) {

        TranslateOutput translateOutput = mOutputOptions.get(position);
        holder.outputWord.setText(translateOutput.getOutput());
        holder.outputLanguage.setSelection(getLanguageIndex(translateOutput.getOutputLanguage()));


    }

    private int getLanguageIndex(String outputLanguage) {
        int index = -1;

        for(String lan : languageOptions){
            if(lan.equals(outputLanguage)){
                return index;
            }
        }

        return index;
    }

    @Override
    public int getItemCount() {
        return mOutputOptions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView outputWord;
        Spinner outputLanguage;

        public ViewHolder(View itemView) {
            super(itemView);

            outputWord = (TextView) itemView.findViewById(R.id.outputWordTextView);
            outputLanguage = (Spinner) itemView.findViewById(R.id.outputLanguageSpinner);
        }
    }
}
