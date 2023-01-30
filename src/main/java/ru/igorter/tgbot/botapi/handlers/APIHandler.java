package ru.igorter.tgbot.botapi.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.igorter.tgbot.botapi.BotState;
import ru.igorter.tgbot.botapi.InputMessageHandler;
import ru.igorter.tgbot.module.USD;
import ru.igorter.tgbot.utils.LinkToPayUtil;


@Component
public class APIHandler implements InputMessageHandler {
    @Override
    public BaseRequest handle(Message message) {
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        String url = "https://www.cbr-xml-daily.ru/daily_json.js";


        return null;
    }

    @Override
    public BaseRequest handle(CallbackQuery callbackQuery) {
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        String url = "https://www.cbr-xml-daily.ru/daily_json.js";
        String response = restTemplate.getForObject(url, String.class);

        int usd = response.indexOf("USD");
        String substring = response.substring(usd);
        int i = substring.indexOf("}");
        usd = substring.indexOf("{");
        substring = substring.substring(usd, i+1);


        Gson gson = new Gson();

        USD exchangeRate = gson.fromJson(substring, USD.class);

        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup()
                .addRow(new InlineKeyboardButton("Клавиатуры").callbackData("Клавиатуры"))
                .addRow(new InlineKeyboardButton("Машинное состояние").callbackData("Машинное состояние"))
                .addRow(new InlineKeyboardButton("Платежная система").url(LinkToPayUtil.getLinkToPay()))
                .addRow(new InlineKeyboardButton("API").callbackData("API"),
                        new InlineKeyboardButton("СУБД").callbackData("СУБД"));
                //.addRow(new InlineKeyboardButton("Медиа группы").callbackData("Медиа группы"));

        long chatId = callbackQuery.message().chat().id();
        int messageId = callbackQuery.message().messageId();

        EditMessageText editMessageText = new EditMessageText(chatId, messageId, "Курс доллара: " + exchangeRate.getValue())
                .replyMarkup(inlineKeyboard);

        return editMessageText;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.API;
    }
}
