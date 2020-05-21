import * as USER from '../../config/User';
import * as SYSTEM from '../../config/System';
import ServiceUserAPI from "../../service/User";
import _ from 'lodash';
import Cookie from 'js-cookie';
import * as NOTISTACK from "../../config/Notistack";
import Oauth from "../../util/Oauth";

export const getUserDataByJWT = () => dispatch => {
    let oauth = Cookie.get("oauth");
    if (_.isString(oauth)) {
        dispatch({
            type: SYSTEM.START_LOAD
        })
        ServiceUserAPI.getUserDataBYJWT().then(res => {

            console.log(res);
            dispatch({
                type: USER.GET_USER_DATA_BY_JWT_SUCCESS,
                payload: res
            })

            dispatch({
                type: NOTISTACK.ENQUEUE_SNACKBAR,
                notification: {
                    ...{
                        message: "Success get user's data"
                    }
                }
            });

        }).catch(() => {
            dispatch({
                type: USER.GET_USER_DATA_BY_JET_FAILED
            })
            dispatch({
                type: NOTISTACK.ENQUEUE_SNACKBAR,
                notification: {
                    ...{
                        message: "Error get user's data"
                    }
                }
            });
        }).finally(() => {
            dispatch({
                type: SYSTEM.STOP_LOAD
            })
        })
    }
}

export const signUpUser = (data, redirect) => dispatch => {
    dispatch({
        type: SYSTEM.START_LOAD
    })

    ServiceUserAPI.signUpUser(data)
        .then(res => {
            const {jwt} = res;
            document.cookie = "oauth=" + jwt;
            dispatch({
                type: NOTISTACK.ENQUEUE_SNACKBAR,
                notification: {
                    ...{
                        message: "Success get jwt token"
                    }
                }
            });

            ServiceUserAPI.getUserDataBYJWT()
                .then(res => {
                    dispatch({
                        type: USER.GET_USER_DATA_BY_JWT_SUCCESS,
                        payload: res
                    })

                    dispatch({
                        type: NOTISTACK.ENQUEUE_SNACKBAR,
                        notification: {
                            ...{
                                message: "Success get user's data"
                            }
                        }
                    });

                    redirect();

                })
                .catch(res => {
                    dispatch({
                        type: USER.GET_USER_DATA_BY_JET_FAILED
                    });
                    dispatch({
                        type: NOTISTACK.ENQUEUE_SNACKBAR,
                        notification: {
                            ...{
                                message: "Error get user's data "
                            }
                        }
                    });
                    throw res;
                })


        })
        .catch(e => {
            const {response: {data: {message}}} = e;
            dispatch({
                type: NOTISTACK.ENQUEUE_SNACKBAR,
                notification: {
                    ...{
                        message: message
                    }
                }
            });
        })
        .finally(() => {
            dispatch({
                type: SYSTEM.STOP_LOAD
            })
        })
}

export const signInUser = (data, redirect) => dispatch => {
    dispatch({
        type: SYSTEM.START_LOAD
    })
    ServiceUserAPI.signInUser(data)
        .then(res => {
            const {jwt} = res;
            document.cookie = "oauth=" + jwt;
            dispatch({
                type: NOTISTACK.ENQUEUE_SNACKBAR,
                notification: {
                    ...{
                        message: "Success get jwt token"
                    }
                }
            });

            ServiceUserAPI.getUserDataBYJWT()
                .then(res => {
                    dispatch({
                        type: NOTISTACK.ENQUEUE_SNACKBAR,
                        notification: {
                            ...{
                                message: "Success get user's data"
                            }
                        }
                    });
                    dispatch({
                        type: USER.GET_USER_DATA_BY_JWT_SUCCESS,
                        payload: res
                    })
                    redirect();
                })
                .catch(res => {
                    dispatch({
                        type: USER.GET_USER_DATA_BY_JET_FAILED
                    });
                    dispatch({
                        type: NOTISTACK.ENQUEUE_SNACKBAR,
                        notification: {
                            ...{
                                message: "Error get user's data"
                            }
                        }
                    });
                    throw res;
                })
        })
        .catch(e => {
            const {response: {data: {message}}} = e;
            dispatch({
                type: NOTISTACK.ENQUEUE_SNACKBAR,
                notification: {
                    ...{
                        message: message
                    }
                }
            });
        })
        .finally(() => {
            dispatch({
                type: SYSTEM.STOP_LOAD
            })
        })
}

export const SignOut = () => dispatch => {
    dispatch({
        type: SYSTEM.START_LOAD
    });

    dispatch({
        type: USER.SIGN_OUT
    });

    Oauth.removeCookies();
    dispatch({
        type: SYSTEM.STOP_LOAD
    });

}