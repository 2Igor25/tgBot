package ru.igorter.tgbot.botapi;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;

public interface InputMessageHandler {
    BaseRequest handle(Message message);

    BaseRequest handle(CallbackQuery callbackQuery);

    BotState getHandlerName();
}
