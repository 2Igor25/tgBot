package ru.igorter.tgbot.module;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class USD {
    String ID;
    String NumCode;
    String CharCode;
    String Nominal;
    String Name;
    String Value;
    String Previous;
}
