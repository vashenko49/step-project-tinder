import React from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as UserAction from '../../actions/User/User';

const HomePage = (props) => {
    console.log(props);
    return (
        <div>
            Home Page
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
