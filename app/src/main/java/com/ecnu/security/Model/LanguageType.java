package com.ecnu.security.Model;

/**
 * Created by Phuylai on 2017/4/25.
 */

public enum LanguageType {
    CHINESE("chinese"), ENGLISH("english");
    private final String text;
    LanguageType(String text){
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
    public boolean equals(String value){
        if(text == null){
            return false;
        }
        return  text.equals(value);
    }
}
