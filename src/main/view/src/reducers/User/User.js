import * as USER from '../../config/User'

export const initialState = {
    load: false,
    isAuthorization:false
}


export default (state = initialState, action) => {
    switch (action.type) {
        case USER.LOG_IN_REQUEST:
            return {
                ...state,
                load: true
            }
        default:
            return state
    }
}