package ru.igorter.tgbot.module;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "user_profile")
public class UserProfile {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    String name;

    @Column(name = "username")
    String username;

    @Column(name = "rand_num ")
    Integer rndNumber;
}
