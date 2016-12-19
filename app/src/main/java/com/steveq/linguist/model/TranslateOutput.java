package com.steveq.linguist.model;

public class TranslateOutput {

    private String mOutput;
    private String mOutputLanguage;

    public TranslateOutput(String outputLanguage) {
        mOutputLanguage = outputLanguage;
    }

    public String getOutput() {
        return mOutput;
    }

    public void setOutput(String output) {
        mOutput = output;
    }

    public String getOutputLanguage() {
        return mOutputLanguage;
    }

    public void setOutputLanguage(String outputLanguage) {
        mOutputLanguage = outputLanguage;
    }
}
