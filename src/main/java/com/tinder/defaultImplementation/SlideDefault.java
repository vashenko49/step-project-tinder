package com.tinder.defaultImplementation;

import com.tinder.dao.SlidedDAO;
import com.tinder.exception.ErrorConnectionToDataBase;
import com.tinder.exception.SlideException;
import com.tinder.model.AccountUser;
import com.tinder.start.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class SlideDefault implements SlidedDAO {

    private static volatile SlideDefault instance;
    private final BasicDataSource basicDataSource;

    private SlideDefault() throws ErrorConnectionToDataBase {
        basicDataSource = DataSource.getDataSource();
    }

    public static SlideDefault getInstance() throws ErrorConnectionToDataBase {
        if (instance == null) {
            synchronized (SlideDefault.class) {
                if (instance == null) {
                    instance = new SlideDefault();
                }
            }
        }
        return instance;
    }

    @Override
    public List<AccountUser> getPackUserForLike(UUID userId) throws SlideException {
        AccountUser accountUser = null;
        List<AccountUser> accountUsers = new ArrayList<>();

        String sql = "select genderpartner, min_age, max_age from tinder_account where user_id=?";
        try (
                final Connection connection = basicDataSource.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, userId);

            final ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                accountUser = AccountUser
                        .builder()
                        .genderpartner(resultSet.getString("genderpartner"))
                        .min_age(resultSet.getInt("min_age"))
                        .max_age(resultSet.getInt("max_age"))
                        .build();
            }
        } catch (SQLException e) {
            throw new SlideException("Error get user option");
        }

        if (accountUser != null && accountUser.getGenderpartner() != null) {


            sql = "select first_name, age, interests, aboutme, img_url, user_id " +
                    "from tinder_account as ta " +
                    "join images_account ia on ta.user_id = ia.tinder_account_id " +
                    "where user_id not in (select second_like from slided where first_like = ?) " +
                    "  and gender = ? " +
                    "  and age >= ? " +
                    "  and age <= ? " +
                    "limit 10; ";
            try (
                    final Connection connection = basicDataSource.getConnection();
                    final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setObject(1, userId);
                preparedStatement.setObject(2, accountUser.getGenderpartner());
                preparedStatement.setObject(3, accountUser.getMin_age());
                preparedStatement.setObject(4, accountUser.getMax_age());

                final ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    accountUsers.add(AccountUser.builder()
                            .first_name(resultSet.getString("first_name"))
                            .age(resultSet.getInt("age"))
                            .interests(resultSet.getString("interests"))
                            .aboutMe(resultSet.getString("aboutme"))
                            .img_url(resultSet.getString("img_url"))
                            .userId(resultSet.getString("user_id"))
                            .build());
                }

            } catch (SQLException e) {
                throw new SlideException("Error get partner");
            }
        }
        return accountUsers;
    }

    @Override
    public List<AccountUser> getMatch(UUID userId) throws SlideException {
        List<AccountUser> accountUsers = new ArrayList<>();
        String sql = "select user_id, first_name, age, interests, aboutme, img_url " +
                "from slided as s1 " +
                "full outer join slided as s2 on s1.second_like=s2.first_like " +
                "right join tinder_account ta on s1.second_like = ta.user_id " +
                "left join images_account ia on ta.user_id = ia.tinder_account_id " +
                "where s1.first_like=? " +
                "and s1.result=true " +
                "and s2.result=true " +
                "group by user_id, img_url";
        try (
                final Connection connection = basicDataSource.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, userId);

            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                accountUsers.add(AccountUser.builder()
                        .first_name(resultSet.getString("first_name"))
                        .age(resultSet.getInt("age"))
                        .interests(resultSet.getString("interests"))
                        .aboutMe(resultSet.getString("aboutme"))
                        .img_url(resultSet.getString("img_url"))
                        .userId(resultSet.getString("user_id"))
                        .build());
            }
        } catch (SQLException e) {
            throw new SlideException("Error get match");
        }
        return accountUsers;
    }

    @Override
    public boolean slideAndIsMatch(UUID userId, UUID partner, boolean result) throws SlideException {
        boolean isMatch = false;
        String sql = "insert into slided(first_like, second_like, result) VALUES (?, ?,?)";
        try (
                final Connection connection = basicDataSource.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, userId);
            preparedStatement.setObject(2, partner);
            preparedStatement.setBoolean(3, result);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SlideException("Error set slide");
        }

        sql = "select s1.first_like, s1.second_like " +
                "from slided as s1 full outer join slided as s2 " +
                "on s1.second_like=s2.first_like " +
                "where s1.first_like=? " +
                "and s1.second_like=? " +
                "and s1.result=true " +
                "and s2.result=true";
        try (
                final Connection connection = basicDataSource.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, userId);
            preparedStatement.setObject(2, partner);

            final ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                isMatch = true;
            }
        } catch (SQLException e) {
            throw new SlideException("Error find match");
        }


        return isMatch;
    }
}
