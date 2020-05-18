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
