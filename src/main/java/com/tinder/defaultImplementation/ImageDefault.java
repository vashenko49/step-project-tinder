package com.tinder.defaultImplementation;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.tinder.dao.ImageDAO;
import com.tinder.exception.ConfigFileException;
import com.tinder.exception.ErrorConnectionToDataBase;
import com.tinder.exception.ImageException;
import com.tinder.start.ConfigFile;
import com.tinder.start.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.servlet.http.Part;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;


public final class ImageDefault implements ImageDAO {
    private static volatile ImageDefault instance;
    private final Cloudinary cloudinary;
    private final BasicDataSource basicDataSource;

    private ImageDefault() throws ConfigFileException, ErrorConnectionToDataBase {
        final ConfigFile CONFIG_FILE = ConfigFile.getInstance();
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CONFIG_FILE.getValueByKey("cd.name"),
                "api_key", CONFIG_FILE.getValueByKey("cd.key"),
                "api_secret", CONFIG_FILE.getValueByKey("cd.secret")));
        basicDataSource = DataSource.getDataSource();
    }

    public static ImageDefault getInstance() throws ConfigFileException, ErrorConnectionToDataBase {
        if (instance == null) {
            synchronized (ImageDefault.class) {
                if (instance == null) {
                    instance = new ImageDefault();
                }
            }
        }
        return instance;
    }

    @Override
    public String uploadImgUseUri(String uri) throws ImageException {
        try {
            final Map responsive_breakpoints = cloudinary.uploader().upload(uri, ObjectUtils.asMap("folder", "tinder", "format", "png"));
            return (String) responsive_breakpoints.get("public_id");
        } catch (IOException e) {
            throw new ImageException("Error upload img to cloudinary");
        }
    }

    @Override
    public String uploadImg(Part file) throws ImageException {
        try {
            final Map upload = cloudinary.uploader().uploadLarge(file.getInputStream(), ObjectUtils.asMap("folder", "tinder", "format", "jpg"));
            return (String) upload.get("public_id");
        } catch (IOException e) {
            e.printStackTrace();
            throw new ImageException("Error upload img to cloudinary");
        }
    }

    @Override
    public boolean dropImgByPublicId(String publicId) throws ImageException {
        try {
            final Map destroy = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return destroy.get("result").equals("ok");
        } catch (IOException e) {
            throw new ImageException("Error drop img from cloudinary");
        }
    }

    @Override
    public boolean saveImgUrlInDataBaseByUserID(UUID userId, String publicId) throws ImageException {
        String sql = "insert into images_account (img_url, tinder_account_id) VALUES (?,?)";
        try (
                final Connection connection = basicDataSource.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, publicId);
            preparedStatement.setObject(2, userId);

            return !preparedStatement.execute();
        } catch (SQLException e) {
            throw new ImageException("Error upload img url to data base");
        }
    }

    @Override
    public boolean dropImgUrlFromDataBaseByPublicId(String publicId) throws ImageException {
        String sql = "delete from images_account where img_url=?";
        try (
                final Connection connection = basicDataSource.getConnection();
                final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, publicId);
            return !preparedStatement.execute();
        } catch (SQLException e) {
            throw new ImageException("Error drop img url from data base");
        }
    }
}
