import * as USER from '../../config/User';

export const logIn = () => dispatch => {
    dispatch({
        type: USER.LOG_IN_REQUEST
    })
}