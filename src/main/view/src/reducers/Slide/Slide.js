import * as SLIDE from '../../config/Slide'

export const initialState = {
    users: [],
    currentSlide: 0
}


export default (state = initialState, action) => {
    switch (action.type) {
        case SLIDE.LOAD_USERS_FOR_LIKE_SUCCESS:
            return {
                ...state,
                users: action.payload.user,
                currentSlide:0
            }
        case SLIDE.NEXT_SLIDE:
            return {
                ...state,
                currentSlide: action.payload
            }
        case SLIDE.LOAD_USERS_FOR_LIKE_FAILED:
            return initialState
        default:
            return state
    }
}