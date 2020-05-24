package com.tinder.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinder.exception.ConfigFileException;
import com.tinder.model.Error;
import com.tinder.oauth.UtilJWT;
import com.tinder.poolUsers.Operation;
import com.tinder.poolUsers.OperationWithUser;
import com.tinder.poolUsers.PoolUsers;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;


@ServerEndpoint(value = "/api/v0/chat/{oauth}")
public class ChatController {
    private final UtilJWT UTIL_JWT;
    private final PoolUsers POOL_USERS;
    private final ObjectMapper objectMapper;

    public ChatController() throws ConfigFileException {
        UTIL_JWT = UtilJWT.getInstance();
        POOL_USERS = PoolUsers.getInstance();
        objectMapper = new ObjectMapper();
    }

    @OnOpen
    public void onOpen(@PathParam("oauth") String clientId, Session session) throws IOException {
        final boolean b = UTIL_JWT.verifyToken(clientId);
        if (b) {
            final String userFromToken = UTIL_JWT.getUserFromToken(clientId);
            Operation updateUserChat = () -> {
                session.getBasicRemote().sendText(userFromToken);
            };
            if (POOL_USERS.containUser(userFromToken)) {
                POOL_USERS.getOperationByUserId(userFromToken).setUpdateChat(updateUserChat);
            } else {
                POOL_USERS.addToPoolUser(userFromToken, OperationWithUser.builder().updateChat(updateUserChat).build());
            }
        } else {
            session.close();
        }
    }

    @OnMessage
    public void onMessage(@PathParam("oauth") String clientId, Session session, String message) throws IOException {
        final boolean b = UTIL_JWT.verifyToken(clientId);
        if (b) {
            Map map = objectMapper.readValue(message, Map.class);
            if (map.containsKey("type") && map.containsKey("message") && map.containsKey("to")) {
                final String userFromToken = UTIL_JWT.getUserFromToken(clientId);
                final String messageFromUser = (String) map.get("message");
                final String to = (String) map.get("to");
                final String type = (String) map.get("type");


                switch (type) {
                    case "GET_CHAT":
                        break;
                    default:
                        session.getBasicRemote().sendText(objectMapper.writeValueAsString(Error.builder().status(404).message("Not found type").build()));
                }

                if (POOL_USERS.containUser(message)) {
                    final OperationWithUser operationByUserId = POOL_USERS.getOperationByUserId(message);
                    operationByUserId.getUpdateChat().operation();
                }
            }
        } else {
            session.close();
        }
    }

    @OnClose
    public void onClose(@PathParam("oauth") String clientId, Session session) throws IOException {
        final boolean b = UTIL_JWT.verifyToken(clientId);
        if (b) {
            POOL_USERS.dropUserFromPool(UTIL_JWT.getUserFromToken(clientId));
        }
        session.close();
    }
}
