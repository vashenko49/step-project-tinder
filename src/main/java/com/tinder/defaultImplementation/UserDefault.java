package com.tinder.defaultImplementation;

import com.tinder.dao.UserDAO;
import com.tinder.exception.ImageException;
import com.tinder.model.AccountUser;
import com.tinder.start.DataSource;
import com.tinder.exception.ErrorConnectionToDataBase;
import com.tinder.exception.UserException;
import javafx.util.Pair;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;
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
    public UUID createUserFromForm(String email, String password, String firstName) throws UserException {
        String sql = "insert into tinder_account (email, first_name, password) values (?,?,?)";
        try (
                final Connection connection = basicDataSource.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"user_id"})) {

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, password);

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
            throw new UserException("Error upload user to data base", e);
        }
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

    @Override
    public UUID getUserUUIDByEmail(String email) throws UserException {
        String sql = "select user_id from tinder_account where email=?";
        UUID user = null;
        try (
                final Connection connection = basicDataSource.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);

            final ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = resultSet.getObject("user_id", UUID.class);
            }
        } catch (SQLException e) {
            throw new UserException("Error check user's email in data base");
        }
        return user;
    }

    @Override
    public AccountUser getUserDataByUserId(UUID userId) throws UserException, ImageException {
        AccountUser accountUser = null;

        String sql = "select * from tinder_account where user_id=?";
        try (
                final Connection connection = basicDataSource.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, userId);

            final ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                accountUser = AccountUser.builder()
                        .first_name(resultSet.getString("first_name"))
                        .age(resultSet.getInt("age"))
                        .interests(resultSet.getString("interests"))
                        .gender(resultSet.getString("gender"))
                        .genderpartner(resultSet.getString("genderpartner"))
                        .aboutMe(resultSet.getString("aboutMe"))
                        .max_distance(resultSet.getInt("max_distance"))
                        .max_age(resultSet.getInt("max_age"))
                        .min_age(resultSet.getInt("min_age"))
                        .password(resultSet.getString("password"))
                        .build();
            }
        } catch (SQLException e) {
            throw new UserException("Error check user's email in data base");
        }

        if (accountUser != null) {
            List<String> images = new ArrayList<>();
            sql = "select img_url from images_account where tinder_account_id=?";
            try (
                    final Connection connection = basicDataSource.getConnection();
                    final PreparedStatement preparedStatement = connection.prepareStatement(sql)
            ) {

                preparedStatement.setObject(1, userId);
                final ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    images.add(resultSet.getString("img_url"));
                }

                accountUser.setImagesList(images);
            } catch (SQLException e) {
                throw new ImageException("Error drop img url from data base");
            }
        }
        return accountUser;
    }

    @Override
    public Pair<String, String> getUserIdAndPasswordByEmail(String email) throws UserException {
        String sql = "select user_id, password from tinder_account where email=?";
        try (
                final Connection connection = basicDataSource.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);

            final ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Pair<>(resultSet.getString("user_id"), resultSet.getString("password"));
            }
        } catch (SQLException e) {
            throw new UserException("Error check user's email in data base");
        }
        return null;
    }


}
