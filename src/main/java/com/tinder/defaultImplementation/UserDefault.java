package com.tinder.defaultImplementation;

import com.tinder.dao.UserDAO;
import com.tinder.start.DataSource;
import com.tinder.exception.ErrorConnectionToDataBase;
import com.tinder.exception.UserException;
import com.tinder.model.AccountTinder;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.List;
import java.util.UUID;

public final class UserDefault implements UserDAO {
    private static volatile UserDefault instance;
    private final BasicDataSource basicDataSource;

    private UserDefault() throws ErrorConnectionToDataBase {
        basicDataSource = DataSource.getDataSource();
    }

    public static UserDefault getInstance() throws ErrorConnectionToDataBase {
        if (instance == null) {
            synchronized (UserDefault.class) {
                if (instance == null) {
                    instance = new UserDefault();
                }
            }
        }
        return instance;
    }

    @Override
    public UUID createUserFromForm(AccountTinder accountTinder) {
        return null;
    }

    @Override
    public UUID createUserFromGoogle(String firstName, String email) throws UserException {
        String sql = "insert into tinder_account (email, first_name) values (?,?)";
        try (
                final Connection connection = basicDataSource.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"user_id"})) {

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, firstName);

            preparedStatement.executeUpdate();

            final ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            UUID object = null;

            if (generatedKeys != null && generatedKeys.next()) {
                object = generatedKeys.getObject(1, UUID.class);
            }

            return object;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UserException("Error upload user to data base", e);
        }
    }

    @Override
    public boolean editUser(AccountTinder accountTinder) {
        return false;
    }

    @Override
    public boolean dropUser(UUID userID) throws UserException {
        String sql = "delete from tinder_account where user_id=?";
        try (
                final Connection connection = basicDataSource.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, userID);
            return !preparedStatement.execute();
        } catch (SQLException e) {
            throw new UserException("Error upload user to data base");
        }
    }

    @Override
    public AccountTinder getUserData(String email) {
        return null;
    }

    @Override
    public List<AccountTinder> getListUsersByUser(AccountTinder accountTinder) {
        return null;
    }

    @Override
    public boolean userExistByEmail(String email) throws UserException {
        String sql = "select email from tinder_account where email=?";
        try (
                final Connection connection = basicDataSource.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, email);

            final ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            throw new UserException("Error check user's email in data base");
        }
    }

}
