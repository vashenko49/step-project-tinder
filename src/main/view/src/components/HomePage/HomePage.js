import React from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as UserAction from '../../actions/User/User';
import _ from "lodash";

const urlParams = new URLSearchParams(window.location.search);
const HomePage = () => {
    _.isString(urlParams.get("oauth")) && (document.cookie = "oauth=" + urlParams.get("oauth"));
    return (
        <div>
            <a href={'https://accounts.google.com/o/oauth2/auth?scope=profile email&response_type=code&access_type=offline&redirect_uri=http://localhost:8080/api/v0/google&client_id=206183164477-qeh7n71mhlf4au9f236fc1i8tr62r080.apps.googleusercontent.com'}>Sing
                Up</a>
        </div>
    );
};


const mapStateToProps = (state) => {
    return {User: state.User};
}

const mapDispatchToProps = (dispatch) => {
    return {
        logIn: bindActionCreators(UserAction.logIn, dispatch)
    };
}


export default connect(mapStateToProps, mapDispatchToProps)(HomePage);
