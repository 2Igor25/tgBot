package ru.igorter.tgbot.cache;

import ru.igorter.tgbot.botapi.BotState;
import ru.igorter.tgbot.botapi.handlers.profile.MachineStateProfile;
import ru.igorter.tgbot.botapi.handlers.profile.UserProfileData;

public interface DataCache {
    void setUsersCurrentBotState(long userId, BotState botState);

    BotState getUsersCurrentBotState(long userId);

    MachineStateProfile getMachineStateProfile(long userId);

    public void saveUserProfileData(long userId, MachineStateProfile machineStateProfile);

}
