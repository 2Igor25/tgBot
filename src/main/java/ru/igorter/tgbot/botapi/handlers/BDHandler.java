package ru.igorter.tgbot.botapi.handlers;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import org.springframework.stereotype.Component;
import ru.igorter.tgbot.botapi.BotState;
import ru.igorter.tgbot.botapi.InputMessageHandler;
import ru.igorter.tgbot.cache.MachineStateDataCache;
import ru.igorter.tgbot.module.UserProfile;
import ru.igorter.tgbot.repository.UserProfileRepository;

import java.util.Random;

@Component
public class BDHandler implements InputMessageHandler {
    private MachineStateDataCache machineStateDataCache;
    private UserProfileRepository userRep;

    public BDHandler(MachineStateDataCache machineStateDataCache, UserProfileRepository userRep) {
        this.machineStateDataCache = machineStateDataCache;
        this.userRep = userRep;
    }

    @Override
    public BaseRequest handle(Message message) {
        BotState botState = machineStateDataCache.getUsersCurrentBotState(message.chat().id());

        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup()
                .addRow(new InlineKeyboardButton("Добавить пользователя в БД").callbackData("ADD_USER"))
                .addRow(new InlineKeyboardButton("Проверить на наличие пользователя").callbackData("CHECK_USER"))
                .addRow(new InlineKeyboardButton("Изменить имя пользователя").callbackData("UPDATE_USER"))
                .addRow(new InlineKeyboardButton("Удалить пользователя").callbackData("DELETE_USER"),
                        new InlineKeyboardButton("Получить число").callbackData("GET_RND_NUMBER"))
                .addRow(new InlineKeyboardButton("Меню").callbackData("/start"));


        if(botState.equals(BotState.BD)) {
            long chatId = message.chat().id();
            int messageId = message.messageId();

            EditMessageText editMessageText = new EditMessageText(chatId, messageId, "Список заданий")
                    .replyMarkup(inlineKeyboard);

            return editMessageText;
        } else if (botState.equals(BotState.ADD_USER)) {
            UserProfile userProfile = new UserProfile();
            userProfile.setUsername(message.chat().username());
            userProfile.setRndNumber(getRandomNum());
            userRep.save(userProfile);

            long chatId = message.chat().id();
            int messageId = message.messageId();

            EditMessageText editMessageText = new EditMessageText(chatId, messageId, "Пользователь добавлен")
                    .replyMarkup(inlineKeyboard);

            return editMessageText;
        }

        return null;
    }

    @Override
    public BaseRequest handle(CallbackQuery callbackQuery) {
        BotState botState = machineStateDataCache.getUsersCurrentBotState(callbackQuery.message().chat().id());
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup()
                .addRow(new InlineKeyboardButton("Добавить пользователя в БД").callbackData("ADD_USER"))
                .addRow(new InlineKeyboardButton("Проверить на наличие пользователя").callbackData("CHECK_USER"))
                .addRow(new InlineKeyboardButton("Изменить имя пользователя").callbackData("UPDATE_USER"))
                .addRow(new InlineKeyboardButton("Удалить пользователя").callbackData("DELETE_USER"),
                        new InlineKeyboardButton("Получить число").callbackData("GET_RND_NUMBER"))
                .addRow(new InlineKeyboardButton("Меню").callbackData("/start"));

        if(botState.equals(BotState.BD)) {

            long chatId = callbackQuery.message().chat().id();
            int messageId = callbackQuery.message().messageId();

            EditMessageText editMessageText = new EditMessageText(chatId, messageId, "Список заданий")
                    .replyMarkup(inlineKeyboard);

            return editMessageText;
        } else if (botState.equals(BotState.ADD_USER)) {
            long chatId = callbackQuery.message().chat().id();
            UserProfile userProfile = new UserProfile();
            userProfile.setId(chatId);
            userProfile.setUsername(callbackQuery.message().chat().username());
            userProfile.setRndNumber(getRandomNum());
            userRep.save(userProfile);

            int messageId = callbackQuery.message().messageId();

            EditMessageText editMessageText = new EditMessageText(chatId, messageId, "Пользователь добавлен")
                    .replyMarkup(inlineKeyboard);

            return editMessageText;
        } else if (botState.equals(BotState.CHECK_USER)) {
            int messageId = callbackQuery.message().messageId();
            long chatId = callbackQuery.message().chat().id();
            String mess;
            UserProfile userProfile = userRep.findById(chatId).orElse(null);
            if (userProfile != null) {
                mess = "Пользователь есть в базе";
            } else {
                mess = "Пользователь отсутствует";
            }

            EditMessageText editMessageText = new EditMessageText(chatId, messageId, mess)
                    .replyMarkup(inlineKeyboard);

            return editMessageText;
        } else if (botState.equals(BotState.DELETE_USER)) {
            int messageId = callbackQuery.message().messageId();
            long chatId = callbackQuery.message().chat().id();
            userRep.deleteById(chatId);

            EditMessageText editMessageText = new EditMessageText(chatId, messageId, "Пользователь удален")
                    .replyMarkup(inlineKeyboard);

            return editMessageText;
        } else if (botState.equals(BotState.UPDATE_USER)) {
            int messageId = callbackQuery.message().messageId();
            long chatId = callbackQuery.message().chat().id();
            String mess;
            UserProfile userProfile = userRep.findById(chatId).orElse(null);

            if (userProfile != null) {
                userProfile.setName("Steve");
                mess = "Имя изменено";
                userRep.save(userProfile);
            } else {
                mess = "Пользователь отсутствует";
            }

            EditMessageText editMessageText = new EditMessageText(chatId, messageId, mess)
                    .replyMarkup(inlineKeyboard);

            return editMessageText;
        } else if (botState.equals(BotState.GET_RND_NUMBER)) {
            int messageId = callbackQuery.message().messageId();
            long chatId = callbackQuery.message().chat().id();
            String mess;
            UserProfile userProfile = userRep.findById(chatId).orElse(null);

            if (userProfile != null) {
                mess = userProfile.getRndNumber().toString();
                userRep.save(userProfile);
            } else {
                mess = "Пользователь отсутствует";
            }

            EditMessageText editMessageText = new EditMessageText(chatId, messageId, mess)
                    .replyMarkup(inlineKeyboard);

            return editMessageText;
        }

        return null;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.BD;
    }

    private int getRandomNum() {
        Random random = new Random();
        return random.nextInt(100);
    }
}
