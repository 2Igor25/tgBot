package ru.igorter.tgbot.botapi.handlers;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.LabeledPrice;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.CreateInvoiceLink;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;
import ru.igorter.tgbot.botapi.BotState;
import ru.igorter.tgbot.botapi.InputMessageHandler;
import ru.igorter.tgbot.utils.LinkToPayUtil;

@Component
public class PayHandler implements InputMessageHandler {
    @Override
    public BaseRequest handle(Message message) {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup()
                .addRow(new InlineKeyboardButton("Клавиатуры").callbackData("Клавиатуры"))
                .addRow(new InlineKeyboardButton("Машинное состояние").callbackData("Машинное состояние"))
                .addRow(new InlineKeyboardButton("Платежная система").url(LinkToPayUtil.getLinkToPay()))
                .addRow(new InlineKeyboardButton("API").callbackData("API"),
                        new InlineKeyboardButton("СУБД").callbackData("СУБД"));
                //.addRow(new InlineKeyboardButton("Медиа группы").callbackData("Медиа группы"));

        long chatId = message.chat().id();

        int price = message.successfulPayment().totalAmount();


        SendMessage response = new SendMessage(chatId, "наушники" + " " + price/100)
                .replyMarkup(inlineKeyboard);

        return response;
    }

    @Override
    public BaseRequest handle(CallbackQuery callbackQuery) {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup()
                .addRow(new InlineKeyboardButton("Клавиатуры").callbackData("Клавиатуры"))
                .addRow(new InlineKeyboardButton("Машинное состояние").callbackData("Машинное состояние"))
                .addRow(new InlineKeyboardButton("Платежная система").url(LinkToPayUtil.getLinkToPay()))
                .addRow(new InlineKeyboardButton("API").callbackData("API"),
                        new InlineKeyboardButton("СУБД").callbackData("СУБД"));
                //.addRow(new InlineKeyboardButton("Медиа группы").callbackData("Медиа группы"));

        long chatId = callbackQuery.message().chat().id();

        int price = callbackQuery.message().successfulPayment().totalAmount();


        SendMessage response = new SendMessage(chatId, "наушники" + " " + price/100)
                .replyMarkup(inlineKeyboard);

        return response;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.PAY_SYSTEM;
    }
}
