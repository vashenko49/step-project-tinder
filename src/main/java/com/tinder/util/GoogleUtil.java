package com.tinder.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinder.exception.ConfigFileException;
import com.tinder.exception.GoogleException;
import com.tinder.exception.ImageException;
import com.tinder.exception.UserException;
import com.tinder.start.ConfigFile;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public final class GoogleUtil {
    final static ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, String> getUserDataUseCode(String code) throws GoogleException, IOException, ConfigFileException {

        Map<String, String> userData = new HashMap<>();
        final ConfigFile instance;
        instance = ConfigFile.getInstance();
        //body post request form data
        Map<String, String> body = new HashMap<>();
        body.put("grant_type", "authorization_code");
        body.put("code", code);
        body.put("client_id", instance.getValueByKey("google.clientID"));
        body.put("client_secret", instance.getValueByKey("google.secret"));
        body.put("redirect_uri", instance.getValueByKey("google.redirect_uri"));


        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, String> param : body.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(param.getKey());
            postData.append('=');
            postData.append(param.getValue());
        }
        byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);

        //post request for get user token from google
        URL urlGetAccessToken = new URL("https://accounts.google.com/o/oauth2/token");
        HttpURLConnection connectionGetAccessTokenToUser = (HttpURLConnection) urlGetAccessToken.openConnection();
        connectionGetAccessTokenToUser.setRequestMethod("POST");
        connectionGetAccessTokenToUser.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connectionGetAccessTokenToUser.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        connectionGetAccessTokenToUser.setDoOutput(true);
        connectionGetAccessTokenToUser.getOutputStream().write(postDataBytes);
        connectionGetAccessTokenToUser.connect();


        if (connectionGetAccessTokenToUser.getResponseCode() == 200) {

            JsonNode jsonMap = objectMapper.readTree(connectionGetAccessTokenToUser.getInputStream());
            final String access_token = jsonMap.get("access_token").toString().replace("\"", "");

            //after receiving the access token, get the user data
            URL getUserData = new URL("https://www.googleapis.com/oauth2/v3/userinfo");
            HttpURLConnection connectionGetUserData = (HttpURLConnection) getUserData.openConnection();
            connectionGetUserData.setRequestMethod("GET");
            connectionGetUserData.setRequestProperty("Authorization", ("Bearer " + access_token));
            connectionGetUserData.connect();


            if (connectionGetUserData.getResponseCode() == 200) {
                final JsonNode jsonNode = objectMapper.readTree(connectionGetUserData.getInputStream());
                String firstName = jsonNode.get("given_name").toString().replace("\"", "");
                String email = jsonNode.get("email").toString().replace("\"", "");
                String imgUrl = jsonNode.get("picture").toString().replace("\"", "");

                userData.put("firstName", firstName);
                userData.put("email", email);
                userData.put("imgUrl", imgUrl);

                return userData;
            } else {
                throw new GoogleException("Error getting user data use access token");
            }

        } else {
            throw new GoogleException("Error getting access token");
        }
    }
}
