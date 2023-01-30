package ru.igorter.tgbot.botapi.handlers;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import org.springframework.stereotype.Component;
import ru.igorter.tgbot.botapi.BotState;
import ru.igorter.tgbot.botapi.InputMessageHandler;
import ru.igorter.tgbot.botapi.handlers.profile.MachineStateProfile;
import ru.igorter.tgbot.cache.MachineStateDataCache;

import java.lang.reflect.Array;
import java.util.Arrays;

@Component
public class MachineStateHandler implements InputMessageHandler {
    private MachineStateDataCache machineStateDataCache;

    public MachineStateHandler(MachineStateDataCache userDataCache) {
        this.machineStateDataCache = userDataCache;
    }

    @Override
    public BaseRequest handle(Message message) {

        String usersAnswer = message.text();
        long chatId = message.chat().id();
        int messageId = message.messageId();

        if (machineStateDataCache.getUsersCurrentBotState(chatId).equals(BotState.MACHINE_STATE)) {
            machineStateDataCache.setUsersCurrentBotState(chatId, BotState.ASK_NAME);
        }

        if (machineStateDataCache.getUsersCurrentBotState(chatId).equals(BotState.MACHINE_STATE_PROFILE_FILLED)) {
            return processUsersInput(chatId, messageId, message.photo());
        }
        return processUsersInput(chatId, messageId, usersAnswer);
    }

    private BaseRequest processUsersInput(long userId, int messageId, PhotoSize[] photo) {
        MachineStateProfile profileData = machineStateDataCache.getMachineStateProfile(userId);
        profileData.setPhoto(photo);
        machineStateDataCache.setUsersCurrentBotState(userId, BotState.START_MENU_REPLAY);
        machineStateDataCache.saveUserProfileData(userId, profileData);

        return new SendPhoto(userId, Arrays.asList(profileData.getPhoto()).get(0).fileId())
                .caption(profileData.toString())
                .replyMarkup(new ReplyKeyboardMarkup("Меню")
                        .resizeKeyboard(true)
                        .oneTimeKeyboard(true));
    }

    @Override
    public BaseRequest handle(CallbackQuery callbackQuery) {
        String usersAnswer = callbackQuery.data();
        long chatId = callbackQuery.message().chat().id();
        int messageId = callbackQuery.message().messageId();

        if (machineStateDataCache.getUsersCurrentBotState(chatId).equals(BotState.MACHINE_STATE)) {
            machineStateDataCache.setUsersCurrentBotState(chatId, BotState.ASK_NAME);
        }

        return processUsersInput(chatId, messageId, usersAnswer);

    }

    private BaseRequest processUsersInput(long userId, int messageId, String usersAnswer) {

        MachineStateProfile profileData = machineStateDataCache.getMachineStateProfile(userId);
        BotState botState = machineStateDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.ASK_NAME)) {
            EditMessageText editMessageText = new EditMessageText(userId, messageId, "Введите имя");
            machineStateDataCache.setUsersCurrentBotState(userId, BotState.ASK_GENDER);
            return editMessageText;
        } else if (botState.equals(BotState.ASK_GENDER)) {
            profileData.setName(usersAnswer);
            replyToUser = new SendMessage(userId, "Введите пол")
                    .replyMarkup(new ReplyKeyboardMarkup(new String[][]{
                            {"Мужской", "Женский"}
                    })
                            .resizeKeyboard(true)
                            .oneTimeKeyboard(true));
            machineStateDataCache.setUsersCurrentBotState(userId, BotState.ASK_PHOTO);
            machineStateDataCache.saveUserProfileData(userId, profileData);
        } else if (botState.equals(BotState.ASK_PHOTO)) {
            profileData.setGenderByText(usersAnswer);
            replyToUser = new SendMessage(userId, "Отправьте фото");
            machineStateDataCache.setUsersCurrentBotState(userId, BotState.MACHINE_STATE_PROFILE_FILLED);
            machineStateDataCache.saveUserProfileData(userId, profileData);
        }

        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.MACHINE_STATE;
    }
}
