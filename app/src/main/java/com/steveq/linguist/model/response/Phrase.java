package com.steveq.linguist.model.response;

import android.os.Parcel;
import android.os.Parcelable;

public class Phrase implements Parcelable{
    String text;
    String language;

    public Phrase(){
        new Phrase("eng");
    }
    public Phrase(String lan){
        setLanguage(lan);
    }

    private Phrase(Parcel in){
        text = in.readString();
        language = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(language);
    }

    public static final Parcelable.Creator<Phrase> CREATOR = new Parcelable.Creator<Phrase>(){
        @Override
        public Phrase createFromParcel(Parcel source) {
            return new Phrase(source);
        }

        @Override
        public Phrase[] newArray(int size) {
            return new Phrase[size];
        }
    };
}
