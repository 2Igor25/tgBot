package ru.igorter.tgbot.botapi.handlers.profile;

import com.pengrad.telegrambot.model.PhotoSize;
import lombok.Data;

import java.io.File;
import java.util.Arrays;

@Data
public class MachineStateProfile {
    String name;
    Gender gender;
    PhotoSize[] photo;

    public void setGenderByText(String text) {
        if(text.equals("женский")) {
            setGender(Gender.FEMALE);
        } else {
            setGender(Gender.MALE);
        }
    }


    @Override
    public String toString() {
        return name + " " + gender.getName();
    }
}
