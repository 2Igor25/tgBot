package ru.igorter.tgbot.utils;

import com.pengrad.telegrambot.model.request.LabeledPrice;
import com.pengrad.telegrambot.request.CreateInvoiceLink;
import org.springframework.stereotype.Component;

@Component
public final class LinkToPayUtil {

    private static String linkToPay;

    public static String getLinkToPay() {
        return linkToPay;
    }

    public static void setLinkToPay(String result) {
        linkToPay = result;
    }

    public static CreateInvoiceLink createInvoiceLink() {
        CreateInvoiceLink link = new CreateInvoiceLink("Наушники", "наушники", "1",
                BotsConfigUtil.getProviderToken(), "RUB",
                new LabeledPrice("Цена", 40000))
                .needPhoneNumber(true);

        return link;
    }

}
