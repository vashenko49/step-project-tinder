package com.tinder.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinder.exception.ChatException;
import com.tinder.exception.ConfigFileException;
import com.tinder.exception.ErrorConnectionToDataBase;
import com.tinder.exception.MessagesException;
import com.tinder.model.Chat;
import com.tinder.model.Message;
import com.tinder.model.Socket;
import com.tinder.oauth.UtilJWT;
import com.tinder.poolUsers.OperationWithUser;
import com.tinder.poolUsers.PoolUsers;
import com.tinder.service.ChatService;
import com.tinder.service.MessagesService;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;


@ServerEndpoint(value = "/api/v0/messages/{oauthMessages}")
public class MessagesController {
    private final ObjectMapper objectMapper;
    private final UtilJWT UTIL_JWT;
    private final ChatService CHAT_SERVICE;
    private final MessagesService MESSAGES_SERVICE;
    private final PoolUsers POOL_USERS;

    public MessagesController() throws ConfigFileException, ErrorConnectionToDataBase {
        UTIL_JWT = UtilJWT.getInstance();
        objectMapper = new ObjectMapper();
        CHAT_SERVICE = ChatService.getInstance();
        MESSAGES_SERVICE = MessagesService.getInstance();
        POOL_USERS = PoolUsers.getInstance();
    }

    @OnOpen
    public void onOpen(@PathParam("oauthMessages") String jwt, Session session) throws IOException {
        try {
            if (UTIL_JWT.verifyToken(jwt)) {
                final String userFromToken = UTIL_JWT.getUserFromToken(jwt);
                final UUID clientId = UUID.fromString(Objects.requireNonNull(userFromToken));
                final List<Chat> userChats = CHAT_SERVICE.getUserChats(clientId);
                final List<Message> messages = new ArrayList<>();

                final UUID activeChatInit = userChats.get(0).getChat_id();
                if (userChats.size() > 0) {
                    messages.addAll(MESSAGES_SERVICE.getMessagesForChat(activeChatInit, 0));
                }

                Map<String, Object> result = new HashMap<>();

                result.put("type", "GET_INIT_INFORMATION");
                result.put("userChats", userChats);
                result.put("messages", messages);
                result.put("activeChat", activeChatInit.toString());
                session.getBasicRemote().sendText(objectMapper.writeValueAsString(result));


                //когда кто то будет производить операции с ид партнера то он будет оповещен (в случае если он онлайн)
                //будет отправлен чат который на данным момент активный и смс
                //с бэк стороны имеем возможность управлять состоянием на клиенте, так как redux автоматически обработает смс сокета
                POOL_USERS.addToPoolUser(userFromToken, OperationWithUser.builder().activeChat(activeChatInit.toString()).updateMessage(page -> {
                    final List<Message> messagesCallBack = new ArrayList<>(MESSAGES_SERVICE.getMessagesForChat(activeChatInit, page));
                    Map<String, Object> resultCallBack = new HashMap<>();
                    resultCallBack.put("type", "RECEIVE_NEW_MESSAGES_IN_ACTIVE_CHAT");
                    resultCallBack.put("activeChat", activeChatInit.toString());
                    resultCallBack.put("page", page);
                    resultCallBack.put("messages", messagesCallBack);
                    session.getBasicRemote().sendText(objectMapper.writeValueAsString(resultCallBack));
                }).updateСhat(() -> {
                    final List<Chat> userChatsCallBack = CHAT_SERVICE.getUserChats(clientId);
                    Map<String, Object> resultCallBack = new HashMap<>();
                    resultCallBack.put("type", "RECEIVE_NEW_INFO_CHAT");
                    resultCallBack.put("userChats", userChatsCallBack);
                    session.getBasicRemote().sendText(objectMapper.writeValueAsString(resultCallBack));
                }).build());
            }
        } catch (ChatException | MessagesException e) {
            session.getBasicRemote()
                    .sendText(objectMapper
                            .writeValueAsString(Socket
                                    .builder()
                                    .type("ERROR_ON_SERVER")
                                    .message("Error server")
                                    .build()));
        }
        System.out.println("onOpen");
    }

