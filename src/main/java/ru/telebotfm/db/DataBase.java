package ru.telebotfm.db;

import org.sqlite.JDBC;
import ru.telebotfm.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataBase {
    private static Connection conn;
    private static DataBase instance = null;
    private final String CON_STR = "c:\\data_base.sqlite";

    public static synchronized DataBase getInstance() {
        if (instance == null)
            try {
                instance = new DataBase();
                return instance;
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
            }
        return instance;
    }

    private DataBase() throws SQLException {
        DriverManager.registerDriver(new JDBC());
        this.conn = DriverManager.getConnection("jdbc:sqlite:" + CON_STR);
    }

    public List<User> getAllUsers() {
        try {
            Statement statement = this.conn.createStatement();
            List<User> users = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
            while (resultSet.next()) {
                users.add(new User(resultSet.getString("name"),
                        resultSet.getLong("ChatId")));
            }
            return users;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return Collections.emptyList();
        }
    }

    public void insertNewUser(User user) {
        try (PreparedStatement preparedStatement =
                     conn.prepareStatement("insert into users (name, ChatId) VALUES (?, ?)"))
        {
            preparedStatement.setObject(1, user.getName());
            preparedStatement.setObject(2, user.getChatId());
            preparedStatement.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean isUserExist(long ChatId) {
        try (PreparedStatement preparedStatement =
                     conn.prepareStatement("select 1 from users where ChatId= ?")) {

            preparedStatement.setObject(1, ChatId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return true;
            else return false;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }
}
