package ru.igorter.tgbot.botapi;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BotStateContext {

    private Map<BotState, InputMessageHandler> messageHandlers = new HashMap<>();

    public BotStateContext(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
    }

    public BaseRequest processInputMessage(BotState currentState, Message message) {
        InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
        return currentMessageHandler.handle(message);
    }

    public BaseRequest processInputMessage(BotState currentState, CallbackQuery callbackQuery) {
        InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
        return currentMessageHandler.handle(callbackQuery);
    }

    private InputMessageHandler findMessageHandler(BotState currentState) {
        if (isFillingMachineStateProfile(currentState)) {
            return messageHandlers.get(BotState.MACHINE_STATE);
        } else if(isBDState(currentState)) {
            return messageHandlers.get(BotState.BD);
        }

        return messageHandlers.get(currentState);
    }

    private boolean isBDState(BotState currentState) {
        switch (currentState) {
            case ADD_USER:
            case UPDATE_USER:
            case CHECK_USER:
            case DELETE_USER:
            case GET_RND_NUMBER:
            case BD:
                return true;
            default:
                return false;
        }
    }

    private boolean isFillingMachineStateProfile(BotState currentState) {
        switch (currentState) {
            case ASK_NAME:
            case ASK_GENDER:
            case ASK_PHOTO:
            case MACHINE_STATE:
            case MACHINE_STATE_PROFILE_FILLED:
                return true;
            default:
                return false;
        }
    }
}
