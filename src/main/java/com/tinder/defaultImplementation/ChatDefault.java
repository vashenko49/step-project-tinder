package com.tinder.defaultImplementation;

import com.tinder.dao.ChatDAO;
import com.tinder.exception.ChatException;
import com.tinder.exception.ErrorConnectionToDataBase;
import com.tinder.model.Chat;
import com.tinder.start.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class ChatDefault implements ChatDAO {
    private static volatile ChatDefault instance;
    private final BasicDataSource basicDataSource;

    private ChatDefault() throws ErrorConnectionToDataBase {
        basicDataSource = DataSource.getDataSource();
    }

    public static ChatDefault getInstance() throws ErrorConnectionToDataBase {
        if (instance == null) {
            synchronized (ChatDefault.class) {
                if (instance == null) {
                    instance = new ChatDefault();
                }
            }
        }
        return instance;
    }

    @Override
    public boolean isChatWithPartner(UUID user, UUID partner) throws ChatException {

        String sql = "select * " +
                "from chats " +
                "where (tou_id = ? and from_id = ?) " +
                "   or (from_id = ? and tou_id = ?) ";

        try (
                final Connection connection = basicDataSource.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, user);
            preparedStatement.setObject(2, partner);
            preparedStatement.setObject(3, user);
            preparedStatement.setObject(4, partner);

            final ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new ChatException("Error check exists chat");
        }
        return false;
    }

    @Override
    public UUID createChat(UUID user, UUID partner) throws ChatException {
        String sql = "insert into chats (from_id, tou_id)\n" +
                "VALUES (?,?);";
        try (
                final Connection connection = basicDataSource.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"chat_id"})) {

            preparedStatement.setObject(1, user);
            preparedStatement.setObject(2, partner);
            preparedStatement.executeUpdate();

            final ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            UUID object = null;

            if (generatedKeys != null && generatedKeys.next()) {
                object = generatedKeys.getObject(1, UUID.class);
            }

            return object;
        } catch (SQLException e) {
            throw new ChatException("Error create chat");
        }
    }

    @Override
    public List<Chat> getUserChats(UUID user) throws ChatException {
        List<Chat> chats = new ArrayList<>();

        String sql = "select ch.chat_id                                                                           as chat_id, " +
                "       ch.tou_id                                                                            as tou_id, " +
                "       ta1.first_name                                                                       as tou_name, " +
                "       ia1.img_url                                                                          as tou_img_url, " +
                "       ch.from_id                                                                           as from_id, " +
                "       ta2.first_name                                                                       as from_name, " +
                "       ia2.img_url                                                                          as from_img_url, " +
                "       sub_query.time_send                                                                  as time_send, " +
                "       sub_query.message_text                                                               as message_text, " +
                "       ( select count(*) from messages where read = false and ch.chat_id = messages.chat_id ) as number_unread " +
                "from chats as ch " +
                "         join lateral " +
                "    (select ms.message_text, ms.time_send " +
                "     from messages as ms " +
                "     where ms.chat_id = ch.chat_id " +
                "     order by ms.time_send desc " +
                "     limit 1) as sub_query on true " +
                "         join tinder_account ta1 on ch.from_id = ta1.user_id " +
                "         join tinder_account ta2 on ch.tou_id = ta2.user_id " +
                "         join images_account ia1 on ta1.user_id = ia1.tinder_account_id " +
                "         join images_account ia2 on ta2.user_id = ia2.tinder_account_id " +
                "where from_id = ? " +
                "   or tou_id = ? " +
                "order by time_send desc";

        try (
                final Connection connection = basicDataSource.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, user);
            preparedStatement.setObject(2, user);

            final ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                chats.add(Chat.builder()
                        .chat_id(resultSet.getObject("chat_id", UUID.class))
                        .tou_id(resultSet.getString("tou_id"))
                        .tou_name(resultSet.getString("tou_name"))
                        .tou_img_url(resultSet.getString("tou_img_url"))
                        .from_id(resultSet.getString("from_id"))
                        .from_name(resultSet.getString("from_name"))
                        .from_img_url(resultSet.getString("from_img_url"))
                        .from_img_url(resultSet.getString("from_img_url"))
                        .message_text(resultSet.getString("message_text"))
                        .time_send(resultSet.getDate("time_send"))
                        .number_unread(resultSet.getInt("number_unread"))
                        .build());
            }

            return chats;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ChatException("Error get user's chats");
        }
    }

    @Override
    public boolean dropChat(UUID chatId) throws ChatException {
        String sql = "delete from messages where chat_id=?";
        try (
                final Connection connection = basicDataSource.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, chatId);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new ChatException("Error clean messages");
        }
        sql = "delete from chats where chat_id=?";

        try (
                final Connection connection = basicDataSource.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, chatId);
            return !preparedStatement.execute();
        } catch (SQLException e) {
            throw new ChatException("Error drop chat");
        }
    }

    @Override
    public boolean makeChatReadForUser(UUID userId, UUID chatId) throws ChatException {
        String sql = "update messages set read=true where chat_id=? and (from_id=? or tou_id=?)";
        try (
                final Connection connection = basicDataSource.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, chatId);
            preparedStatement.setObject(2, userId);
            preparedStatement.setObject(3, userId);
            return !preparedStatement.execute();
        } catch (SQLException e) {
            throw new ChatException("Error make chat read for user");
        }
    }
}
