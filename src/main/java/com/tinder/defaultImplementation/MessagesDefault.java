package com.tinder.defaultImplementation;


import com.tinder.dao.MessagesDAO;
import com.tinder.exception.ErrorConnectionToDataBase;
import com.tinder.exception.MessagesException;
import com.tinder.model.Message;
import com.tinder.start.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class MessagesDefault implements MessagesDAO {
    private static volatile MessagesDefault instance;
    private final BasicDataSource basicDataSource;

    private MessagesDefault() throws ErrorConnectionToDataBase {
        basicDataSource = DataSource.getDataSource();
    }

    public static MessagesDefault getInstance() throws ErrorConnectionToDataBase {
        if (instance == null) {
            synchronized (MessagesDefault.class) {
                if (instance == null) {
                    instance = new MessagesDefault();
                }
            }
        }
        return instance;
    }

    @Override
    public List<Message> getMessagesForChat(UUID chatId, int page) throws MessagesException {
        List<Message> messages = new ArrayList<>();
        String sql = "select * from messages where chat_id=? order by time_send desc offset ? limit 15";
        try (
                final Connection connection = basicDataSource.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, chatId);
            preparedStatement.setInt(2, page * 15);
            final ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                messages.add(Message.builder()
                        .messages_id(resultSet.getObject("messages_id", UUID.class))
                        .from_id(resultSet.getString("from_id"))
                        .tou_id(resultSet.getString("tou_id"))
                        .chat_id(resultSet.getString("chat_id"))
                        .message_text(resultSet.getString("message_text"))
                        .read(resultSet.getBoolean("read"))
                        .time_send(resultSet.getDate("time_send"))
                        .build());
            }

        } catch (SQLException e) {
            throw new MessagesException("Error get messages");
        }
        return messages;
    }

    @Override
    public boolean deleteMessage(UUID messageId) throws MessagesException {
        String sql = "delete from messages where messages_id=?";
        try (
                final Connection connection = basicDataSource.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, messageId);
            return !preparedStatement.execute();
        } catch (SQLException e) {
            throw new MessagesException("Error delete message");
        }
    }

    @Override
    public boolean sendMessage(UUID chatId, UUID userId, UUID receiver, String textMessage) throws MessagesException {
        String sql = "insert into messages(from_id, tou_id, chat_id, message_text) VALUES (?,?,?,?)";
        try (
                final Connection connection = basicDataSource.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, userId);
            preparedStatement.setObject(2, receiver);
            preparedStatement.setObject(3, chatId);
            preparedStatement.setString(4, textMessage);
            return !preparedStatement.execute();
        } catch (SQLException e) {
            throw new MessagesException("Error send message");
        }
    }
}
