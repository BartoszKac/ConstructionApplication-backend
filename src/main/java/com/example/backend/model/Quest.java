package com.example.backend.model;

public class Quest {

    private String text;

    public Quest() {
    }

    public Quest(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
