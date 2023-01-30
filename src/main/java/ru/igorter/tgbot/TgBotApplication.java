package ru.igorter.tgbot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.igorter.tgbot.botapi.TelegramFacade;
import ru.igorter.tgbot.utils.BeanUtil;

@SpringBootApplication
public class TgBotApplication {
    private static final String BOT_TOKEN = "5918759811:AAEqFNXaGQXRsyjgSL4n6lHf79JOsPoDtQc";

    public static void main(String[] args) {
        SpringApplication.run(TgBotApplication.class, args);
        TelegramFacade telegramFacade = BeanUtil.getBean(TelegramFacade.class);

        TelegramBotApplication bot = TelegramBotApplication.builder()
                .botToken(BOT_TOKEN)
                .telegramFacade(telegramFacade)
                .build();

        bot.run();
    }

}
