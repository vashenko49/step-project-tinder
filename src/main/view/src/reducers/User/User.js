import * as USER from '../../config/User'

export const initialState = {
    isAuthorization: false,
    unReadMessage: 0,
    first_name: "",
    age: 18,
    interests: "",
    gender: "",
    genderpartner: '',
    aboutMe: "",
    max_distance: 10,
    min_age: 18,
    max_age: 22,
    imagesList: []
}


export default (state = initialState, action) => {
    const {type, payload} = action;
    switch (type) {
        case USER.LOG_IN_REQUEST:
            return {
                ...state,
                isAuthorization: true
            }
        case USER.GET_USER_DATA_BY_JWT_SUCCESS:
            return {
                ...state,
                isAuthorization: true,
                first_name: payload.first_name,
                age: payload.age,
                interests: payload.interests,
                gender: payload.gender,
                genderpartner: payload.genderpartner,
                aboutMe: payload.aboutMe,
                max_distance: payload.max_distance,
                min_age: payload.min_age,
                max_age: payload.max_age,
                imagesList: payload.imagesList,
            }
        case USER.EDIT_USER_DATA:
            return {
                ...state,
                isAuthorization: true,
                first_name: payload.first_name,
                age: payload.age,
                interests: payload.interests,
                gender: payload.gender,
                genderpartner: payload.genderpartner,
                aboutMe: payload.aboutMe,
                max_distance: payload.max_distance,
                min_age: payload.min_age,
                max_age: payload.max_age,
            }
        case USER.GET_USER_DATA_BY_JET_FAILED:
        case USER.SIGN_OUT:
            return initialState
        default:
            return state
    }
}