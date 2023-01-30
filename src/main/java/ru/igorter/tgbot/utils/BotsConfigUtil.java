package ru.igorter.tgbot.utils;

import org.springframework.stereotype.Component;

@Component
public final class BotsConfigUtil {
    private static String providerToken = "381764678:TEST:49133";
    public static String getProviderToken() {
        return providerToken;
    }
}
