import * as SYSTEM from '../../config/System';
import * as SLIDE from "../../config/Slide";
import * as NOTISTACK from "../../config/Notistack";
import SlideAPI from "../../service/Slide";

export const nextSlide = (currentSlide, currentUsers, result) => async dispatch => {
    dispatch({
        type: SYSTEM.START_LOAD
    })

    const isMatch = await SlideAPI.slidePartner({
        partner: currentUsers[currentSlide].userId,
        result: result
    })


    if (isMatch.result) {
        dispatch({
            type: NOTISTACK.ENQUEUE_SNACKBAR,
            notification: {
                ...{
                    message: "You have new match"
                }
            }
        });
    }

    if (currentSlide + 2 > currentUsers.length) {
        SlideAPI.getPackAccountForUser()
            .then(res => {
                dispatch({
                    type: SLIDE.LOAD_USERS_FOR_LIKE_SUCCESS,
                    payload: {
                        user: res,
                        currentSlide: 0
                    }
                })
            })
            .catch(res => {
                dispatch({
                    type: SLIDE.LOAD_USERS_FOR_LIKE_FAILED
                })
            })
            .finally(() => {
                dispatch({
                    type: SYSTEM.STOP_LOAD
                })
            })
    } else {
        dispatch({
            type: SLIDE.NEXT_SLIDE,
            payload: currentSlide + 1
        });
        dispatch({
            type: SYSTEM.STOP_LOAD
        })
    }

}
