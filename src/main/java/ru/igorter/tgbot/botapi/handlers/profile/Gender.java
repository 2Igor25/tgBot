package ru.igorter.tgbot.botapi.handlers.profile;

public enum Gender {
    MALE("мужской"),
    FEMALE("женский");

    private final String name;

    Gender(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