    @OnMessage
    public void onMessage(@PathParam("oauthMessages") String jwt, String message, Session session) throws IOException {
        try {
            if (UTIL_JWT.verifyToken(jwt)) {
                final String userFromTokenString = UTIL_JWT.getUserFromToken(jwt);
                final UUID userFromToken = UUID.fromString(Objects.requireNonNull(userFromTokenString));
                Map<String, String> map = objectMapper.readValue(message, new TypeReference<Map<String, String>>() {
                });
                if (map.containsKey("type")) {
                    switch (map.get("type")) {
                        case "GET_INIT_INFORMATION":
                            final OperationWithUser operationByUserIdInit = POOL_USERS.getOperationByUserId(userFromTokenString);
                            operationByUserIdInit.getUpdateСhat().operation();
                            operationByUserIdInit.getUpdateMessage().operation(0);
                            break;
                        case "GET_MESSAGES_FOR_ACTIVE_CHAT":
                            final String activeChat = map.get("activeChat");
                            int pageReq = Integer.parseInt(map.get("page"));
                            if (!POOL_USERS.getOperationByUserId(userFromTokenString).getActiveChat().equals(activeChat)) {
                                POOL_USERS.getOperationByUserId(userFromTokenString).setActiveChat(activeChat);
                                POOL_USERS.getOperationByUserId(userFromTokenString).setUpdateMessage(page -> {
                                    final List<Message> messagesCallBack = new ArrayList<>(MESSAGES_SERVICE.getMessagesForChat(UUID.fromString(activeChat), page));
                                    Map<String, Object> resultCallBack = new HashMap<>();
                                    resultCallBack.put("type", "RECEIVE_NEW_MESSAGES_IN_ACTIVE_CHAT");
                                    resultCallBack.put("activeChat", activeChat);
                                    resultCallBack.put("page", page);
                                    resultCallBack.put("messages", messagesCallBack);
                                    session.getBasicRemote().sendText(objectMapper.writeValueAsString(resultCallBack));
                                });
                            }

                            final OperationWithUser operationByUserId = POOL_USERS.getOperationByUserId(userFromTokenString);
                            operationByUserId.getUpdateСhat().operation();
                            operationByUserId.getUpdateMessage().operation(pageReq);
                            break;
                        case "SEND_MESSAGE":
                            final String receiver = map.get("receiver");
                            UUID activeChatInit = null;
                            final boolean chatId = map.containsKey("chatId");
                            if (!chatId) {
                                if (!CHAT_SERVICE.isChatWithPartner(userFromToken, UUID.fromString(receiver))) {
                                    activeChatInit = CHAT_SERVICE.createChat(userFromToken, UUID.fromString(receiver));
                                }
                            } else {
                                activeChatInit = UUID.fromString(map.get("chatId"));
                            }

                            MESSAGES_SERVICE.sendMessage(
                                    activeChatInit,
                                    userFromToken,
                                    UUID.fromString(receiver),
                                    map.get("text"));
                            //если партнер по чату сейчас в сети оповестить его, прислать ему новые данные
                            if (POOL_USERS.containUser(receiver)) {
                                final OperationWithUser operationByUserIdReceiver = POOL_USERS.getOperationByUserId(receiver);
                                operationByUserIdReceiver.getUpdateMessage().operation(0);
                                operationByUserIdReceiver.getUpdateСhat().operation();
                            }
                            final OperationWithUser operationByUserIdSender = POOL_USERS.getOperationByUserId(userFromTokenString);
                            operationByUserIdSender.getUpdateСhat().operation();
                            operationByUserIdSender.getUpdateMessage().operation(0);
                            break;
                        case "MAKE_READ_CHAT":
                            final String chatID = map.get("chatID");
//                            final String receiverForRead = map.get("receiver");
                            CHAT_SERVICE.makeChatReadForUser(userFromToken, UUID.fromString(chatID));

                            final OperationWithUser operationByUserIdMadeRead = POOL_USERS.getOperationByUserId(userFromTokenString);
                            operationByUserIdMadeRead.getUpdateMessage().operation(0);
                            operationByUserIdMadeRead.getUpdateСhat().operation();
                            break;
                        case "DROP_MESSAGE":
                            final String receiverDrop = map.get("receiver");
                            final UUID messageIdDrop = UUID.fromString(map.get("messageIdDrop"));
                            MESSAGES_SERVICE.deleteMessage(messageIdDrop);

                            if (POOL_USERS.containUser(receiverDrop)) {
                                OperationWithUser operationReceiverDro = POOL_USERS.getOperationByUserId(receiverDrop);
                                operationReceiverDro.getUpdateСhat().operation();
                                operationReceiverDro.getUpdateMessage().operation(0);
                            }
                            final OperationWithUser operationByUserIdDroper = POOL_USERS.getOperationByUserId(userFromTokenString);
                            operationByUserIdDroper.getUpdateСhat().operation();
                            operationByUserIdDroper.getUpdateMessage().operation(0);
                            break;
                        case "DROP_CHAT":
                            final String receiverDropChat = map.get("receiver");
                            final UUID chatIdDrop = UUID.fromString(map.get("chatIdDrop"));
                            CHAT_SERVICE.dropChat(chatIdDrop);
                            if (POOL_USERS.containUser(receiverDropChat)) {
                                OperationWithUser operationReceiverDropChat = POOL_USERS.getOperationByUserId(receiverDropChat);
                                operationReceiverDropChat.getUpdateСhat().operation();
                            }
                            final OperationWithUser operationByUserIdDroperChat = POOL_USERS.getOperationByUserId(userFromTokenString);
                            operationByUserIdDroperChat.getUpdateСhat().operation();
                            break;
                        default:
                            session.getBasicRemote()
                                    .sendText(objectMapper
                                            .writeValueAsString(Socket
                                                    .builder()
                                                    .type("ERROR_TYPE")
                                                    .message("Send correct type")
                                                    .build()));
                            break;
                    }
                }
            }
        } catch (IOException | MessagesException |
                ChatException e) {
            session.getBasicRemote()
                    .sendText(objectMapper
                            .writeValueAsString(Socket
                                    .builder()
                                    .type("ERROR_ON_SERVER")
                                    .message(e.getMessage())
                                    .build()));
        }

    }

    @OnClose
    public void onClose(@PathParam("oauthMessages") String jwt, Session session) throws IOException {
        if (UTIL_JWT.verifyToken(jwt)) {
            POOL_USERS.dropUserFromPool(UTIL_JWT.getUserFromToken(jwt));
        }
        System.out.println("onClose");
    }

    @OnError
    public void onError(@PathParam("oauthMessages") String jwt, Session session, Throwable throwable) {
        if (UTIL_JWT.verifyToken(jwt)) {
            POOL_USERS.dropUserFromPool(UTIL_JWT.getUserFromToken(jwt));
        }
    }
}
