package com.tinder.dao;

import com.tinder.exception.ImageException;

import java.util.UUID;


public interface ImageDAO {
    String uploadImgUseUri(String uri) throws  ImageException;

    boolean dropImgByPublicId(String publicId) throws ImageException;

    boolean saveImgUrlInDataBaseByUserID(UUID userId, String publicId) throws ImageException;

    boolean dropImgUrlFromDataBaseByUserID(String publicId) throws ImageException;
}
