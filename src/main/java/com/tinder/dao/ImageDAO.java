package com.tinder.dao;

import com.tinder.exception.ImageException;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;


public interface ImageDAO {
    String uploadImgUseUri(String uri) throws ImageException;

    public String uploadImg(Part file) throws ImageException;

    boolean dropImgByPublicId(String publicId) throws ImageException;

    boolean saveImgUrlInDataBaseByUserID(UUID userId, String publicId) throws ImageException;

    boolean dropImgUrlFromDataBaseByPublicId(String publicId) throws ImageException;

}
