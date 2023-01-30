package ru.igorter.tgbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.igorter.tgbot.module.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}
