package ru.igorter.tgbot.botapi;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.AnswerPreCheckoutQuery;
import com.pengrad.telegrambot.request.BaseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.igorter.tgbot.cache.MachineStateDataCache;

import java.util.Optional;

@Component
public class TelegramFacade {

    private BotStateContext botStateContext;
    private MachineStateDataCache machineStateDataCache;

    @Autowired
    public TelegramFacade(BotStateContext botStateContext, MachineStateDataCache userDataCache) {
        this.botStateContext = botStateContext;
        this.machineStateDataCache = userDataCache;
    }


    public BaseRequest handleUpdate(Update update) {
        BaseRequest replyMessage = null;
        Message message = update.message();
        CallbackQuery callbackQuery = update.callbackQuery();

        if (update.preCheckoutQuery() != null) {
            PreCheckoutQuery preCheckoutQuery = update.preCheckoutQuery();
            return new AnswerPreCheckoutQuery(preCheckoutQuery.id());
        }

        if (message != null && message.text() != null) {
            replyMessage = handleInputMessage(message);
        } else if (message != null && message.photo() != null) {
            replyMessage = handleInputMessage(message);
        } else if (callbackQuery != null && callbackQuery.data() != null) {
            replyMessage = handleInputMessage(callbackQuery);
        } else if (message != null && message.successfulPayment() != null) {
            replyMessage = botStateContext.processInputMessage(BotState.PAY_SYSTEM, message);
        }


        return replyMessage;
    }

    private BaseRequest handleInputMessage(Message message) {
        String inputMsg = message.text();
        long userId = message.chat().id();
        BotState botState;

        if(inputMsg == null) {
            botState = machineStateDataCache.getUsersCurrentBotState(userId);
        } else {
            botState = switchBotState(inputMsg, userId);
        }

        BaseRequest replyMessage;

        machineStateDataCache.setUsersCurrentBotState(userId, botState);

        replyMessage = botStateContext.processInputMessage(botState, message);

        return replyMessage;
    }

    private BaseRequest handleInputMessage(CallbackQuery callbackQuery) {
        String inputMsg = callbackQuery.data();
        long userId = callbackQuery.message().chat().id();

        BotState botState = switchBotState(inputMsg, userId);
        BaseRequest replyMessage;

        machineStateDataCache.setUsersCurrentBotState(userId, botState);

        replyMessage = botStateContext.processInputMessage(botState, callbackQuery);

        return replyMessage;
    }



    private BotState switchBotState(String inputMsg, long userId) {
        BotState botState;

        switch (inputMsg) {
            case "/start":
                botState = BotState.START_MENU;
                break;
            case "Клавиатуры":
                botState = BotState.KEYBOARDS;
                break;
            case "Машинное состояние":
                botState = BotState.MACHINE_STATE;
                break;
            case "Платежная система":
                botState = BotState.PAY_SYSTEM;
                break;
            case "API":
                botState = BotState.API;
                break;
            case "СУБД":
                botState = BotState.BD;
                break;
            case "ADD_USER":
                botState = BotState.ADD_USER;
                break;
            case "CHECK_USER":
                botState = BotState.CHECK_USER;
                break;
            case "UPDATE_USER":
                botState = BotState.UPDATE_USER;
                break;
            case "DELETE_USER":
                botState = BotState.DELETE_USER;
                break;
            case "GET_RND_NUMBER":
                botState = BotState.GET_RND_NUMBER;
                break;
            case "Медиа группы":
                botState = BotState.MEDIA;
                break;
            default:
                botState = machineStateDataCache.getUsersCurrentBotState(userId);
                break;
        }

        return botState;
    }
}
