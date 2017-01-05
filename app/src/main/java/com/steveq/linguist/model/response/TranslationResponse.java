package com.steveq.linguist.model.response;

import java.util.List;

public class TranslationResponse {
    private String result;
    private List<Translation> tuc;
    String phrase;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<Translation> getTuc() {
        return tuc;
    }

    public void setTuc(List<Translation> tuc) {
        this.tuc = tuc;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

}
