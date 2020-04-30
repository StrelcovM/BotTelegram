package ru.telebotfm;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.telebotfm.db.DataBase;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SeliwanowaBot extends TelegramLongPollingBot {
    private static final String BOT_TOKEN = "1021892547:AAGfcAZqDvoNDl2ML3pI02XPdlOJTkYT4Dw";
    private static final String BOT_USERNAME = "SeliwanovaAnnaBot";
    private DataBase dataBase;

    public SeliwanowaBot() {
        super();
        dataBase = DataBase.getInstance();
    }

    @Override
    public void onUpdateReceived(Update update) {
        String textMessage = update.getMessage().getText();

        if (textMessage.toLowerCase().equals("привет"))
            greeting(update);

        if (textMessage.toLowerCase().matches("добавить .+")) {
            if (!dataBase.isUserExist(update.getMessage().getChatId()))
                dataBase.insertNewUser(new User(textMessage.substring(9), update.getMessage().getChatId()));
            else
                sendMsg(update.getMessage().getChatId(), "[Error:SeliwanowaBot] Пользователь уже существует...");
        }

        if (textMessage.matches("\\d"))
            game(update.getMessage().getChatId(), Integer.parseInt(textMessage));

        if (textMessage.toLowerCase().equals("пользователи")) {
            StringBuilder stringBuilder = new StringBuilder();
            List<User> userList = dataBase.getAllUsers();

            for (User user : userList)
                stringBuilder.append(user.toString()).append('\n');

            sendMsg(update.getMessage().getChatId(),
                    "Пользователей в базе: " + userList.size() + "\n" + stringBuilder.toString());
            System.out.println("Отправлен список пользователей");
        }
    }

    private void greeting(Update update) {
        StringBuilder greeting = new StringBuilder();
        InputStreamReader inputStream = new InputStreamReader(getClass()
                .getResourceAsStream("/greeting.txt"), StandardCharsets.UTF_8);
        try {
            while (inputStream.ready())
                greeting.append((char) inputStream.read());
            System.out.println("Отправлено приветствие...");

        } catch (FileNotFoundException e) {
            sendMsg(update.getMessage().getChatId(), "[Error:SeliwanowaBot] Файл приветствия не найден...");
            e.printStackTrace();
            return;

        } catch (IOException e) {
            e.printStackTrace();
        }
        sendMsg(update.getMessage().getChatId(), greeting.toString());
    }

    private void game(long ChatId, int num) {
        int myNum = (int) (Math.random() * 6);

        System.out.println(myNum + " - " + num);

        if (num == myNum)
            sendMsg(ChatId, "Правильно! Поздравляю!");
        else {
            sendMsg(ChatId, "Нет, ты проиграл \nХочешь попробовать ещё? Просто отправь цифру от 1 до 5");
        }
    }

    public synchronized void sendMsg(long ChatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(ChatId);
        sendMessage.setText(s);
        setButtons(sendMessage);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public synchronized void setButtons(SendMessage sendMessage) {
        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardFirstRow.add(new KeyboardButton("Привет"));

        // Вторая строчка клавиатуры
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Добавляем кнопки во вторую строчку клавиатуры
        keyboardSecondRow.add(new KeyboardButton("Пользователи"));

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public String getBotUsername() {
        return BOT_USERNAME;
    }

    public String getBotToken() {
        return BOT_TOKEN;
    }

    public void setDataBase(DataBase dataBase) {
        this.dataBase = dataBase;
    }
}