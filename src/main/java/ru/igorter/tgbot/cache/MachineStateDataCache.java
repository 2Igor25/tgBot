package ru.igorter.tgbot.cache;

import org.springframework.stereotype.Component;
import ru.igorter.tgbot.botapi.BotState;
import ru.igorter.tgbot.botapi.handlers.profile.MachineStateProfile;
import ru.igorter.tgbot.botapi.handlers.profile.UserProfileData;

import java.util.HashMap;
import java.util.Map;

@Component
public class MachineStateDataCache implements DataCache {
    private Map<Long, BotState> usersBotStates = new HashMap<>();
    private Map<Long, MachineStateProfile> machineStateProfiles = new HashMap<>();

    @Override
    public void setUsersCurrentBotState(long userId, BotState botState) {
        usersBotStates.put(userId, botState);
    }

    @Override
    public BotState getUsersCurrentBotState(long userId) {
        BotState botState = usersBotStates.get(userId);
        if (botState == null) {
            botState = BotState.START_MENU;
        }

        return botState;
    }

    @Override
    public MachineStateProfile getMachineStateProfile (long userId) {
        MachineStateProfile machineStateProfile = machineStateProfiles.get(userId);
        if (machineStateProfile == null) {
            machineStateProfile = new MachineStateProfile();
        }
        return machineStateProfile;
    }

    @Override
    public void saveUserProfileData(long userId, MachineStateProfile machineStateProfile) {
        machineStateProfiles.put(userId, machineStateProfile);
    }
}
