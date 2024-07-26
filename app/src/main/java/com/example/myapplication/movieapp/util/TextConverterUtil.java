package com.example.myapplication.movieapp.util;

public class TextConverterUtil {
    private String text;

    public String getText(String oldText) {
        if(oldText.length() >= 17){
            text = oldText.substring(0, 17) + "...";
        }else{
            text = oldText;
        }
        return text;
    }
}
