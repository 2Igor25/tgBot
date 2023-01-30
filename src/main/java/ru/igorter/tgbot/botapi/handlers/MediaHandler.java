package ru.igorter.tgbot.botapi.handlers;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.BaseRequest;
import org.springframework.stereotype.Component;
import ru.igorter.tgbot.botapi.BotState;
import ru.igorter.tgbot.botapi.InputMessageHandler;

@Component
public class MediaHandler implements InputMessageHandler {
    @Override
    public BaseRequest handle(Message message) {
        return null;
    }

    @Override
    public BaseRequest handle(CallbackQuery callbackQuery) {
        return null;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.MEDIA;
    }
}
