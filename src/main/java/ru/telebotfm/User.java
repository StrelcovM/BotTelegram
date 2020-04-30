package ru.telebotfm;

public class User {
    private String name;
    private long ChatId;

    public User(String name, long chatId) {
        this.name = name;
        ChatId = chatId;
    }

    public String getName() {
        return name;
    }

    public long getChatId() {
        return ChatId;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", ChatId=" + ChatId +
                '}';
    }
}
