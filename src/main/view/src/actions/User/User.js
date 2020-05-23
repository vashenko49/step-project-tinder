import * as USER from '../../config/User';
import * as SYSTEM from '../../config/System';
import ServiceUserAPI from "../../service/User";
import _ from 'lodash';
import Cookie from 'js-cookie';
import * as NOTISTACK from "../../config/Notistack";
import * as SLIDE from "../../config/Slide";
import Oauth from "../../util/Oauth";
import SlideAPI from "../../service/Slide";

export const getUserDataByJWT = () => dispatch => {
    let oauth = Cookie.get("oauth");
    if (_.isString(oauth)) {
        dispatch({
            type: SYSTEM.START_LOAD
        })
        ServiceUserAPI.getUserDataBYJWT().then(res => {
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

            SlideAPI.getPackAccountForUser()
                .then(res => {
                    dispatch({
                        type: SLIDE.LOAD_USERS_FOR_LIKE_SUCCESS,
                        payload: {
                            user:res,
                            currentSlide:0
                        }
                    })
                })
                .catch(e => {
                    dispatch({
                        type: SLIDE.LOAD_USERS_FOR_LIKE_FAILED
                    })
                    throw e;
                })

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

                    SlideAPI.getPackAccountForUser()
                        .then(res => {
                            dispatch({
                                type: SLIDE.LOAD_USERS_FOR_LIKE_SUCCESS,
                                payload: {
                                    user:res,
                                    currentSlide:0
                                }
                            })
                        })
                        .catch(e => {
                            dispatch({
                                type: SLIDE.LOAD_USERS_FOR_LIKE_FAILED
                            })
                            throw e;
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

                    SlideAPI.getPackAccountForUser()
                        .then(res => {
                            dispatch({
                                type: SLIDE.LOAD_USERS_FOR_LIKE_SUCCESS,
                                payload: {
                                    user:res,
                                    currentSlide:0
                                }
                            })
                        })
                        .catch(e => {
                            dispatch({
                                type: SLIDE.LOAD_USERS_FOR_LIKE_FAILED
                            })
                            throw e;
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

export const editData = data => dispatch => {
    dispatch({
        type: SYSTEM.START_LOAD
    });

    ServiceUserAPI.editUserData(data)
        .then(res => {
            dispatch({
                type: USER.EDIT_USER_DATA,
                payload: res
            })

            dispatch({
                type: NOTISTACK.ENQUEUE_SNACKBAR,
                notification: {
                    ...{
                        message: "Success change user data"
                    }
                }
            });

            SlideAPI.getPackAccountForUser()
                .then(res => {
                    dispatch({
                        type: SLIDE.LOAD_USERS_FOR_LIKE_SUCCESS,
                        payload: {
                            user:res,
                            currentSlide:0
                        }
                    })
                })
                .catch(e => {
                    dispatch({
                        type: SLIDE.LOAD_USERS_FOR_LIKE_FAILED
                    })
                    throw e;
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
        });
};

export const changePassword = data => dispatch => {
    dispatch({
        type: SYSTEM.START_LOAD
    });

    ServiceUserAPI.changePassword(data)
        .then(res => {
            const {message} = res;
            dispatch({
                type: NOTISTACK.ENQUEUE_SNACKBAR,
                notification: {
                    ...{
                        message: message
                    }
                }
            });
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
};

export const changeImg = data => dispatch => {
    dispatch({
        type: SYSTEM.START_LOAD
    });

    ServiceUserAPI.changeImgUser(data)
        .then(res => {
            const {message} = res;
            dispatch({
                type: NOTISTACK.ENQUEUE_SNACKBAR,
                notification: {
                    ...{
                        message: message
                    }
                }
            });
            ServiceUserAPI.getUserDataBYJWT()
                .then(res => {
                    dispatch({
                        type: USER.GET_USER_DATA_BY_JWT_SUCCESS,
                        payload: res
                    })
                })
                .catch(res => {
                    dispatch({
                        type: USER.GET_USER_DATA_BY_JET_FAILED
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