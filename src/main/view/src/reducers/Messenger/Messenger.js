import * as MESSENGER from '../../config/Messenger'
import _ from 'lodash';

export const initialState = {
    error: false,
    errorMessage: "",
    connected: false,
    connecting: false,
    unReadMessage: 0,
    chats: [],
    messages: [],
    activeChat: "",
    receiver: "",
    page: 0
}


export default (state = initialState, action) => {


    switch (action.type) {
        case "REDUX_WEBSOCKET::OPEN":
            return {
                ...state,
                error: false,
                errorMessage: "",
                connecting: false,
                connected: true
            }
        case "REDUX_WEBSOCKET::CONNECT":
            return {
                ...state,
                connected: true,
                connecting: true
            }
        case "REDUX_WEBSOCKET::MESSAGE":

            const payloadSocket = JSON.parse(action.payload.message);

            const {type} = payloadSocket;
            switch (type) {
                case MESSENGER.GET_INIT_INFORMATION: {
                    const {userChats, messages, activeChat} = payloadSocket
                    return {
                        ...state,
                        chats: userChats,
                        messages: messages,
                        activeChat: activeChat,
                        unReadMessage: _.sumBy(userChats, o => o.number_unread)
                    }
                }
                case MESSENGER.RECEIVE_NEW_INFO_CHAT: {
                    const {userChats} = payloadSocket;
                    return {
                        ...state,
                        chats: userChats,
                        unReadMessage: _.sumBy(userChats, o => o.number_unread)
                    }
                }
                case MESSENGER.RECEIVE_NEW_MESSAGES_IN_ACTIVE_CHAT: {
                    const {activeChat, messages, page} = payloadSocket;

                    if (page === 0) {
                        return {
                            ...state,
                            messages: messages,
                            activeChat: activeChat,
                            page: page,
                        }
                    } else {
                        return {
                            ...state,
                            activeChat: activeChat,
                            page: page,
                            messages: messages.concat(state.messages)
                        }
                    }
                }
                case MESSENGER.ERROR_ON_SERVER:
                case MESSENGER.ERROR_TYPE: {
                    const {message} = payloadSocket;
                    return {
                        ...state,
                        error: true,
                        errorMessage: message
                    }
                }
            }
            break;
        case "REDUX_WEBSOCKET::CLOSED": {
            return {
                ...state,
                error: true,
                errorMessage: "DISCONNECT"
            }
        }
        case MESSENGER.SET_RECEIVER:
            return {
                ...state,
                receiver: action.payload
            }
        case MESSENGER.SET_ACTIVE_CHAT:
            return {
                ...state,
                activeChat: action.payload
            }
        case MESSENGER.START_NEW_CHAT:
            return {
                ...state,
                activeChat: "",
                messages: [],
                receiver: action.payload,
                page: 0
            }
        case MESSENGER.CHAT_SIGN_OUT:
            return initialState
        default:
            return state
    }
}