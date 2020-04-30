package ru.telebotfm;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.telebotfm.db.DataBase;
import java.sql.SQLException;

public class Application {
    public static void main(String[] args) throws SQLException {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new SeliwanowaBot());

        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
