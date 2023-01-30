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

@Component
public class KeyboardHandler implements InputMessageHandler {
    private MachineStateDataCache machineStateDataCache;

    public KeyboardHandler(MachineStateDataCache machineStateDataCache) {
        this.machineStateDataCache = machineStateDataCache;
    }

    @Override
    public  BaseRequest handle(Message message) {
        long chatId = message.chat().id();
        int messageId = message.messageId();
        String username = message.chat().username();

        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup()
                .addRow(new InlineKeyboardButton("Меню").callbackData("Меню"));

        EditMessageText editMessageText = new EditMessageText(chatId, messageId, username)
                .replyMarkup(inlineKeyboard);

        return editMessageText;
    }

    @Override
    public BaseRequest handle(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.message().chat().id();
        int messageId = callbackQuery.message().messageId();
        String username = callbackQuery.message().chat().username();

        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup()
                .addRow(new InlineKeyboardButton("Меню").callbackData("Меню"));

        EditMessageText editMessageText = new EditMessageText(chatId, messageId, username)
                .replyMarkup(inlineKeyboard);

        machineStateDataCache.setUsersCurrentBotState(chatId, BotState.START_MENU);

        return editMessageText;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.KEYBOARDS;
    }
}
