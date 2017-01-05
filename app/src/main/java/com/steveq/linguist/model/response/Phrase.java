package com.steveq.linguist.model.response;

public class Phrase {
    String text;
    String language;

    public Phrase(){
        new Phrase("eng");
    }
    public Phrase(String lan){
        setLanguage(lan);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLanguage() {
        return language;
    }

    public String getLanguageCropped(){
        if(!this.language.equals("pol")){
            return this.language.substring(0, 2);
        } else {
            return this.language = "pl";
        }
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
