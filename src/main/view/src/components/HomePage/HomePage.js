import React from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as SystemAction from '../../actions/System/System';

const HomePage = () => {
    return (
        <div>
5
        </div>
    );
};


const mapStateToProps = (state) => {
    return {User: state.User};
}

const mapDispatchToProps = (dispatch) => {
    return {
        startLoad: bindActionCreators(SystemAction.startLoad, dispatch)
    };
}


export default connect(mapStateToProps, mapDispatchToProps)(HomePage);
