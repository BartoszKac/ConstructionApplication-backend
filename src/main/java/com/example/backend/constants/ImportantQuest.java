package com.example.backend.constants;


public enum ImportantQuest {
    INTERIOR_WALL_PAINT("interior+wall+paint"),
    EXTERIOR_PAINT("exterior+paint"),
    WALL_PRIMER("wall+primer");

    private final String quest;

    ImportantQuest(String quest) {
        this.quest = quest;
    }

    public String getQuest() {
        return quest;
    }
}
