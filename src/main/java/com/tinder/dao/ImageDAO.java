package com.tinder.dao;

import java.io.File;
import java.util.List;

public interface ImageDAO {
    String uploadImage(File file);

    boolean dropImage(String url);

    List<String> uploadImages(List<File> files);

    List<Boolean> dropImages(List<File> files);
}
