import * as MESSENGER from '../../config/Messenger';

export const setReceiver = data => {
    return {
        type: MESSENGER.SET_ACTIVE_CHAT,
        payload: data
    };
}

export const startNewChat = data => {
    return {
        type: MESSENGER.START_NEW_CHAT,
        payload: data
    }
}