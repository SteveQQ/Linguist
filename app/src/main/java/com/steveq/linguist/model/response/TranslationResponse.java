package com.steveq.linguist.model.response;

import java.util.List;

public class TranslationResponse {
    private String result;
    private List<Translation> tuc;
    private String phrase;
    private String from;
    private String dest;

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

    public String getFrom() {
        if(this.from.equals("pl")){
            return "pol";
        } else if(from.equals("de")){
            return "deu";
        } else if(from.equals("fr")){
            return "fra";
        } else if(from.equals("en")){
            return "eng";
        }
        return null;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDest() {
        if(dest.equals("pl")){
            return "pol";
        } else if(dest.equals("de")){
            return "deu";
        } else if(from.equals("fr")){
            return "fra";
        } else if(from.equals("en")){
            return "eng";
        }
        return null;
    }

    public void setDest(String dest) {
    }
}
