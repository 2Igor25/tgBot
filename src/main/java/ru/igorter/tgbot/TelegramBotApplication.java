package ru.igorter.tgbot;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;
import com.pengrad.telegrambot.response.StringResponse;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.igorter.tgbot.botapi.BotStateContext;
import ru.igorter.tgbot.botapi.TelegramFacade;
import ru.igorter.tgbot.utils.LinkToPayUtil;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class TelegramBotApplication extends TelegramBot {

    private TelegramFacade telegramFacade;

    @lombok.Builder
    public TelegramBotApplication(String botToken, TelegramFacade telegramFacade) {
        super(botToken);
        this.telegramFacade = telegramFacade;
    }


    public void run() {
        this.setUpdatesListener(updates -> {
            updates.forEach(this::process);

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

    }

    private void process(Update update) {
        if (LinkToPayUtil.getLinkToPay() == null) {
            StringResponse response = this.execute(LinkToPayUtil.createInvoiceLink());
            LinkToPayUtil.setLinkToPay(response.result());
        }


        BaseRequest sendMessage = telegramFacade.handleUpdate(update);
        this.execute(sendMessage);
    }

//    private void startMenu() {
//        WebAppInfo webAppInfo = new WebAppInfo("https://www.youtube.com/");
//
//        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
//                new InlineKeyboardButton("url").webApp(webAppInfo),
//                new InlineKeyboardButton("Switch!").switchInlineQuery("switch_inline_query"));
//
//        SendMessage response = new SendMessage(chatId, "Стартовое меню")
//                .replyMarkup(inlineKeyboard);
//        this.execute(response);
//    }

//    private void process(Update update) {
//        Message message = update.message();
//
//        if(message != null) {
//            String text = message.text();
//
//            Optional.ofNullable(text)
//                    .ifPresent(commandName -> this.serveComment(commandName, message.chat().id(), message.chat().username()));
//
//        }
//        CallbackQuery callbackQuery = update.callbackQuery();
//
//        if(callbackQuery != null) {
//            String data = callbackQuery.data();
//            Optional.ofNullable(data)
//                    .ifPresent(commandName -> this.serveComment(commandName, callbackQuery.message().chat().id(),callbackQuery.message().chat().username()));
//        }
//    }

//    private void serveComment(String commandName, Long chatId, String username) {
//        switch (commandName) {
//            case "/start": {
//                InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup()
//                        .addRow(new InlineKeyboardButton("Клавиатуры").callbackData("Клавиатуры"))
//                        .addRow(new InlineKeyboardButton("Машинное состояние").callbackData("Машинное состояние"))
//                        .addRow(new InlineKeyboardButton("Платежная система").callbackData("Платежная система"))
//                        .addRow(new InlineKeyboardButton("API").callbackData("API"),
//                                new InlineKeyboardButton("СУБД").callbackData("СУБД"))
//                        .addRow(new InlineKeyboardButton("Медиа группы").callbackData("Медиа группы"));
//
//                SendMessage response = new SendMessage(chatId,"Список заданий")
//                        .replyMarkup(inlineKeyboard)
//                        ;
//
//
//                SendResponse execute = this.execute(response);
//                if(execute.message() != null) {
//                    if(execute.message().messageId() != null) {
//                        messageId = execute.message().messageId();
//                    }
//                }
//
//                break;
//            }
//            case "Клавиатуры": {
//                InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup()
//                        .addRow(new InlineKeyboardButton("Меню").callbackData("Меню"));
//
//                EditMessageText editMessageText = new EditMessageText(chatId, messageId, username)
//                        .replyMarkup(inlineKeyboard);
//
//                SendResponse execute = (SendResponse) this.execute(editMessageText);
//
//                if(execute.message() != null) {
//                    if(execute.message().messageId() != null) {
//                        messageId = execute.message().messageId();
//                    }
//                }
//
//                break;
//            } case "Меню" : {
//                InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup()
//                        .addRow(new InlineKeyboardButton("Клавиатуры").callbackData("Клавиатуры"))
//                        .addRow(new InlineKeyboardButton("Машинное состояние").callbackData("Машинное состояние"))
//                        .addRow(new InlineKeyboardButton("Платежная система").callbackData("Платежная система"))
//                        .addRow(new InlineKeyboardButton("API").callbackData("API"),
//                                new InlineKeyboardButton("СУБД").callbackData("СУБД"))
//                        .addRow(new InlineKeyboardButton("Медиа группы").callbackData("Медиа группы"));
//
//                EditMessageText editMessageText = new EditMessageText(chatId, messageId, "Список заданий")
//                        .replyMarkup(inlineKeyboard);
//
//                SendResponse execute = (SendResponse) this.execute(editMessageText);
//                if(execute.message() != null) {
//                    if(execute.message().messageId() != null) {
//                        messageId = execute.message().messageId();
//                    }
//                }
//
//                break;
//            }
//            case "Машинное состояние" : {
//
//                if(messageId != null) {
//                    EditMessageText editMessageText = new EditMessageText(chatId, messageId, "Введите имя");
//                    this.execute(editMessageText);
//                }
//
//                GetUpdates getUpdates = new GetUpdates().limit(100).offset(0).timeout(0);
//                GetUpdatesResponse updatesResponse = this.execute(getUpdates);
//                List<Update> updates = updatesResponse.updates();
//
//                while (updates != null) {
//                    getUpdates = new GetUpdates().limit(100).offset(0).timeout(0);
//                    updatesResponse = this.execute(getUpdates);
//                    updates = updatesResponse.updates();
//                }
//
//
//
////                SendMessage response = new SendMessage(chatId, "Введите пол")
////                        .replyMarkup(new ReplyKeyboardMarkup(new String[][] {
////                                {"Мужской", "Женский"}
////                        }).resizeKeyboard(true));
////
////                 this.execute(response);
//
//                break;
//            }
//            case "/help": {
//                SendMessage response = new SendMessage(chatId, "Вы ввели команду /help");
//                this.execute(response);
//                break;
//            }
//            case "/menu": {
//                SendMessage response = new SendMessage(chatId, "Меню")
//                        .replyMarkup(new ReplyKeyboardMarkup(new String[][] {
//                                {"Товары", "Отзывы"},
//                                {"Поддержка"}
//                        }).resizeKeyboard(true));
//                this.execute(response);
//                break;
//            }
//            case "/starMenu": {
//                WebAppInfo webAppInfo = new WebAppInfo("https://www.youtube.com/");
//
//                InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
//                        new InlineKeyboardButton("url").webApp(webAppInfo),
//                        new InlineKeyboardButton("Switch!").switchInlineQuery("switch_inline_query"));
//
//                SendMessage response = new SendMessage(chatId, "Стартовое меню")
//                        .replyMarkup(inlineKeyboard);
//                this.execute(response);
//                break;
//            }
//            default: {
//                SendMessage response = new SendMessage(chatId, "Команда не найдена");
//                this.execute(response);
//                break;
//            }
//        }
//    }
}
